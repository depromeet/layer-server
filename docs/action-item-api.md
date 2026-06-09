# 실행목표 API 문서

> **Base URL**: `https://stgapi.layerapp.io` (스테이징) / `https://api.layerapp.io` (프로덕션)  
> **인증**: 모든 API는 `Authorization: Bearer {accessToken}` 헤더 필요

---

## 변경 사항 (최신)

- 팀 실행목표 조회 응답에 **`createdAt`** 필드 추가
- **개인 실행목표** CRUD API 신규 추가 (스페이스 리더가 아닌 일반 멤버도 사용 가능)

---

## 팀 실행목표 (기존)

> 스페이스 리더가 팀 전체를 위해 관리하는 실행목표입니다.

### 팀 실행목표 생성

```
POST /api/action-item/create
```

**Request Body**
```json
{
  "retrospectId": 109,
  "content": "다음 스프린트까지 테스트 커버리지 80% 달성"
}
```

**Response** `201 Created`
```json
{
  "actionItemId": 42
}
```

---

### 스페이스 ID로 팀 실행목표 생성

가장 최근에 종료된 회고에 실행목표를 추가합니다.

```
POST /api/action-item/create/space/{spaceId}
```

**Request Body**
```json
{
  "spaceId": 31,
  "content": "다음 스프린트까지 테스트 커버리지 80% 달성"
}
```

**Response** `201 Created`
```json
{
  "actionItemId": 42
}
```

---

### 스페이스의 팀 실행목표 전체 조회 (회고별)

```
GET /api/action-item/space/{spaceId}
```

**Response** `200 OK`
```json
{
  "spaceId": 31,
  "spaceName": "레이어 팀",
  "teamActionItemList": [
    {
      "retrospectId": 109,
      "retrospectTitle": "3월 회고",
      "deadline": "2024-03-31T23:59:59",
      "status": "PROCEEDING",
      "actionItemList": [
        {
          "actionItemId": 42,
          "content": "테스트 커버리지 80% 달성",
          "createdAt": "2024-03-15T10:30:00"
        }
      ]
    },
    {
      "retrospectId": 100,
      "retrospectTitle": "2월 회고",
      "deadline": "2024-02-29T23:59:59",
      "status": "DONE",
      "actionItemList": [
        {
          "actionItemId": 35,
          "content": "코드 리뷰 문화 정착",
          "createdAt": "2024-02-10T09:00:00"
        }
      ]
    }
  ]
}
```

> `status` 값: `PROCEEDING` (가장 최근 회고), `DONE` (이전 회고)

---

### 스페이스의 최근 팀 실행목표 조회

가장 최근에 종료된 회고의 실행목표만 조회합니다.

```
GET /api/action-item/space/{spaceId}/recent
```

**Response** `200 OK`
```json
{
  "spaceId": 31,
  "spaceName": "레이어 팀",
  "teamActionItemList": [
    {
      "actionItemId": 42,
      "content": "테스트 커버리지 80% 달성",
      "retrospectId": 109,
      "retrospectTitle": "3월 회고"
    }
  ]
}
```

> 완료된 회고가 없는 경우 `teamActionItemList`는 빈 배열 반환

---

### 내 팀 실행목표 전체 조회 (멤버 기준)

내가 속한 모든 스페이스의 팀 실행목표를 회고별로 조회합니다.

```
GET /api/action-item/member
```

**Response** `200 OK`
```json
{
  "actionItems": [
    {
      "spaceId": 31,
      "spaceName": "레이어 팀",
      "retrospectId": 109,
      "retrospectTitle": "3월 회고",
      "deadline": "2024-03-31T23:59:59",
      "status": "PROCEEDING",
      "actionItemList": [
        {
          "actionItemId": 42,
          "content": "테스트 커버리지 80% 달성",
          "createdAt": "2024-03-15T10:30:00"
        }
      ]
    }
  ]
}
```

---

### 팀 실행목표 편집 (일괄)

순서 변경·내용 수정·신규 추가·삭제를 한 번에 처리합니다.  
요청 리스트의 순서가 최종 순서가 됩니다.

```
PATCH /api/action-item/retrospect/{retrospectId}/update
```

**Request Body**
```json
{
  "actionItems": [
    {
      "id": 42,
      "content": "테스트 커버리지 80% 달성 (수정됨)"
    },
    {
      "id": null,
      "content": "신규 실행목표"
    }
  ]
}
```

> - `id`가 있으면 기존 항목 수정  
> - `id`가 `null`이면 신규 생성  
> - 요청에 포함되지 않은 기존 항목은 **삭제**

**Response** `200 OK`

---

### 팀 실행목표 삭제

```
DELETE /api/action-item/{actionItemId}
```

**Response** `200 OK`

> 스페이스 리더만 삭제 가능

---

---

## 개인 실행목표 (신규)

> 스페이스 내 각 멤버가 특정 회고에 대해 개인적으로 설정하는 실행목표입니다.  
> **스페이스 리더가 아닌 일반 멤버도 생성·수정·삭제 가능합니다.**  
> 다른 멤버의 개인 실행목표는 조회되지 않습니다.

---

### 개인 실행목표 생성

```
POST /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}
```

**Path Parameters**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `spaceId` | Long | 스페이스 ID |
| `retrospectId` | Long | 회고 ID |

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

특정 회고에 대한 나의 개인 실행목표 목록을 조회합니다.

```
GET /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}
```

**Path Parameters**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `spaceId` | Long | 스페이스 ID |
| `retrospectId` | Long | 회고 ID |

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

순서 변경·내용 수정·신규 추가·삭제를 한 번에 처리합니다.  
요청 리스트의 순서가 최종 순서가 됩니다.

```
PATCH /api/action-item/personal/space/{spaceId}/retrospect/{retrospectId}/update
```

**Path Parameters**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `spaceId` | Long | 스페이스 ID |
| `retrospectId` | Long | 회고 ID |

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

**Path Parameters**

| 파라미터 | 타입 | 설명 |
|----------|------|------|
| `actionItemId` | Long | 삭제할 실행목표 ID |

**Response** `200 OK`

> 본인이 생성한 개인 실행목표만 삭제 가능. 타인의 항목 삭제 시 `403 Forbidden`

---

## 에러 응답

| HTTP Status | name | 설명 |
|-------------|------|------|
| `403` | `FORBIDDEN_ACTION_ITEM` | 해당 실행목표에 대한 권한 없음 (타인의 개인 실행목표 삭제 시도 등) |
| `404` | `NOT_FOUND_ACTION_ITEM` | 실행목표가 존재하지 않음 |
| `404` | `NOT_FOUND_MEMBER_SPACE_RELATION` | 해당 스페이스의 멤버가 아님 |
| `400` | `NO_PROCEEDING_ACTION_ITEMS` | 해당 스페이스에 완료된 회고가 없음 |

```json
{
  "name": "FORBIDDEN_ACTION_ITEM",
  "message": "해당 실행목표에 대한 권한이 없습니다."
}
```

---

## 팀 vs 개인 실행목표 비교

| 구분 | 팀 실행목표 | 개인 실행목표 |
|------|------------|--------------|
| 생성 권한 | 스페이스 리더만 | 스페이스 멤버 누구나 |
| 조회 범위 | 팀 전체 공유 | 본인만 조회 가능 |
| 삭제 권한 | 스페이스 리더만 | 본인만 |
| 응답에 `createdAt` | O | O |
