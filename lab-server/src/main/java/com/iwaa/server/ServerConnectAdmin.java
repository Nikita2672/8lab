package com.iwaa.server;

import com.iwaa.common.util.network.Request;
import com.iwaa.common.util.network.Serializer;
import com.iwaa.common.util.state.State;
import com.iwaa.server.channels.ChannelState;
import com.iwaa.server.request.RequestExecutor;
import com.iwaa.server.request.RequestReader;
import com.iwaa.server.response.CommandResultSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ServerConnectAdmin implements Runnable {

    private static final int SELECT_DELAY = 1000;
    private final ConcurrentHashMap<SocketChannel, ByteBuffer> channels = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final RequestExecutor requestExecutor;
    private final ConcurrentHashMap<SocketChannel, ChannelState> channelsState = new ConcurrentHashMap<>();
    private final State state;

    public ServerConnectAdmin(RequestExecutor requestExecutor, State state) throws IOException {
        this.state = state;
        this.requestExecutor = requestExecutor;
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        while (true) {
            try {
                serverChannel.socket().bind(new InetSocketAddress(inputPort()));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Port must be in the range from 0 to 65535.");
            } catch (IOException e) {
                System.out.println("Port is already in use.");
            }
        }
    }

    private int inputPort() throws IOException {
        while (true) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter port:");
                String port = reader.readLine();
                if (port == null) {
                    throw new IOException();
                }
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
                System.out.println("Enter number");
            }
        }
    }

    public void run() {
        try {
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    private void close() {
        try {
            selector.close();
            serverChannel.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void listen() throws IOException {
        while (state.getPerformanceStatus()) {
            selector.select(SELECT_DELAY);
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isValid()) {
                        if (key.isAcceptable()) {
                            accept();
                        } else if (key.isReadable()) {
                            read(key);
                        } else if (key.isWritable()) {
                            write(key);
                        }
                    }
                } catch (IOException e) {
                    if (key.channel().isOpen()) {
                        kill((SocketChannel) key.channel());
                    }
                }
            }
        }
        executorService.shutdown();
    }

    private void accept() throws IOException {
        SocketChannel channel = serverChannel.accept();
        channels.put(channel, ByteBuffer.allocate(0));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        channelsState.put(channel, ChannelState.READY_TO_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channelsState.get(channel) == ChannelState.READY_TO_READ) {
            channelsState.put(channel, ChannelState.READING);
            RequestReader requestReader = new RequestReader(channel, channels, selector, channelsState);
            CompletableFuture.runAsync(requestReader, executorService);
        }
        if (channels.get((SocketChannel) key.channel()) == null) {
            throw new IOException();
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        if (channelsState.get(channel) == ChannelState.READY_TO_WRITE) {
            channelsState.put(channel, ChannelState.WRITING);
            Serializer serializer = new Serializer();

            Request request = (Request) serializer.deserialize(channels.get((SocketChannel) key.channel()).array());

            CommandResultSender commandResultSender = new CommandResultSender(channel, channels, selector, channelsState);

            CompletableFuture.supplyAsync(() -> requestExecutor.execute(request), executorService)
                    .thenAcceptAsync(commandResultSender::send, executorService);
        }

        if (channels.get((SocketChannel) key.channel()) == null) {
            throw new IOException();
        }
    }

    private void kill(SocketChannel channel) throws IOException {
        channels.remove(channel);
        channel.close();
    }
}
