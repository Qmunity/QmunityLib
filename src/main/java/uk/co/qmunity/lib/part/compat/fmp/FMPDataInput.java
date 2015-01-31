package uk.co.qmunity.lib.part.compat.fmp;

import java.io.DataInput;
import java.io.IOException;

import codechicken.lib.data.MCDataInput;

public class FMPDataInput implements DataInput {

    private MCDataInput input;

    public FMPDataInput(MCDataInput input) {

        this.input = input;
    }

    @Override
    public boolean readBoolean() throws IOException {

        return input.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {

        return input.readByte();
    }

    @Override
    public char readChar() throws IOException {

        return input.readChar();
    }

    @Override
    public double readDouble() throws IOException {

        return input.readDouble();
    }

    @Override
    public float readFloat() throws IOException {

        return input.readFloat();
    }

    @Override
    public void readFully(byte[] b) throws IOException {

        byte[] bytes = input.readByteArray(b.length);
        for (int i = 0; i < b.length; i++)
            b[i] = bytes[i];
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {

        skipBytes(off);

        byte[] bytes = input.readByteArray(len);
        for (int i = 0; i < len; i++)
            b[i] = bytes[i];
    }

    @Override
    public int readInt() throws IOException {

        return input.readInt();
    }

    @Override
    public String readLine() throws IOException {

        return input.readString();
    }

    @Override
    public long readLong() throws IOException {

        return input.readLong();
    }

    @Override
    public short readShort() throws IOException {

        return input.readShort();
    }

    @Override
    public String readUTF() throws IOException {

        return input.readString();
    }

    @Override
    public int readUnsignedByte() throws IOException {

        return input.readUByte();
    }

    @Override
    public int readUnsignedShort() throws IOException {

        return input.readUShort();
    }

    @Override
    public int skipBytes(int n) throws IOException {

        int i = 0;

        try {
            for (; i < n; i++)
                input.readByte();
        } catch (Exception ex) {
        }

        return i;
    }

}
