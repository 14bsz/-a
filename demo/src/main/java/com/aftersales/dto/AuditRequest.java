package com.aftersales.dto;

import com.aftersales.enums.OrderStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 审核请求DTO
 */
public class AuditRequest {
    
    @NotNull(message = "审核状态不能为空")
    private OrderStatus status;
    
    @NotBlank(message = "审核人不能为空")
    private String auditor;
    
    private String auditRemark;

    // 构造函数
    public AuditRequest() {}

    public AuditRequest(OrderStatus status, String auditor, String auditRemark) {
        this.status = status;
        this.auditor = auditor;
        this.auditRemark = auditRemark;
    }

    // Getter和Setter方法
    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    @Override
    public String toString() {
        return "AuditRequest{" +
                "status=" + status +
                ", auditor='" + auditor + '\'' +
                ", auditRemark='" + auditRemark + '\'' +
                '}';
    }
}