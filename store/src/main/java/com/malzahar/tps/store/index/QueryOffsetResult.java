package com.malzahar.tps.store.index;

import java.util.List;

public class QueryOffsetResult {
    private final List<Long> phyOffsets;
    private final long indexLastUpdateTimestamp;
    private final long indexLastUpdatePhyoffset;

    public QueryOffsetResult(List<Long> phyOffsets, long indexLastUpdateTimestamp,
        long indexLastUpdatePhyoffset) {
        this.phyOffsets = phyOffsets;
        this.indexLastUpdateTimestamp = indexLastUpdateTimestamp;
        this.indexLastUpdatePhyoffset = indexLastUpdatePhyoffset;
    }

    public List<Long> getPhyOffsets() {
        return phyOffsets;
    }

    public long getIndexLastUpdateTimestamp() {
        return indexLastUpdateTimestamp;
    }

    public long getIndexLastUpdatePhyoffset() {
        return indexLastUpdatePhyoffset;
    }
}
