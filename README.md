# 心理学网站系统

基于 Spring Cloud 微服务架构的心理学内容管理平台。

## 技术栈

- **后端框架：** Spring Boot 3.0.2 + Spring Cloud 2022.0.0
- **开发语言：** Java 17
- **构建工具：** Maven
- **架构模式：** 微服务

## 项目结构

```
心理学网站系统_Project/
├── psychology-parent/              # 父工程
│   ├── common-api/                 # 公共模块（实体类、工具类）
│   ├── gateway/                    # 网关服务
│   ├── psychology-user-service/    # 用户服务
│   ├── psychology-content-service/ # 内容服务
│   ├── psychology-comment-service/ # 评论服务
│   ├── psychology-favorite-service/# 收藏服务
│   ├── psychology-web/             # Web 前端服务
│   ├── sql/                        # 数据库脚本
│   └── uploads/                    # 上传文件
├── 心理文章/                        # 文章资源
├── 心理书籍/                        # 书籍资源
├── 心理图片/                        # 图片资源
└── article_insert.sql              # 文章数据初始化脚本
```

## 微服务说明

| 服务 | 说明 |
|------|------|
| `gateway` | API 网关，统一入口和路由 |
| `psychology-user-service` | 用户注册、登录、信息管理 |
| `psychology-content-service` | 文章、书籍、公告等内容管理 |
| `psychology-comment-service` | 评论的增删改查 |
| `psychology-favorite-service` | 用户收藏功能 |
| `psychology-web` | 前端页面服务 |

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### 启动步骤

1. 导入 `sql/` 目录下的数据库脚本
2. 修改各服务的数据库连接配置
3. 按顺序启动服务：
   ```bash
   # 先启动网关
   cd psychology-parent/gateway
   mvn spring-boot:run

   # 再启动各业务服务
   cd psychology-parent/psychology-user-service
   mvn spring-boot:run

   cd psychology-parent/psychology-content-service
   mvn spring-boot:run

   # ...依次启动其他服务
   ```

## 作者

- GitHub: [lansij](https://github.com/lansij)
