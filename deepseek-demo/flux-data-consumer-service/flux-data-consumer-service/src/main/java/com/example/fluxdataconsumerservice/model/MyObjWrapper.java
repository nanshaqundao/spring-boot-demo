package com.example.fluxdataconsumerservice.model;

import java.util.List;

public class MyObjWrapper {
    private int batchSize;
    private List<MyObj> data;

    // 构造函数
    public MyObjWrapper(List<MyObj> data) {
        this.data = data;
        this.batchSize = data.size();
    }

    // getter 和 setter
    public int getBatchSize() {
        return batchSize;
    }

    public List<MyObj> getData() {
        return data;
    }
}
