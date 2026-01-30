# チケットシステム API仕様書

## ベースURL

```
http://localhost:8080/api
```

## エンドポイント一覧

### 1. チケット作成

**POST** `/tickets`

リクエストボディ:

```json
{
  "title": "ログイン機能の不具合",
  "description": "ログイン時にエラーが発生する",
  "priority": "HIGH",
  "dueDateHours": 4
}
```

レスポンス:

```json
"3fa85f64-5717-4562-b3fc-2c963f66afa6"
```

### 2. ステータス更新

**PUT** `/tickets/{ticketId}/status`

リクエストボディ:

```json
{
  "newStatus": "IN_PROGRESS"
}
```

### 3. チケット割当

**PUT** `/tickets/{ticketId}/assign`

リクエストボディ:

```json
{
  "assigneeId": "user123",
  "assigneeName": "山田太郎"
}
```

### 4. 優先度更新

**PUT** `/tickets/{ticketId}/priority`

リクエストボディ:

```json
{
  "newPriority": "HIGH"
}
```

### 5. チケット解決

**PUT** `/tickets/{ticketId}/resolve`

### 6. チケット再開

**PUT** `/tickets/{ticketId}/reopen`

### 7. コメント追加

**POST** `/tickets/{ticketId}/comments`

リクエストボディ:

```json
{
  "author": "user123",
  "content": "調査中です"
}
```

### 8. チケット検索

**GET** `/tickets?status=NEW&priority=HIGH&assigneeId=user123&overdue=true`

レスポンス:

```json
[
  {
    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "title": "ログイン機能の不具合",
    "description": "ログイン時にエラーが発生する",
    "status": "NEW",
    "priority": "HIGH",
    "assigneeId": "user123",
    "assigneeName": "山田太郎",
    "dueDate": "2026-01-30T12:00:00",
    "createdAt": "2026-01-29T08:00:00",
    "updatedAt": "2026-01-29T08:00:00",
    "slaViolated": false
  }
]
```

### 9. チケット詳細取得

**GET** `/tickets/{ticketId}`

### 10. ステータス別統計

**GET** `/tickets/statistics/status`

レスポンス:

```json
[
  {
    "status": "NEW",
    "count": 10
  },
  {
    "status": "IN_PROGRESS",
    "count": 5
  }
]
```

### 11. 担当者別ワークロード

**GET** `/tickets/statistics/workload`

レスポンス:

```json
[
  {
    "assigneeId": "user123",
    "assigneeName": "山田太郎",
    "totalTickets": 15,
    "overdueTickets": 2
  }
]
```

### 12. SLA違反チケット一覧

**GET** `/tickets/sla-violated`

### 13. 優先度別レポート

**GET** `/tickets/statistics/priority`

レスポンス:

```json
[
  {
    "priority": "HIGH",
    "totalTickets": 20,
    "resolvedTickets": 15,
    "overdueTickets": 3,
    "averageResolutionHours": 3.2
  }
]
```

## ステータス値

- `NEW`: 新規
- `IN_PROGRESS`: 対応中
- `ON_HOLD`: 保留
- `RESOLVED`: 解決
- `REOPENED`: 再開

## 優先度値

- `HIGH`: 高（SLA: 4時間）
- `MEDIUM`: 中（SLA: 24時間）
- `LOW`: 低（SLA: 72時間）

## エラーレスポンス

```json
{
  "timestamp": "2026-01-29T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status transition: NEW -> RESOLVED"
}
```
