# Freight Contract Management Backend

基于Spring Boot和GraphQL的货运合同管理系统后端服务。

## 功能特性

- **合同管理**: 完整的合同生命周期管理
- **应收应付**: 应收应付账款的详细跟踪
- **用户管理**: 用户权限和角色管理
- **GraphQL API**: 现代化的API接口
- **多数据库支持**: 支持PostgreSQL和H2数据库
- **国际化支持**: 支持多语言环境

## 技术栈

- **框架**: Spring Boot 3.2.0
- **数据库**: Spring Data JPA + PostgreSQL/H2
- **API**: GraphQL (Spring GraphQL)
- **安全**: Spring Security
- **构建**: Maven
- **测试**: JUnit 5

## 快速开始

### 1. 环境要求

- Java 17 或更高版本
- Maven 3.6+
- PostgreSQL 12+ (可选，默认使用H2)

### 2. 运行项目

#### 使用开发环境 (H2数据库)

```bash
# 克隆项目
git clone <repository-url>
cd freight-contract-system/freight-contract-backend

# 使用开发配置运行
mvn spring-boot:run -Dspring.profiles.active=dev
```

#### 使用PostgreSQL

1. 创建数据库：
```sql
CREATE DATABASE freight_contract_db;
```

2. 更新 `application.yml` 中的数据库配置：
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/freight_contract_db
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

3. 运行项目：
```bash
mvn spring-boot:run
```

### 3. 访问服务

- **GraphQL Playground**: http://localhost:8080/graphiql
- **H2 Console**: http://localhost:8080/h2-console (仅限开发环境)
- **健康检查**: http://localhost:8080/health

## GraphQL API 使用示例

### 查询合同列表

```graphql
query {
  contracts {
    id
    businessNo
    customerName
    amount
    currency
    status
    receivables {
      id
      amount
      status
    }
    payables {
      id
      amount
      status
    }
  }
}
```

### 创建新合同

```graphql
mutation {
  createContract(
    businessNo: "CTR-2024-004"
    customerName: "Test Customer"
    amount: 10000.00
    currency: "USD"
    status: "PENDING"
    salesman: "Test Salesman"
  ) {
    id
    businessNo
    customerName
    status
  }
}
```

### 按状态查询合同

```graphql
query {
  contractsByStatus(status: "PROCESSING") {
    id
    businessNo
    customerName
    amount
  }
}
```

## 项目结构

```
src/main/java/com/freight/contract/
├── entity/          # 实体类
├── repository/      # 数据访问层
├── service/         # 业务逻辑层
├── graphql/         # GraphQL解析器
├── controller/      # REST控制器
├── config/          # 配置类
└── security/        # 安全配置
```

## 数据库模型

### 合同 (Contract)
- 业务编号、客户名称、金额、状态等

### 应收 (Receivable)
- 关联合同、客户名称、金额、到期日、状态

### 应付 (Payable)
- 关联合同、供应商名称、金额、到期日、状态

### 用户 (User)
- 用户名、邮箱、密码、角色、状态

## 开发指南

### 添加新实体

1. 在 `entity/` 目录下创建实体类
2. 在 `repository/` 目录下创建Repository接口
3. 在 `service/` 目录下创建Service类
4. 在 `graphql/` 目录下创建Resolver
5. 更新GraphQL Schema

### 运行测试

```bash
mvn test
```

### 构建项目

```bash
mvn clean package
```

## API文档

详细的GraphQL Schema可以在运行时通过访问 `/graphql` 端点的GraphQL Playground查看。

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。# freight-contract-backend
