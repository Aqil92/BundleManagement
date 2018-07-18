package com.sevenrocks.firebasedb.BeanClasses;

/**
 * Created by admin on 18/07/18.
 */

public class WorkerModel {

    private  String overlock;
    private String pi;
    private String rank;

    public String getOverlock() {
        return overlock;
    }

    public void setOverlock(String overlock) {
        this.overlock = overlock;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "WorkerModel{" +
                "overlock='" + overlock + '\'' +
                ", pi='" + pi + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}
