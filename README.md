售后单管理系统 - 业务对象关系图
系统架构图
┌─────────────────────────────────────────────────────────────┐
│                    售后单管理系统                              │
├─────────────────────────────────────────────────────────────┤
│  Controller Layer (控制器层)                                  │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │           AfterSalesController                          │ │
│  │  - 创建售后单 POST /api/after-sales/orders              │ │
│  │  - 查询列表   GET  /api/after-sales/orders              │ │
│  │  - 获取详情   GET  /api/after-sales/orders/{id}         │ │
│  │  - 审核售后单 PUT  /api/after-sales/orders/{id}/audit   │ │
│  │  - 获取统计   GET  /api/after-sales/statistics          │ │
│  └─────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│  Service Layer (服务层)                                      │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │              AfterSalesService                          │ │
│  │  - createOrder()     创建售后单                         │ │
│  │  - queryOrders()     查询售后单列表                     │ │
│  │  - getOrderById()    根据ID获取详情                     │ │
│  │  - auditOrder()      审核售后单                         │ │
│  │  - canAudit()        检查是否可审核                     │ │
│  │  - isValidAuditStatus() 验证审核状态                    │ │
│  └─────────────────────────────────────────────────────────┘ │
├─────────────────────────────────────────────────────────────┤
│  Data Storage (数据存储层)                                   │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │         ConcurrentHashMap<String, AfterSalesOrder>      │ │
│  │                    (内存数组存储)                        │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
核心业务对象关系
1. 实体对象 (Entity)
AfterSalesOrder (售后单实体)
┌─────────────────────────────────────┐
│           AfterSalesOrder           │
├─────────────────────────────────────┤
│ - id: String                        │  主键ID (UUID)
│ - productName: String               │  商品名称
│ - storeName: String                 │  购买门店
│ - purchaseAmount: BigDecimal        │  购买金额
│ - purchaseTime: LocalDateTime       │  购买时间
│ - problemDescription: String        │  问题描述
│ - contactPhone: String              │  联系人电话
│ - status: OrderStatus               │  售后单状态
│ - createTime: LocalDateTime         │  创建时间
│ - updateTime: LocalDateTime         │  更新时间
│ - auditRemark: String               │  审核备注
│ - auditor: String                   │  审核人
│ - auditTime: LocalDateTime          │  审核时间
└─────────────────────────────────────┘
2. 枚举对象 (Enum)
OrderStatus (售后单状态枚举)
┌─────────────────────────────────────┐
│            OrderStatus              │
├─────────────────────────────────────┤
│ PENDING     - 待审核                │
│ APPROVED    - 已通过                │
│ REJECTED    - 已拒绝                │
│ PROCESSING  - 处理中                │
│ COMPLETED   - 已完成                │
│ CANCELLED   - 已取消                │
└─────────────────────────────────────┘
3. 数据传输对象 (DTO)
AfterSalesOrderCreateRequest (创建请求DTO)
┌─────────────────────────────────────┐
│   AfterSalesOrderCreateRequest      │
├─────────────────────────────────────┤
│ - productName: String               │  @NotBlank
│ - storeName: String                 │  @NotBlank
│ - purchaseAmount: BigDecimal        │  @NotNull @Positive
│ - purchaseTime: LocalDateTime       │  @NotNull
│ - problemDescription: String        │  @NotBlank
│ - contactPhone: String              │  @NotBlank @Pattern
└─────────────────────────────────────┘
AfterSalesOrderListResponse (列表响应DTO)
┌─────────────────────────────────────┐
│   AfterSalesOrderListResponse       │
├─────────────────────────────────────┤
│ - id: String                        │
│ - productName: String               │
│ - purchaseTime: LocalDateTime       │
│ - problemDescription: String        │
└─────────────────────────────────────┘
AuditRequest (审核请求DTO)
┌─────────────────────────────────────┐
│            AuditRequest             │
├─────────────────────────────────────┤
│ - status: OrderStatus               │  @NotNull
│ - auditor: String                   │  @NotBlank
│ - auditRemark: String               │  可选
└─────────────────────────────────────┘
业务流程关系图
售后单生命周期
创建售后单 → 待审核(PENDING) → 审核流程 → 最终状态
    │              │              │
    │              │              ├─ 已通过(APPROVED) → 处理中(PROCESSING) → 已完成(COMPLETED)
    │              │              │
    │              │              ├─ 已拒绝(REJECTED)
    │              │              │
    │              │              └─ 已取消(CANCELLED)
    │              │
    │              └─ 可重复审核（状态为PENDING或PROCESSING时）
    │
    └─ 自动生成ID、设置创建时间、初始状态为PENDING
