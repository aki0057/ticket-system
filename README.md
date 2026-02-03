# 社内ITヘルプデスク対応管理システム

DDD・TDD・イベント駆動・CQRSを実装したSpring Bootアプリケーション

## 1. システム概要

社内ITヘルプデスク業務を効率化するためのチケット管理システムです。チケット作成・検索、ステータス遷移、承認フロー、監査ログ機能を備えています。

## 2. アーキテクチャ

### ディレクトリ構造

```
src/main/java/com/example/ticket/
├── application/       # ユースケース層（Service）
├── domain/            # ドメイン層（ValueObject、DomainEvent）
├── infrastructure/    # 永続化層（Entity、Mapper、RepositoryImpl）
└── presentation/      # Web層（Controller）
```

### 技術スタック

- Java 23
- Spring Boot 3.5.10
- Spring Data JPA
- Spring Batch
- Spring Boot Actuator（運用監視）
- Thymeleaf（テンプレートエンジン）
- H2 Database（開発環境）
- PostgreSQL（本番環境）
- JUnit 5 + Mockito（テスト）
- Lombok（ボイラープレート削減）

## 3. DDD設計

実装予定

## 4. TDD実装

実装予定

## 5. イベント駆動

実装予定

## 6. CQRS実装

実装予定

## 7. ログ機能

実装予定

## 8. SpringBatch

実装予定
