package server.response;


import com.iwaa.common.util.network.CommandResult;
import com.iwaa.common.util.network.Serializer;
import server.channels.ChannelState;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

public class CommandResultSender {

    private final SocketChannel channel;
    private final Map<SocketChannel, ByteBuffer> channels;
    private final Selector selector;
    private final Map<SocketChannel, ChannelState> channelState;

    public CommandResultSender(SocketChannel channel, Map<SocketChannel, ByteBuffer> channels, Selector selector, Map<SocketChannel, ChannelState> channelState) {
        this.channel = channel;
        this.channels = channels;
        this.selector = selector;
        this.channelState = channelState;
    }

    public void send(CommandResult commandResult) {
        Serializer serializer = new Serializer();

        try {
            ByteBuffer buffer = ByteBuffer.wrap(serializer.serialize(commandResult));
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channels.put(channel, ByteBuffer.allocate(0));
            channelState.put(channel, ChannelState.READY_TO_READ);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            channels.put(channel, null);
        }
    }

}
