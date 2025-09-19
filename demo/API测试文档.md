# 售后单管理系统 API 测试文档

## 系统启动
```bash
mvn spring-boot:run
```
启动成功后，系统运行在: http://localhost:8080

## API 接口测试

### 1. 创建售后单
**接口**: `POST /api/after-sales/orders`

**测试用例1 - 正常创建**:
```json
POST http://localhost:8080/api/after-sales/orders
Content-Type: application/json

{
    "productName": "iPhone 14 Pro",
    "storeName": "苹果专卖店(北京店)",
    "purchaseAmount": 8999.00,
    "purchaseTime": "2024-01-15 14:30:00",
    "problemDescription": "手机屏幕出现黑屏问题，无法正常使用",
    "contactPhone": "13812345678"
}
```

**测试用例2 - 另一个售后单**:
```json
POST http://localhost:8080/api/after-sales/orders
Content-Type: application/json

{
    "productName": "MacBook Pro",
    "storeName": "苹果专卖店(上海店)",
    "purchaseAmount": 15999.00,
    "purchaseTime": "2024-02-20 10:15:00",
    "problemDescription": "键盘按键失灵，部分按键无响应",
    "contactPhone": "13987654321"
}
```

### 2. 查询售后单列表
**接口**: `GET /api/after-sales/orders`

**测试用例1 - 查询所有**:
```
GET http://localhost:8080/api/after-sales/orders
```

**测试用例2 - 按时间范围查询**:
```
GET http://localhost:8080/api/after-sales/orders?startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59
```

**测试用例3 - 按商品名查询**:
```
GET http://localhost:8080/api/after-sales/orders?productName=iPhone
```

**测试用例4 - 组合查询**:
```
GET http://localhost:8080/api/after-sales/orders?startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59&productName=iPhone
```

### 3. 获取售后单详情
**接口**: `GET /api/after-sales/orders/{id}`

**测试用例**:
```
GET http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}
```

### 4. 审核售后单
**接口**: `PUT /api/after-sales/orders/{id}/audit`

**测试用例1 - 审核通过**:
```json
PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "APPROVED",
    "auditor": "张三",
    "auditRemark": "售后申请符合条件，审核通过"
}
```

**测试用例2 - 审核拒绝**:
```json
PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "REJECTED",
    "auditor": "李四",
    "auditRemark": "超出保修期，不符合售后条件"
}
```

**测试用例3 - 设置为处理中**:
```json
PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "PROCESSING",
    "auditor": "王五",
    "auditRemark": "开始处理售后问题"
}
```

### 5. 获取所有售后单（管理员接口）
**接口**: `GET /api/after-sales/orders/all`

**测试用例**:
```
GET http://localhost:8080/api/after-sales/orders/all
```

### 6. 获取统计信息
**接口**: `GET /api/after-sales/statistics`

**测试用例**:
```
GET http://localhost:8080/api/after-sales/statistics
```

### 7. 清空所有数据（测试接口）
**接口**: `DELETE /api/after-sales/orders/clear`

**测试用例**:
```
DELETE http://localhost:8080/api/after-sales/orders/clear
```

## 测试流程建议

### 完整测试流程:
1. **启动系统**: `mvn spring-boot:run`
2. **创建测试数据**: 使用创建接口添加几个售后单
3. **测试查询功能**: 
   - 查询所有售后单
   - 按时间范围查询
   - 按商品名查询
4. **测试详情获取**: 根据ID获取具体售后单详情
5. **测试审核功能**: 
   - 审核通过一个售后单
   - 审核拒绝一个售后单
   - 测试重复审核（应该失败）
6. **测试统计功能**: 查看各状态的售后单数量
7. **测试异常情况**:
   - 查询不存在的ID
   - 审核已完成的售后单
   - 提交无效数据

### 使用 curl 命令测试示例:

**创建售后单**:
```bash
curl -X POST http://localhost:8080/api/after-sales/orders \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "iPhone 14 Pro",
    "storeName": "苹果专卖店(北京店)",
    "purchaseAmount": 8999.00,
    "purchaseTime": "2024-01-15 14:30:00",
    "problemDescription": "手机屏幕出现黑屏问题，无法正常使用",
    "contactPhone": "13812345678"
  }'
```

**查询售后单列表**:
```bash
curl -X GET "http://localhost:8080/api/after-sales/orders"
```

**获取统计信息**:
```bash
curl -X GET "http://localhost:8080/api/after-sales/statistics"
```

## 预期响应格式

所有接口都返回统一的响应格式:
```json
{
    "success": true/false,
    "message": "操作结果描述",
    "data": "具体数据内容",
    "total": "数据总数（列表接口）"
}
```

## 状态码说明
- `200 OK`: 操作成功
- `400 Bad Request`: 请求参数错误或业务逻辑错误
- `404 Not Found`: 资源不存在
- `500 Internal Server Error`: 服务器内部错误

## 注意事项
1. 所有时间格式使用: `yyyy-MM-dd HH:mm:ss`
2. 联系人电话必须是有效的手机号格式
3. 购买金额必须大于0
4. 审核状态转换有严格的业务规则控制
5. 系统使用内存存储，重启后数据会丢失