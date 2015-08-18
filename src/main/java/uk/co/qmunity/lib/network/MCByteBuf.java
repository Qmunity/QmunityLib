package uk.co.qmunity.lib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.common.network.ByteBufUtils;

/**
 * QMunityLib's readable version of Minecraft's {@link PacketBuffer}. Includes (properly named) helper methods for writing and reading Strings,
 * NBTTagCompounds and ItemStacks.
 */
public class MCByteBuf extends ByteBuf {

    private ByteBuf buf;

    public MCByteBuf(ByteBuf buf) {

        this.buf = buf;
    }

    @Override
    public int refCnt() {

        return buf.refCnt();
    }

    @Override
    public boolean release() {

        return buf.release();
    }

    @Override
    public boolean release(int decrement) {

        return buf.release(decrement);
    }

    @Override
    public int capacity() {

        return buf.capacity();
    }

    @Override
    public MCByteBuf capacity(int newCapacity) {

        buf.capacity(newCapacity);
        return this;
    }

    @Override
    public int maxCapacity() {

        return buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {

        return buf.alloc();
    }

    @Override
    public ByteOrder order() {

        return buf.order();
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {

        return buf.order(endianness);
    }

    @Override
    public ByteBuf unwrap() {

        return buf;
    }

    @Override
    public boolean isDirect() {

        return buf.isDirect();
    }

    @Override
    public int readerIndex() {

        return buf.readerIndex();
    }

    @Override
    public MCByteBuf readerIndex(int readerIndex) {

        buf.readerIndex(readerIndex);
        return this;
    }

    @Override
    public int writerIndex() {

        return buf.writerIndex();
    }

    @Override
    public MCByteBuf writerIndex(int writerIndex) {

        buf.writerIndex(writerIndex);
        return this;
    }

    @Override
    public MCByteBuf setIndex(int readerIndex, int writerIndex) {

        buf.setIndex(readerIndex, writerIndex);
        return this;
    }

    @Override
    public int readableBytes() {

        return buf.readableBytes();
    }

    @Override
    public int writableBytes() {

        return buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {

        return buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {

        return buf.isReadable();
    }

    @Override
    public boolean isReadable(int size) {

        return buf.isReadable(size);
    }

    @Override
    public boolean isWritable() {

        return buf.isWritable();
    }

    @Override
    public boolean isWritable(int size) {

        return buf.isWritable(size);
    }

    @Override
    public MCByteBuf clear() {

        buf.clear();
        return this;
    }

    @Override
    public MCByteBuf markReaderIndex() {

        buf.markReaderIndex();
        return this;
    }

    @Override
    public MCByteBuf resetReaderIndex() {

        buf.resetReaderIndex();
        return this;
    }

    @Override
    public MCByteBuf markWriterIndex() {

        buf.markWriterIndex();
        return this;
    }

    @Override
    public MCByteBuf resetWriterIndex() {

        buf.resetWriterIndex();
        return this;
    }

    @Override
    public MCByteBuf discardReadBytes() {

        buf.discardReadBytes();
        return this;
    }

    @Override
    public MCByteBuf discardSomeReadBytes() {

        buf.discardSomeReadBytes();
        return this;
    }

    @Override
    public MCByteBuf ensureWritable(int minWritableBytes) {

        buf.ensureWritable(minWritableBytes);
        return this;
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {

        return buf.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {

        return buf.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {

        return buf.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {

        return buf.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {

        return buf.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {

        return buf.getUnsignedShort(index);
    }

    @Override
    public int getMedium(int index) {

        return buf.getMedium(index);
    }

    @Override
    public int getUnsignedMedium(int index) {

        return buf.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {

        return buf.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {

        return buf.getUnsignedInt(index);
    }

    @Override
    public long getLong(int index) {

        return buf.getLong(index);
    }

    @Override
    public char getChar(int index) {

        return buf.getChar(index);
    }

    @Override
    public float getFloat(int index) {

        return buf.getFloat(index);
    }

    @Override
    public double getDouble(int index) {

        return buf.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {

        return buf.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {

        return buf.getBytes(index, dst, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {

        return buf.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {

        return buf.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {

        return buf.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {

        return buf.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {

        return buf.getBytes(index, out, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {

        return buf.getBytes(index, dst);
    }

    @Override
    public MCByteBuf setBoolean(int index, boolean value) {

        buf.setBoolean(index, value);
        return this;
    }

    @Override
    public MCByteBuf setByte(int index, int value) {

        buf.setByte(index, value);
        return this;
    }

    @Override
    public MCByteBuf setShort(int index, int value) {

        buf.setShort(index, value);
        return this;
    }

    @Override
    public MCByteBuf setMedium(int index, int value) {

        buf.setMedium(index, value);
        return this;
    }

    @Override
    public MCByteBuf setInt(int index, int value) {

        buf.setInt(index, value);
        return this;
    }

    @Override
    public MCByteBuf setLong(int index, long value) {

        buf.setLong(index, value);
        return this;
    }

    @Override
    public MCByteBuf setChar(int index, int value) {

        buf.setChar(index, value);
        return this;
    }

    @Override
    public MCByteBuf setFloat(int index, float value) {

        buf.setFloat(index, value);
        return this;
    }

    @Override
    public MCByteBuf setDouble(int index, double value) {

        buf.setDouble(index, value);
        return this;
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {

        return buf.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {

        return buf.setBytes(index, src, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {

        return buf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {

        return buf.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {

        return buf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {

        return buf.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {

        return buf.setBytes(index, in, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {

        return buf.setBytes(index, src);
    }

    @Override
    public MCByteBuf setZero(int index, int length) {

        buf.setZero(index, length);
        return this;
    }

    @Override
    public boolean readBoolean() {

        return buf.readBoolean();
    }

    @Override
    public byte readByte() {

        return buf.readByte();
    }

    @Override
    public short readUnsignedByte() {

        return buf.readUnsignedByte();
    }

    @Override
    public short readShort() {

        return buf.readShort();
    }

    @Override
    public int readUnsignedShort() {

        return buf.readUnsignedShort();
    }

    @Override
    public int readMedium() {

        return buf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {

        return buf.readUnsignedMedium();
    }

    @Override
    public int readInt() {

        return buf.readInt();
    }

    @Override
    public long readUnsignedInt() {

        return buf.readUnsignedInt();
    }

    @Override
    public long readLong() {

        return buf.readLong();
    }

    @Override
    public char readChar() {

        return buf.readChar();
    }

    @Override
    public float readFloat() {

        return buf.readFloat();
    }

    @Override
    public double readDouble() {

        return buf.readDouble();
    }

    public String readString() {

        return ByteBufUtils.readUTF8String(buf);
    }

    public NBTTagCompound readNBTTag() {

        return ByteBufUtils.readTag(buf);
    }

    public ItemStack readItemStack() {

        return ByteBufUtils.readItemStack(buf);
    }

    @Override
    public ByteBuf readBytes(int length) {

        return buf.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {

        return buf.readSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {

        return buf.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {

        return buf.readBytes(dst, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {

        return buf.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {

        return buf.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {

        return buf.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {

        return buf.readBytes(out, length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {

        return buf.readBytes(out, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {

        return buf.readBytes(dst);
    }

    @Override
    public MCByteBuf skipBytes(int length) {

        buf.skipBytes(length);
        return this;
    }

    @Override
    public MCByteBuf writeBoolean(boolean value) {

        buf.writeBoolean(value);
        return this;
    }

    @Override
    public MCByteBuf writeByte(int value) {

        buf.writeByte(value);
        return this;
    }

    @Override
    public MCByteBuf writeShort(int value) {

        buf.writeShort(value);
        return this;
    }

    @Override
    public MCByteBuf writeMedium(int value) {

        buf.writeMedium(value);
        return this;
    }

    @Override
    public MCByteBuf writeInt(int value) {

        buf.writeInt(value);
        return this;
    }

    @Override
    public MCByteBuf writeLong(long value) {

        buf.writeLong(value);
        return this;
    }

    @Override
    public MCByteBuf writeChar(int value) {

        buf.writeChar(value);
        return this;
    }

    @Override
    public MCByteBuf writeFloat(float value) {

        buf.writeFloat(value);
        return this;
    }

    @Override
    public MCByteBuf writeDouble(double value) {

        buf.writeDouble(value);
        return this;
    }

    public MCByteBuf writeString(String value) {

        ByteBufUtils.writeUTF8String(buf, value);
        return this;
    }

    public MCByteBuf writeNBTTag(NBTTagCompound value) {

        ByteBufUtils.writeTag(buf, value);
        return this;
    }

    public MCByteBuf writeItemStack(ItemStack value) {

        ByteBufUtils.writeItemStack(buf, value);
        return this;
    }

    @Override
    public MCByteBuf writeBytes(ByteBuf src) {

        buf.writeBytes(src);
        return this;
    }

    @Override
    public MCByteBuf writeBytes(ByteBuf src, int length) {

        buf.writeBytes(src, length);
        return this;
    }

    @Override
    public MCByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {

        buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public MCByteBuf writeBytes(byte[] src) {

        buf.writeBytes(src);
        return this;
    }

    @Override
    public MCByteBuf writeBytes(byte[] src, int srcIndex, int length) {

        buf.writeBytes(src, srcIndex, length);
        return this;
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {

        return buf.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {

        return buf.writeBytes(in, length);
    }

    @Override
    public MCByteBuf writeBytes(ByteBuffer src) {

        buf.writeBytes(src);
        return this;
    }

    @Override
    public MCByteBuf writeZero(int length) {

        buf.writeZero(length);
        return this;
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {

        return buf.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {

        return buf.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {

        return buf.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {

        return buf.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteBufProcessor processor) {

        return buf.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteBufProcessor processor) {

        return buf.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor processor) {

        return buf.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {

        return buf.forEachByteDesc(processor);
    }

    @Override
    public MCByteBuf copy() {

        return new MCByteBuf(buf.copy());
    }

    @Override
    public MCByteBuf copy(int index, int length) {

        return new MCByteBuf(buf.copy(index, length));
    }

    @Override
    public MCByteBuf slice() {

        return new MCByteBuf(buf.slice());
    }

    @Override
    public MCByteBuf slice(int index, int length) {

        return new MCByteBuf(buf.slice(index, length));
    }

    @Override
    public MCByteBuf duplicate() {

        return new MCByteBuf(buf.duplicate());
    }

    @Override
    public int nioBufferCount() {

        return buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {

        return buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {

        return buf.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {

        return buf.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {

        return buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {

        return buf.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {

        return buf.hasArray();
    }

    @Override
    public byte[] array() {

        return buf.array();
    }

    @Override
    public int arrayOffset() {

        return buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {

        return buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {

        return buf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {

        return buf.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {

        return buf.toString(index, length, charset);
    }

    @Override
    public int hashCode() {

        return buf.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        return buf.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {

        return buf.compareTo(buffer);
    }

    @Override
    public String toString() {

        return buf.toString();
    }

    @Override
    public MCByteBuf retain(int increment) {

        buf.retain(increment);
        return this;
    }

    @Override
    public MCByteBuf retain() {

        buf.retain();
        return this;
    }

}
