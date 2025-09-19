package com.aftersales.entity;

import com.aftersales.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 售后单实体对象
 */
public class AfterSalesOrder {
    
    /**
     * 售后单ID
     */
    private String id;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 购买门店
     */
    private String storeName;
    
    /**
     * 购买金额
     */
    private BigDecimal purchaseAmount;
    
    /**
     * 购买时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseTime;
    
    /**
     * 问题描述
     */
    private String problemDescription;
    
    /**
     * 联系人电话
     */
    private String contactPhone;
    
    /**
     * 售后单状态
     */
    private OrderStatus status;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 审核备注
     */
    private String auditRemark;
    
    /**
     * 审核人
     */
    private String auditor;
    
    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    // 构造函数
    public AfterSalesOrder() {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.status = OrderStatus.PENDING;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public AfterSalesOrder(String productName, String storeName, BigDecimal purchaseAmount, 
                          LocalDateTime purchaseTime, String problemDescription, String contactPhone) {
        this();
        this.productName = productName;
        this.storeName = storeName;
        this.purchaseAmount = purchaseAmount;
        this.purchaseTime = purchaseTime;
        this.problemDescription = problemDescription;
        this.contactPhone = contactPhone;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
        this.updateTime = LocalDateTime.now();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public LocalDateTime getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(LocalDateTime auditTime) {
        this.auditTime = auditTime;
    }

    @Override
    public String toString() {
        return "AfterSalesOrder{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", purchaseAmount=" + purchaseAmount +
                ", purchaseTime=" + purchaseTime +
                ", problemDescription='" + problemDescription + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", auditRemark='" + auditRemark + '\'' +
                ", auditor='" + auditor + '\'' +
                ", auditTime=" + auditTime +
                '}';
    }
}