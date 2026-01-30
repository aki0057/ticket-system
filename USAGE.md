# チケットシステム 使用例

このドキュメントでは、チケットシステムの主な使用例を示します。

## 1. アプリケーションの起動

```bash
# Mavenでビルド
mvn clean install

# アプリケーション起動
mvn spring-boot:run
```

起動後、以下のURLでアクセス可能です：

- アプリケーション: http://localhost:8080
- H2コンソール: http://localhost:8080/h2-console

## 2. curlコマンドでのAPI操作

### チケット作成

```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "ログイン機能の不具合",
    "description": "ログイン時に500エラーが発生する",
    "priority": "HIGH",
    "dueDateHours": 4
  }'
```

レスポンス例:

```
"3fa85f64-5717-4562-b3fc-2c963f66afa6"
```

### チケット割当

```bash
curl -X PUT http://localhost:8080/api/tickets/3fa85f64-5717-4562-b3fc-2c963f66afa6/assign \
  -H "Content-Type: application/json" \
  -d '{
    "assigneeId": "user001",
    "assigneeName": "山田太郎"
  }'
```

### ステータス更新（対応中へ）

```bash
curl -X PUT http://localhost:8080/api/tickets/3fa85f64-5717-4562-b3fc-2c963f66afa6/status \
  -H "Content-Type: application/json" \
  -d '{
    "newStatus": "IN_PROGRESS"
  }'
```

### コメント追加

```bash
curl -X POST http://localhost:8080/api/tickets/3fa85f64-5717-4562-b3fc-2c963f66afa6/comments \
  -H "Content-Type: application/json" \
  -d '{
    "author": "user001",
    "content": "調査を開始しました。データベース接続に問題がありそうです。"
  }'
```

### チケット解決

```bash
curl -X PUT http://localhost:8080/api/tickets/3fa85f64-5717-4562-b3fc-2c963f66afa6/resolve
```

### チケット検索（ステータス別）

```bash
curl "http://localhost:8080/api/tickets?status=IN_PROGRESS"
```

### チケット検索（担当者別）

```bash
curl "http://localhost:8080/api/tickets?assigneeId=user001"
```

### SLA違反チケット取得

```bash
curl http://localhost:8080/api/tickets/sla-violated
```

### ステータス別統計

```bash
curl http://localhost:8080/api/tickets/statistics/status
```

レスポンス例:

```json
[
  {
    "status": "NEW",
    "count": 5
  },
  {
    "status": "IN_PROGRESS",
    "count": 3
  },
  {
    "status": "RESOLVED",
    "count": 10
  }
]
```

### 担当者別ワークロード

```bash
curl http://localhost:8080/api/tickets/statistics/workload
```

レスポンス例:

```json
[
  {
    "assigneeId": "user001",
    "assigneeName": "山田太郎",
    "totalTickets": 8,
    "overdueTickets": 1
  }
]
```

### 優先度別レポート

```bash
curl http://localhost:8080/api/tickets/statistics/priority
```

## 3. PowerShellでのAPI操作

### チケット作成

```powershell
$body = @{
    title = "データベースエラー"
    description = "クエリ実行時にタイムアウト"
    priority = "MEDIUM"
    dueDateHours = 24
} | ConvertTo-Json

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/tickets" `
    -ContentType "application/json" -Body $body
```

### チケット一覧取得

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/tickets"
```

### ステータス更新

```powershell
$ticketId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
$body = @{
    newStatus = "RESOLVED"
} | ConvertTo-Json

Invoke-RestMethod -Method Put -Uri "http://localhost:8080/api/tickets/$ticketId/status" `
    -ContentType "application/json" -Body $body
```

## 4. 典型的なワークフロー例

### シナリオ: 緊急バグの対応

```bash
# 1. 高優先度チケット作成
TICKET_ID=$(curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -d '{
    "title": "本番環境でシステムダウン",
    "description": "全サービスが停止している",
    "priority": "HIGH",
    "dueDateHours": 4
  }' | tr -d '"')

echo "作成されたチケットID: $TICKET_ID"

# 2. 担当者割当
curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/assign" \
  -H "Content-Type: application/json" \
  -d '{
    "assigneeId": "emergency-team",
    "assigneeName": "緊急対応チーム"
  }'

# 3. 対応開始
curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/status" \
  -H "Content-Type: application/json" \
  -d '{"newStatus": "IN_PROGRESS"}'

# 4. 進捗コメント追加
curl -X POST "http://localhost:8080/api/tickets/$TICKET_ID/comments" \
  -H "Content-Type: application/json" \
  -d '{
    "author": "emergency-team",
    "content": "原因特定しました。データベース接続プールが枯渇しています。"
  }'

# 5. 解決
curl -X PUT "http://localhost:8080/api/tickets/$TICKET_ID/resolve"

# 6. 確認
curl "http://localhost:8080/api/tickets/$TICKET_ID"
```

## 5. バッチジョブの動作確認

バッチジョブは以下のスケジュールで自動実行されます：

- **SLA超過エスカレーション**: 毎日深夜0時
- **統計レポート生成**: 毎日深夜1時
- **期限切れチケット自動クローズ**: 毎日深夜2時

ログファイル `logs/ticket-system.log` で実行結果を確認できます。

## 6. ログの確認

```bash
# リアルタイムでログを確認
tail -f logs/ticket-system.log

# 監査ログのみ抽出
grep "【監査ログ】" logs/ticket-system.log

# バッチログのみ抽出
grep "【バッチ" logs/ticket-system.log
```

## 7. H2データベースの確認

1. ブラウザで http://localhost:8080/h2-console にアクセス
2. 以下の接続情報を入力：
   - JDBC URL: `jdbc:h2:mem:ticketdb`
   - User Name: `sa`
   - Password: （空白）
3. SQLでデータを確認：

```sql
SELECT * FROM tickets;
SELECT * FROM tickets WHERE status = 'NEW';
SELECT * FROM tickets WHERE due_date < CURRENT_TIMESTAMP;
```

## 8. テストの実行

```bash
# 全テスト実行
mvn test

# 特定のテストクラスを実行
mvn test -Dtest=StatusTest

# テストカバレッジレポート生成（JaCoCo使用の場合）
mvn clean test jacoco:report
```

## 9. トラブルシューティング

### ポート8080が既に使用されている場合

`application.yml` で別のポートを指定：

```yaml
server:
  port: 8081
```

### データベース接続エラー

H2の接続設定を確認し、必要に応じて再起動してください。

### バッチジョブが実行されない

`application.yml` の `spring.batch.job.enabled` が `false` になっていることを確認してください（手動制御のため）。
