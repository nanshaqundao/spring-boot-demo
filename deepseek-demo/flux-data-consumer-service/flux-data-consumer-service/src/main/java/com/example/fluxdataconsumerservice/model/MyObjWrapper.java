package com.example.fluxdataconsumerservice.model;

import java.util.List;

public class MyObjWrapper {
    private int batchId;        // 批次编号
    private int batchSize;      // 当前批次大小
    private long totalSize;     // 总记录数
    private int batchCount;     // 总批次数
    private List<MyObj> data;   // 数据

    public MyObjWrapper(int batchId, List<MyObj> data, long totalSize, int batchCount) {
        this.batchId = batchId;
        this.data = data;
        this.batchSize = data.size();
        this.totalSize = totalSize;
        this.batchCount = batchCount;
    }

    public int getBatchId() {
        return batchId;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public List<MyObj> getData() {
        return data;
    }
}