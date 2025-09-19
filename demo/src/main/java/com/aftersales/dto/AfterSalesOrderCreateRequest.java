package com.aftersales.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后单创建请求DTO
 */
public class AfterSalesOrderCreateRequest {
    
    @NotBlank(message = "商品名称不能为空")
    private String productName;
    
    @NotBlank(message = "购买门店不能为空")
    private String storeName;
    
    @NotNull(message = "购买金额不能为空")
    @Positive(message = "购买金额必须大于0")
    private BigDecimal purchaseAmount;
    
    @NotNull(message = "购买时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseTime;
    
    @NotBlank(message = "问题描述不能为空")
    private String problemDescription;
    
    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系人电话格式不正确")
    private String contactPhone;

    // 构造函数
    public AfterSalesOrderCreateRequest() {}

    public AfterSalesOrderCreateRequest(String productName, String storeName, BigDecimal purchaseAmount, 
                                       LocalDateTime purchaseTime, String problemDescription, String contactPhone) {
        this.productName = productName;
        this.storeName = storeName;
        this.purchaseAmount = purchaseAmount;
        this.purchaseTime = purchaseTime;
        this.problemDescription = problemDescription;
        this.contactPhone = contactPhone;
    }

    // Getter和Setter方法
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Override
    public String toString() {
        return "AfterSalesOrderCreateRequest{" +
                "productName='" + productName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", purchaseAmount=" + purchaseAmount +
                ", purchaseTime=" + purchaseTime +
                ", problemDescription='" + problemDescription + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                '}';
    }
}