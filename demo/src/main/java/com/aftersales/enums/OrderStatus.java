package com.aftersales.enums;

/**
 * 售后单状态枚举
 */
public enum OrderStatus {
    PENDING("待审核", "售后单已创建，等待审核"),
    APPROVED("已通过", "售后单审核通过"),
    REJECTED("已拒绝", "售后单审核被拒绝"),
    PROCESSING("处理中", "售后单正在处理"),
    COMPLETED("已完成", "售后单处理完成"),
    CANCELLED("已取消", "售后单已取消");

    private final String displayName;
    private final String description;

    OrderStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}