审核流程控制
┌─────────────────────────────────────────────────────────────┐
│                      审核流程控制                              │
├─────────────────────────────────────────────────────────────┤
│  当前状态检查:                                                │
│  ├─ PENDING    ✓ 可以审核                                    │
│  ├─ PROCESSING ✓ 可以审核                                    │
│  ├─ APPROVED   ✗ 不可审核                                    │
│  ├─ REJECTED   ✗ 不可审核                                    │
│  ├─ COMPLETED  ✗ 不可审核                                    │
│  └─ CANCELLED  ✗ 不可审核                                    │
│                                                             │
│  目标状态验证:                                                │
│  ├─ APPROVED   ✓ 有效审核状态                                │
│  ├─ REJECTED   ✓ 有效审核状态                                │
│  ├─ PROCESSING ✓ 有效审核状态                                │
│  ├─ COMPLETED  ✓ 有效审核状态                                │
│  ├─ CANCELLED  ✓ 有效审核状态                                │
│  └─ PENDING    ✗ 无效审核状态                                │
└─────────────────────────────────────────────────────────────┘
API接口关系图
前端应用
    │
    ├─ POST   /api/after-sales/orders           → 创建售后单
    │     └─ AfterSalesOrderCreateRequest → AfterSalesOrder
    │
    ├─ GET    /api/after-sales/orders           → 查询售后单列表
    │     └─ 查询参数: startTime, endTime, productName
    │     └─ 返回: List<AfterSalesOrderListResponse>
    │
    ├─ GET    /api/after-sales/orders/{id}      → 获取售后单详情
    │     └─ 返回: AfterSalesOrder
    │
    ├─ PUT    /api/after-sales/orders/{id}/audit → 审核售后单
    │     └─ AuditRequest → AfterSalesOrder
    │
    ├─ GET    /api/after-sales/orders/all       → 获取所有售后单
    │     └─ 返回: List<AfterSalesOrder>
    │
    ├─ GET    /api/after-sales/statistics       → 获取统计信息
    │     └─ 返回: 各状态售后单数量统计
    │
    └─ DELETE /api/after-sales/orders/clear     → 清空所有数据(测试用)
数据存储关系
内存存储 (ConcurrentHashMap)
┌─────────────────────────────────────┐
│  Key: String (售后单ID)              │
│  Value: AfterSalesOrder             │
├─────────────────────────────────────┤
│  线程安全保证                        │
│  支持并发读写操作                    │
│  数据持久化在应用运行期间             │
│  重启后数据丢失                      │
└─────────────────────────────────────┘
系统特性
完整的实体设计: 包含售后单所需的所有字段
状态管理: 完善的状态枚举和状态转换控制
审核流程: 防止重复操作和异常操作的控制机制
数据验证: 使用Bean Validation进行参数校验
RESTful API: 标准的REST接口设计
线程安全: 使用ConcurrentHashMap保证并发安全
异常处理: 完善的异常处理和错误信息返回

售后单管理系统 API 测试文档
系统启动
mvn spring-boot:run
启动成功后，系统运行在: http://localhost:8080

API 接口测试
1. 创建售后单
接口: POST /api/after-sales/orders

测试用例1 - 正常创建:

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
测试用例2 - 另一个售后单:

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
2. 查询售后单列表
接口: GET /api/after-sales/orders

测试用例1 - 查询所有:

GET http://localhost:8080/api/after-sales/orders
测试用例2 - 按时间范围查询:

GET http://localhost:8080/api/after-sales/orders?startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59
测试用例3 - 按商品名查询:

GET http://localhost:8080/api/after-sales/orders?productName=iPhone
测试用例4 - 组合查询:

GET http://localhost:8080/api/after-sales/orders?startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59&productName=iPhone
3. 获取售后单详情
接口: GET /api/after-sales/orders/{id}

测试用例:

GET http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}
4. 审核售后单
接口: PUT /api/after-sales/orders/{id}/audit

测试用例1 - 审核通过:

PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "APPROVED",
    "auditor": "张三",
    "auditRemark": "售后申请符合条件，审核通过"
}
测试用例2 - 审核拒绝:

PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "REJECTED",
    "auditor": "李四",
    "auditRemark": "超出保修期，不符合售后条件"
}
测试用例3 - 设置为处理中:

PUT http://localhost:8080/api/after-sales/orders/{替换为实际的售后单ID}/audit
Content-Type: application/json

{
    "status": "PROCESSING",
    "auditor": "王五",
    "auditRemark": "开始处理售后问题"
}
5. 获取所有售后单（管理员接口）
接口: GET /api/after-sales/orders/all

测试用例:

GET http://localhost:8080/api/after-sales/orders/all
6. 获取统计信息
接口: GET /api/after-sales/statistics

测试用例:

GET http://localhost:8080/api/after-sales/statistics
7. 清空所有数据（测试接口）
接口: DELETE /api/after-sales/orders/clear

测试用例:

DELETE http://localhost:8080/api/after-sales/orders/clear
测试流程建议
完整测试流程:
启动系统: mvn spring-boot:run
创建测试数据: 使用创建接口添加几个售后单
测试查询功能:
查询所有售后单
按时间范围查询
按商品名查询
测试详情获取: 根据ID获取具体售后单详情
测试审核功能:
审核通过一个售后单
审核拒绝一个售后单
测试重复审核（应该失败）
测试统计功能: 查看各状态的售后单数量
测试异常情况:
查询不存在的ID
审核已完成的售后单
提交无效数据
使用 curl 命令测试示例:
创建售后单:

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
查询售后单列表:

curl -X GET "http://localhost:8080/api/after-sales/orders"
获取统计信息:

curl -X GET "http://localhost:8080/api/after-sales/statistics"
预期响应格式
所有接口都返回统一的响应格式:

{
    "success": true/false,
    "message": "操作结果描述",
    "data": "具体数据内容",
    "total": "数据总数（列表接口）"
}
状态码说明
200 OK: 操作成功
400 Bad Request: 请求参数错误或业务逻辑错误
404 Not Found: 资源不存在
500 Internal Server Error: 服务器内部错误
注意事项
所有时间格式使用: yyyy-MM-dd HH:mm:ss
联系人电话必须是有效的手机号格式
购买金额必须大于0
审核状态转换有严格的业务规则控制
系统使用内存存储，重启后数据会丢失
