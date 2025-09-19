package com.aftersales.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 售后单列表响应DTO
 */
public class AfterSalesOrderListResponse {
    
    /**
     * 售后单ID
     */
    private String id;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 购买时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseTime;
    
    /**
     * 问题描述
     */
    private String problemDescription;

    // 构造函数
    public AfterSalesOrderListResponse() {}

    public AfterSalesOrderListResponse(String id, String productName, LocalDateTime purchaseTime, String problemDescription) {
        this.id = id;
        this.productName = productName;
        this.purchaseTime = purchaseTime;
        this.problemDescription = problemDescription;
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(LocalDateTime purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    @Override
    public String toString() {
        return "AfterSalesOrderListResponse{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", purchaseTime=" + purchaseTime +
                ", problemDescription='" + problemDescription + '\'' +
                '}';
    }
}