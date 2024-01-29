package com.qfsp.QFSP;

public class PageData {
    int page = 1;
    int size = 20;

    public PageData() {
    }

    public PageData(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
