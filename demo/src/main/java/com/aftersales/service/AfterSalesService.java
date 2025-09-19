package com.aftersales.service;

import com.aftersales.dto.AfterSalesOrderCreateRequest;
import com.aftersales.dto.AfterSalesOrderListResponse;
import com.aftersales.dto.AuditRequest;
import com.aftersales.entity.AfterSalesOrder;
import com.aftersales.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 售后单服务层
 */
@Service
public class AfterSalesService {
    
    // 使用ConcurrentHashMap存储售后单数据，保证线程安全
    private final ConcurrentHashMap<String, AfterSalesOrder> orderStorage = new ConcurrentHashMap<>();

    /**
     * 创建售后单
     * @param request 创建请求
     * @return 售后单对象
     */
    public AfterSalesOrder createOrder(AfterSalesOrderCreateRequest request) {
        AfterSalesOrder order = new AfterSalesOrder(
                request.getProductName(),
                request.getStoreName(),
                request.getPurchaseAmount(),
                request.getPurchaseTime(),
                request.getProblemDescription(),
                request.getContactPhone()
        );
        
        orderStorage.put(order.getId(), order);
        return order;
    }

    /**
     * 根据时间和商品名查询售后单列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productName 商品名称（可选）
     * @return 售后单列表
     */
    public List<AfterSalesOrderListResponse> queryOrders(LocalDateTime startTime, LocalDateTime endTime, String productName) {
        return orderStorage.values().stream()
                .filter(order -> {
                    // 时间范围过滤
                    boolean timeMatch = true;
                    if (startTime != null) {
                        timeMatch = order.getPurchaseTime().isAfter(startTime) || order.getPurchaseTime().isEqual(startTime);
                    }
                    if (endTime != null && timeMatch) {
                        timeMatch = order.getPurchaseTime().isBefore(endTime) || order.getPurchaseTime().isEqual(endTime);
                    }
                    
                    // 商品名称过滤
                    boolean productMatch = true;
                    if (productName != null && !productName.trim().isEmpty()) {
                        productMatch = order.getProductName().contains(productName.trim());
                    }
                    
                    return timeMatch && productMatch;
                })
                .map(order -> new AfterSalesOrderListResponse(
                        order.getId(),
                        order.getProductName(),
                        order.getPurchaseTime(),
                        order.getProblemDescription()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取售后单详情
     * @param id 售后单ID
     * @return 售后单详情
     */
    public AfterSalesOrder getOrderById(String id) {
        AfterSalesOrder order = orderStorage.get(id);
        if (order == null) {
            throw new RuntimeException("售后单不存在，ID: " + id);
        }
        return order;
    }

    /**
     * 审核售后单
     * @param id 售后单ID
     * @param auditRequest 审核请求
     * @return 更新后的售后单
     */
    public AfterSalesOrder auditOrder(String id, AuditRequest auditRequest) {
        AfterSalesOrder order = getOrderById(id);
        
        // 检查当前状态是否允许审核
        if (!canAudit(order.getStatus())) {
            throw new RuntimeException("当前状态不允许审核，当前状态: " + order.getStatus().getDisplayName());
        }
        
        // 检查目标状态是否有效
        if (!isValidAuditStatus(auditRequest.getStatus())) {
            throw new RuntimeException("无效的审核状态: " + auditRequest.getStatus().getDisplayName());
        }
        
        // 更新审核信息
        order.setStatus(auditRequest.getStatus());
        order.setAuditor(auditRequest.getAuditor());
        order.setAuditRemark(auditRequest.getAuditRemark());
        order.setAuditTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        
        return order;
    }

    /**
     * 检查当前状态是否允许审核
     * @param currentStatus 当前状态
     * @return 是否允许审核
     */
    private boolean canAudit(OrderStatus currentStatus) {
        return currentStatus == OrderStatus.PENDING || currentStatus == OrderStatus.PROCESSING;
    }

    /**
     * 检查审核状态是否有效
     * @param auditStatus 审核状态
     * @return 是否有效
     */
    private boolean isValidAuditStatus(OrderStatus auditStatus) {
        return auditStatus == OrderStatus.APPROVED || 
               auditStatus == OrderStatus.REJECTED || 
               auditStatus == OrderStatus.PROCESSING ||
               auditStatus == OrderStatus.COMPLETED ||
               auditStatus == OrderStatus.CANCELLED;
    }

    /**
     * 获取所有售后单
     * @return 所有售后单列表
     */
    public List<AfterSalesOrder> getAllOrders() {
        return new ArrayList<>(orderStorage.values());
    }

    /**
     * 根据状态查询售后单
     * @param status 状态
     * @return 售后单列表
     */
    public List<AfterSalesOrder> getOrdersByStatus(OrderStatus status) {
        return orderStorage.values().stream()
                .filter(order -> order.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * 获取售后单总数
     * @return 总数
     */
    public int getOrderCount() {
        return orderStorage.size();
    }

    /**
     * 清空所有售后单（仅用于测试）
     */
    public void clearAllOrders() {
        orderStorage.clear();
    }
}