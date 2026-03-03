# Code Review AI Assistant  
**一个基于 GitHub App 的智能代码审查助手**

## 项目流程简图
<img width="843" height="702" alt="项目流程简图" src="https://github.com/user-attachments/assets/082a745f-ec69-42d7-89ea-8dab0c833bf8" />


## 项目背景与目标

在实际开发中，我发现手动代码审查耗时巨大，且容易遗漏深层技术债。  
我希望构建一个**可一键安装到 GitHub 仓库的 AI 助手**，实现：

- **首次安装时**：对整个仓库代码进行整体分析、打分、识别技术债、提炼项目代码风格  
- **后续每次 PR**：基于仓库历史风格 + 当前 diff，自动给出高质量审查意见  
- **额外能力**：智能生成优雅的 Commit Message、分析配置文件关键参数  
- **用户友好**：支持自行配置 DeepSeek / OpenAI 等大模型 API Token，成本可控

最终目标：让开发者把更多精力放在创造性工作上，而不是重复的风格争论与低级 bug 查找。

## 核心功能（当前规划与部分已实现）

| 优先级 | 功能                              | 状态          | 说明                                                                 |
|--------|-----------------------------------|---------------|----------------------------------------------------------------------|
| ★★★★★  | PR 自动审查（质量打分 A-F + 技术债 + 建议） | 已实现基本链路 | Webhook → diff → 审查 → 评论到 PR                                    |
| ★★★★★  | 全仓库初始化分析（风格 + 技术债）   | 进行中        | 克隆 → 向量化 → 存 Milvus → 建立长期记忆                             |
| ★★★★   | 自动生成优雅 Commit Message       | 规划中        | 基于 git diff 智能生成 Conventional Commit 风格                      |
| ★★★★   | 代码相似度检测 & 最佳实践推荐     | 初步实现      | Milvus 向量检索相似代码，提示可复用片段                              |
| ★★★    | 配置文件关键参数提取与分析        | 规划中        | 支持常见格式（application.yml、pom.xml 等）                          |
| ★★     | 用户管理 & GitHub 账号绑定        | 暂缓          | MVP 阶段先专注核心审查能力                                           |

## 技术选型与理由

| 技术栈               | 选择理由                                                                 | 替代方案考虑过          |
|----------------------|--------------------------------------------------------------------------|--------------------------|
| Spring Boot 3 + Spring AI | 生态完整、Java 生态最友好 AI 集成框架，快速原型                         | FastAPI / NestJS         |
| DeepSeek (对话/生成) | 性价比极高（远低于 GPT-4o），API 完全兼容 OpenAI                         | Claude / GPT-4o-mini     |
| Ollama (Embedding)   | Spring AI 原生不支持 DeepSeek embedding，只能本地跑 Ollama              | sentence-transformers    |
| Milvus 2.6           | 开源分布式向量数据库之王，支持高并发、PQ 量化压缩、易扩展               | Chroma / Weaviate / PGVector |
| Redis                | 缓存审查结果 & Prompt，显著降低重复调用大模型成本                       | -                        |
| JavaParser           | 语法树级代码切片，比正则更准确，支持复杂 Java 代码                      | tree-sitter              |
| smee.io              | 本地开发时完美模拟 GitHub Webhook                                       | ngrok                    |

**关键决策反思**  
- 为什么不用纯 OpenAI/GPT？→ 个人项目成本敏感，DeepSeek 质量接近且便宜 5–10 倍  
- 为什么用 Milvus 而非 Chroma？→ 需要长期支持全仓库向量（百万级 chunk），Milvus 更成熟  
- 本地 Ollama embedding → 增加了部署复杂度，但保证了数据隐私因为是本地调用 & 零额外费用

## 开发过程与进度（时间线式）

2025.10 – 2026.03（持续迭代中）

- **阶段1：基础设施打通**  
  创建 GitHub App → 配置 Webhook → smee.io 本地转发 → Spring Boot 接收成功

- **阶段2：代码 → AI 审查链路**  
  获取 PR diff → JavaParser 切片 → Ollama Embedding → 存 Milvus（踩过 null 值坑）  
  调用 DeepSeek 生成审查意见 → 通过 GitHub API 评论回 PR（已跑通端到端）

- **阶段3：向量数据库探索**  
  实现代码向量化存储 → 初步相似度检索（但阈值过高目前主要召回配置而非代码，正在调优）

- **当前重点（TODO 高优先）**  
  1. 项目首次安装时的全量代码向量化与风格提取  
  2. 相似度阈值 & Prompt 压缩 & Redis 缓存（降低成本）  
  3. 完善 System Prompt 模板，使输出结构化（JSON）且高质量  
  4. Commit Message 生成模块

## 遇到的主要技术挑战 & 解决方案

| 挑战                              | 解决方案                                    |
|-----------------------------------|------------------------------------------------------------|
| Spring AI 无 DeepSeek embedding 支持 | Docker 部署 Ollama + nomic-embed-text，桥接 Spring AI     |
| Milvus 检索阈值过高，只召回配置     | 调整阈值     |
| API 调用成本高                    | Redis 缓存相同 diff 的审查结果 + Prompt 精简 + 固定 System Prompt |
| 代码切片不准（注释、长方法）       | JavaParser + 自定义 CodeChunk 规则 + 长度控制              |
| Webhook 安全                      | 后续上线会加上 GitHub 签名验证（当前本地开发阶段跳过）     |

## 未来规划（Roadmap）

- v0.1 MVP：PR 自动审查 + Commit Msg 生成  
- v0.5：全仓库初始化 + 代码风格长期记忆  
- v1.0：支持多语言（Java/TS/Python） + 用户自定义 Prompt + 审查报告页面  
- 长期：成为可商用 GitHub Marketplace App

## 快速启动（本地开发）

```bash
# 1. 启动 Milvus standalone
docker-compose up -d    # 使用官方 milvus-standalone docker-compose.yml

# 2. 启动 Ollama + embedding 模型
docker run -d -v ollama:/root/.ollama -p 11434:11434 --name ollama ollama/ollama
ollama pull nomic-embed-text

# 3. Redis
docker run -d -p 6379:6379 redis

# 4. 启动 Spring Boot
./mvnw spring-boot:run

# 5. 启动 smee.io 转发（另开终端）
smee --url https://smee.io/你的channel --path /webhook --port 8080
```
欢迎各位提出宝贵意见！我会非常开心继续把这个工具打磨得更实用。
Star ⭐️ & Fork 一份支持～
