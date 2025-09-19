package com.aftersales.controller;

import com.aftersales.dto.AfterSalesOrderCreateRequest;
import com.aftersales.dto.AfterSalesOrderListResponse;
import com.aftersales.dto.AuditRequest;
import com.aftersales.entity.AfterSalesOrder;
import com.aftersales.service.AfterSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后单控制器
 */
@RestController
@RequestMapping("/api/after-sales")
@CrossOrigin(origins = "*")
public class AfterSalesController {

    @Autowired
    private AfterSalesService afterSalesService;

    /**
     * 创建售后单
     * @param request 创建请求
     * @return 创建结果
     */
    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody AfterSalesOrderCreateRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            AfterSalesOrder order = afterSalesService.createOrder(request);
            response.put("success", true);
            response.put("message", "售后单创建成功");
            response.put("data", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "售后单创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 查询售后单列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param productName 商品名称
     * @return 售后单列表
     */
    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> queryOrders(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) String productName) {
        
        Map<String, Object> response = new HashMap<>();
        try {
            List<AfterSalesOrderListResponse> orders = afterSalesService.queryOrders(startTime, endTime, productName);
            response.put("success", true);
            response.put("message", "查询成功");
            response.put("data", orders);
            response.put("total", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "查询失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据ID获取售后单详情
     * @param id 售后单ID
     * @return 售后单详情
     */
    @GetMapping("/orders/{id}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            AfterSalesOrder order = afterSalesService.getOrderById(id);
            response.put("success", true);
            response.put("message", "获取详情成功");
            response.put("data", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取详情失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 审核售后单
     * @param id 售后单ID
     * @param auditRequest 审核请求
     * @return 审核结果
     */
    @PutMapping("/orders/{id}/audit")
    public ResponseEntity<Map<String, Object>> auditOrder(@PathVariable String id, 
                                                         @Valid @RequestBody AuditRequest auditRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            AfterSalesOrder order = afterSalesService.auditOrder(id, auditRequest);
            response.put("success", true);
            response.put("message", "审核成功");
            response.put("data", order);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "审核失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取所有售后单（管理员接口）
     * @return 所有售后单
     */
    @GetMapping("/orders/all")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<AfterSalesOrder> orders = afterSalesService.getAllOrders();
            response.put("success", true);
            response.put("message", "获取所有售后单成功");
            response.put("data", orders);
            response.put("total", orders.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取系统统计信息
     * @return 统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalOrders", afterSalesService.getOrderCount());
            statistics.put("pendingOrders", afterSalesService.getOrdersByStatus(com.aftersales.enums.OrderStatus.PENDING).size());
            statistics.put("approvedOrders", afterSalesService.getOrdersByStatus(com.aftersales.enums.OrderStatus.APPROVED).size());
            statistics.put("rejectedOrders", afterSalesService.getOrdersByStatus(com.aftersales.enums.OrderStatus.REJECTED).size());
            statistics.put("completedOrders", afterSalesService.getOrdersByStatus(com.aftersales.enums.OrderStatus.COMPLETED).size());
            
            response.put("success", true);
            response.put("message", "获取统计信息成功");
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取统计信息失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 清空所有售后单（仅用于测试）
     * @return 清空结果
     */
    @DeleteMapping("/orders/clear")
    public ResponseEntity<Map<String, Object>> clearAllOrders() {
        Map<String, Object> response = new HashMap<>();
        try {
            afterSalesService.clearAllOrders();
            response.put("success", true);
            response.put("message", "清空所有售后单成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清空失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}