package uk.co.qmunity.lib.part.compat.fmp;

import java.io.DataOutput;
import java.io.IOException;

import codechicken.lib.data.MCDataOutput;

public class FMPDataOutput implements DataOutput {

    private MCDataOutput output;

    public FMPDataOutput(MCDataOutput output) {

        this.output = output;
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {

        output.writeByteArray(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {

        write(b);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {

        output.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {

        output.writeByte(v);
    }

    @Override
    public void writeBytes(String s) throws IOException {

        output.writeString(s);
    }

    @Override
    public void writeChar(int v) throws IOException {

        output.writeChar((char) v);
    }

    @Override
    public void writeChars(String s) throws IOException {

        output.writeString(s);
    }

    @Override
    public void writeDouble(double v) throws IOException {

        output.writeDouble(v);
    }

    @Override
    public void writeFloat(float v) throws IOException {

        output.writeFloat(v);
    }

    @Override
    public void writeInt(int v) throws IOException {

        output.writeInt(v);
    }

    @Override
    public void writeLong(long v) throws IOException {

        output.writeLong(v);
    }

    @Override
    public void writeShort(int v) throws IOException {

        output.writeShort(v);
    }

    @Override
    public void writeUTF(String s) throws IOException {

        output.writeString(s);
    }

}
