# チケットシステム - 実装要件書

## 1. システム概要

**目的**: DDD、TDD、イベント駆動、CQRS、ログ機能、SpringBatchを実装・学習するためのチケット管理システム

**スコープ**: チケットの作成、ステータス管理、割当、優先度管理、SLAトラッキング

## 2. アーキテクチャ

### レイヤー構成

```
presentation (controller)
    ↓
application (command/query/service)
    ↓
domain (entity/vo/event/repository/service)
    ↓
infrastructure (persistence/event/batch)
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

### エンティティ

- `Ticket`: チケット本体
- `Assignment`: 割当情報
- `Comment`: コメント

### 値オブジェクト

- `TicketId`: チケットID
- `Status`: ステータス（新規、対応中、保留、解決、再開）
- `Priority`: 優先度（高、中、低）
- `Assignee`: 担当者
- `DueDate`: 期限

### ステート遷移ルール

- 新規 → 対応中、保留
- 対応中 → 保留、解決
- 保留 → 対応中
- 解決 → 再開
- 再開 → 対応中、保留、解決

**不可な遷移**: 保留中 → 新規、解決 → 新規など

## 4. TDD実装

### テストカバレッジ対象

1. ステータス遷移の妥当性チェック
2. 優先度と期限の組み合わせ検証
3. 割当先の有効性確認
4. コメント追加時の権限チェック
5. エッジケース（null、境界値、例外系）

## 5. イベント駆動

### ドメインイベント

1. `TicketCreatedEvent`: チケット作成時
2. `StatusChangedEvent`: ステータス変更時
3. `AssigneeChangedEvent`: 担当者変更時
4. `PriorityChangedEvent`: 優先度変更時
5. `CommentAddedEvent`: コメント追加時
6. `TicketResolvedEvent`: チケット解決時
7. `TicketReopenedEvent`: チケット再開時
8. `DueDateExtendedEvent`: 期限延長時

### イベントハンドラーチェーン

- イベント発行 → 監査ログ記録 → 通知送信 → 統計更新

## 6. CQRS実装

### コマンド側

- `CreateTicketCommand`
- `UpdateTicketStatusCommand`
- `AssignTicketCommand`
- `UpdatePriorityCommand`
- `ResolveTicketCommand`

### クエリ側

- ステータス別集計
- 担当者別ワークロード
- SLA違反者リスト
- 優先度別レポート
- チケット検索（複数条件）

## 7. ログ機能

### ログレベル

- **INFO**: ビジネス操作（チケット作成、ステータス変更など）
- **WARN**: SLA違反、期限超過
- **ERROR**: 例外、システムエラー

### 監査ログ

- 全チケット操作を記録
- イベント発行時刻
- 操作者情報
- 変更前後の値

## 8. SpringBatch

### バッチジョブ

1. **SLA超過チケットエスカレーション**
    - 期限超過チケットを自動で優先度UP
    - スケジュール: 毎日深夜0時

2. **統計レポート生成**
    - ステータス別・担当者別の統計をDB保存
    - スケジュール: 毎日深夜1時

3. **期限切れチケット自動クローズ**
    - 期限超過かつ解決済みのチケットを自動クローズ
    - スケジュール: 毎日深夜2時

## 9. 設定管理

### 環境別設定

このプロジェクトは以下の環境別設定ファイルを提供しています：

| ファイル                  | 用途         | git管理 | 説明                           |
| ------------------------- | ------------ | ------- | ------------------------------ |
| `application.yml`         | 開発環境     | ✅      | H2 インメモリDB、DEBUG ログ    |
| `application-default.yml` | テンプレート | ✅      | 新規開発者向け参考ファイル     |
| `application-prod.yml`    | 本番環境     | ✅      | PostgreSQL接続、環境変数設定例 |
| `application-local.yml`   | ローカル開発 | ❌      | 個人環境用（.gitignoreで除外） |

### 設定の優先順位

```
System Properties (最優先)
  ↓
Environment Variables
  ↓
application-{profile}.yml
  ↓
application.yml (デフォルト)
```

### 環境切り替え方法

#### 開発環境（デフォルト）

```bash
# 特に指定しない場合はapplication.ymlが使用されます
.\mvnw.cmd spring-boot:run
```

#### 本番環境

```bash
# Linux/Mac
export SPRING_PROFILES_ACTIVE=prod
./mvnw spring-boot:run

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE="prod"
.\mvnw.cmd spring-boot:run
```

#### ローカル環境（個人設定）

```bash
# application-local.yml を作成して個人設定を定義
# .gitignoreで除外されるため、個人環境に特化した設定が可能
.\mvnw.cmd spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

### 本番環境の機密情報管理

本番環境ではapplication-prod.ymlで環境変数を参照します：

```yaml
datasource:
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

環境変数の設定例：

```bash
# Docker環境
docker run -e DB_USERNAME=ticket_user -e DB_PASSWORD=secure_pass ticket-system

# Kubernetes環境
kubectl set env deployment/ticket-system DB_USERNAME=ticket_user DB_PASSWORD=secure_pass

# 手動設定
export DB_USERNAME=ticket_user
export DB_PASSWORD=secure_pass
```

## 10. セットアップと実行

### 実行

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Mac/Linux
./mvnw spring-boot:run
```

### テスト実行

```bash
# Windows
.\mvnw.cmd test

# Mac/Linux
./mvnw test
```

### コード整形（Spotless）

```bash
# Windows
.\mvnw.cmd spotless:apply

# Mac/Linux
./mvnw spotless:apply
```

### H2コンソール

http://localhost:8080/h2-console

### Actuatorエンドポイント

```
http://localhost:8080/actuator              # 利用可能なエンドポイント一覧
http://localhost:8080/actuator/health       # ヘルスチェック
http://localhost:8080/actuator/metrics      # メトリクス
http://localhost:8080/actuator/prometheus   # Prometheus形式メトリクス
```

## 11. 今後の拡張

- REST API仕様（OpenAPI/Swagger）
- 認証・認可（Spring Security）
- 非同期イベント処理（Spring Event、Kafka）
- 分散トレーシング（Spring Cloud Sleuth）
- メトリクス収集（Micrometer）
