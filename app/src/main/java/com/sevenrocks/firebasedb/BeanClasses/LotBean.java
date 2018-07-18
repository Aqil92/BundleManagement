package com.sevenrocks.firebasedb.BeanClasses;

/**
 * Created by admin on 17/07/18.
 */

public class LotBean {

    private  String lotCount;
    private  String scanStatus;
    private  String styleCode;
    private  String lotno;
    private  String bundleNo;


    public String getLotCount() {
        return lotCount;
    }

    public void setLotCount(String lotCount) {
        this.lotCount = lotCount;
    }

    public String getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus) {
        this.scanStatus = scanStatus;
    }

    public String getStyleCode() {
        return styleCode;
    }

    public void setStyleCode(String styleCode) {
        this.styleCode = styleCode;
    }

    public String getLotno() {
        return lotno;
    }

    public void setLotno(String lotno) {
        this.lotno = lotno;
    }

    public String getBundleNo() {
        return bundleNo;
    }

    public void setBundleNo(String bundleNo) {
        this.bundleNo = bundleNo;
    }
}
