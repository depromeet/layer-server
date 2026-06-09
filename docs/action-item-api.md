# 실행목표 API 변경 사항

> **Base URL**: `https://stgapi.layerapp.io` (스테이징) / `https://api.layerapp.io` (프로덕션)  
> **인증**: 모든 API는 `Authorization: Bearer {accessToken}` 헤더 필요

---

## 변경: 팀 실행목표 응답에 `createdAt` 추가

기존 팀 실행목표 조회 API(`GET /api/action-item/space/{spaceId}`, `GET /api/action-item/space/{spaceId}/recent`, `GET /api/action-item/member`) 응답의 각 실행목표 항목에 `createdAt` 필드가 추가됩니다.

**Before**
```json
{
  "actionItemId": 42,
  "content": "테스트 커버리지 80% 달성"
}
```

**After**
```json
{
  "actionItemId": 42,
  "content": "테스트 커버리지 80% 달성",
  "createdAt": "2024-03-15T10:30:00"
}
```

---

## 신규: 개인 실행목표 API

> 스페이스 리더가 아닌 일반 멤버도 사용 가능하며, 본인의 개인 실행목표만 조회됩니다.

---

### 스페이스의 개인 실행목표 전체 조회 (회고별)

팀 실행목표의 `GET /api/action-item/space/{spaceId}`와 동일한 구조로 회고별로 묶어 반환합니다.

```
GET /api/action-item/personal/space/{spaceId}
```

**Response** `200 OK`
```json
{
  "spaceId": 430,
  "spaceName": "세트스",
  "personalActionItemList": [
    {
      "retrospectId": 310,
      "retrospectTitle": "테스트",
      "deadline": "2025-03-19T05:00:00",
      "status": "PROCEEDING",
      "actionItemList": [
        {
          "actionItemId": 55,
          "content": "개인 학습 2시간씩 투자하기",
          "createdAt": "2024-03-15T10:30:00"
        }
      ]
    },
    {
      "retrospectId": 309,
      "retrospectTitle": "이전 회고",
      "deadline": "2025-03-12T04:00:00",
      "status": "DONE",
      "actionItemList": []
    }
  ]
}
```

> `status`: `PROCEEDING` (가장 최근 회고), `DONE` (이전 회고)  
> 개인 실행목표가 없는 회고는 `actionItemList`가 빈 배열로 반환

---

### 스페이스의 최근 회고 개인 실행목표 조회

팀 실행목표의 `GET /api/action-item/space/{spaceId}/recent`에 대응합니다.

```
GET /api/action-item/personal/space/{spaceId}/recent
```

**Response** `200 OK`
```json
{
  "spaceId": 430,
  "spaceName": "세트스",
  "personalActionItemList": [
    {
      "actionItemId": 55,
      "content": "개인 학습 2시간씩 투자하기",
      "retrospectId": 310,
      "retrospectTitle": "테스트",
      "createdAt": "2024-03-15T10:30:00"
    }
  ]
}
```

> 완료된 회고가 없거나 개인 실행목표가 없으면 `personalActionItemList`는 빈 배열 반환

---

### 내 모든 스페이스의 개인 실행목표 조회

팀 실행목표의 `GET /api/action-item/member`에 대응합니다.

```
GET /api/action-item/personal/member
```

**Response** `200 OK`
```json
{
  "actionItems": [
    {
      "spaceId": 430,
      "spaceName": "세트스",
      "retrospectId": 310,
      "retrospectTitle": "테스트",
      "deadline": "2025-03-19T05:00:00",
      "status": "PROCEEDING",
      "actionItemList": [
        {
          "actionItemId": 55,
          "content": "개인 학습 2시간씩 투자하기",
          "createdAt": "2024-03-15T10:30:00"
        }
      ]
    }
  ]
}
```

---

### 개인 실행목표 생성

```
POST /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}
```

**Request Body**
```json
{
  "content": "다음 회고 전까지 개인 학습 2시간씩 투자하기"
}
```

**Response** `201 Created`
```json
{
  "actionItemId": 55
}
```

---

### 개인 실행목표 조회

```
GET /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}
```

**Response** `200 OK`
```json
{
  "actionItems": [
    {
      "actionItemId": 55,
      "content": "다음 회고 전까지 개인 학습 2시간씩 투자하기",
      "actionItemOrder": 1,
      "createdAt": "2024-03-15T10:30:00"
    },
    {
      "actionItemId": 56,
      "content": "매일 회고 일기 쓰기",
      "actionItemOrder": 2,
      "createdAt": "2024-03-15T11:00:00"
    }
  ]
}
```

> 개인 실행목표가 없는 경우 `actionItems`는 빈 배열 반환

---

### 개인 실행목표 편집 (일괄)

순서 변경·내용 수정·신규 추가·삭제를 한 번에 처리합니다. 요청 리스트의 순서가 최종 순서가 됩니다.

```
PATCH /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}/update
```

**Request Body**
```json
{
  "actionItems": [
    {
      "id": 55,
      "content": "개인 학습 2시간 (수정됨)"
    },
    {
      "id": null,
      "content": "새로운 개인 실행목표"
    }
  ]
}
```

> - `id`가 있으면 기존 항목 수정  
> - `id`가 `null`이면 신규 생성  
> - 요청에 포함되지 않은 기존 항목은 **삭제**

**Response** `200 OK`

---

### 개인 실행목표 삭제

```
DELETE /api/action-item/personal/{actionItemId}
```

**Response** `200 OK`

> 본인이 생성한 개인 실행목표만 삭제 가능. 타인의 항목 삭제 시 `403 Forbidden`

---

## 에러 응답

| HTTP Status | name | 설명 |
|-------------|------|------|
| `403` | `FORBIDDEN_ACTION_ITEM` | 타인의 개인 실행목표 삭제 시도 |
| `404` | `NOT_FOUND_ACTION_ITEM` | 실행목표가 존재하지 않음 |
| `404` | `NOT_FOUND_MEMBER_SPACE_RELATION` | 해당 스페이스의 멤버가 아님 |
