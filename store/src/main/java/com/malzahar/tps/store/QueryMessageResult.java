package com.malzahar.tps.store;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class QueryMessageResult {

    private final List<SelectMappedBufferResult> messageMapedList =
        new ArrayList<SelectMappedBufferResult>(100);

    private final List<ByteBuffer> messageBufferList = new ArrayList<ByteBuffer>(100);
    private long indexLastUpdateTimestamp;
    private long indexLastUpdatePhyoffset;

    private int bufferTotalSize = 0;

    public void addMessage(final SelectMappedBufferResult mapedBuffer) {
        this.messageMapedList.add(mapedBuffer);
        this.messageBufferList.add(mapedBuffer.getByteBuffer());
        this.bufferTotalSize += mapedBuffer.getSize();
    }

    public void release() {
        for (SelectMappedBufferResult select : this.messageMapedList) {
            select.release();
        }
    }

    public long getIndexLastUpdateTimestamp() {
        return indexLastUpdateTimestamp;
    }

    public void setIndexLastUpdateTimestamp(long indexLastUpdateTimestamp) {
        this.indexLastUpdateTimestamp = indexLastUpdateTimestamp;
    }

    public long getIndexLastUpdatePhyoffset() {
        return indexLastUpdatePhyoffset;
    }

    public void setIndexLastUpdatePhyoffset(long indexLastUpdatePhyoffset) {
        this.indexLastUpdatePhyoffset = indexLastUpdatePhyoffset;
    }

    public List<ByteBuffer> getMessageBufferList() {
        return messageBufferList;
    }

    public int getBufferTotalSize() {
        return bufferTotalSize;
    }
}
