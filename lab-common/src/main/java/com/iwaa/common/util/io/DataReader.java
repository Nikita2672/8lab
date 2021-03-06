package com.iwaa.common.util.io;

import com.iwaa.common.util.exceptions.EndOfStreamException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class DataReader extends BufferedReader implements AutoCloseable {

    public DataReader(Reader reader) {
        super(reader);
    }

    public String inputLine() throws EndOfStreamException, IOException {
        String s = super.readLine();
        if (s == null) {
            throw new EndOfStreamException();
        }
        return s;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

}
