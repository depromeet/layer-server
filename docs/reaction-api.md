# 회고 반응 API

회고 답변에 이모지 반응을 달고 조회하는 API 모음입니다.

> **Base URL** `https://stgapi.layerapp.io`  
> **Auth** 모든 요청에 `Authorization: Bearer {accessToken}` 헤더 필요 (반응 목록 조회 제외)

---

## 이모지 코드 목록

서버-클라이언트는 아래 코드 값으로 소통하며, **이미지 리소스는 클라이언트에서 관리**합니다.

| 코드 | 설명 |
|---|---|
| `LEC01` | 대단해 |
| `LEC02` | 완벽해 |
| `LEC03` | 최고야 |
| `LEC04` | 역시 |
| `LEC05` | 고생했어 |
| `LEC06` | 기대중 |
| `LEC07` | 괜찮아 |
| `LEC08` | 성장했다 |
| `LEC09` | 화이팅 |
| `LEC10` | 할 수 있다 |

---

## 목차

1. [사용 가능한 모든 반응 조회](#1-사용-가능한-모든-반응-조회)
2. [최근 사용한 반응 조회](#2-최근-사용한-반응-조회)
3. [회고 반응 생성](#3-회고-반응-생성)
4. [회고 반응 삭제](#4-회고-반응-삭제)
5. [회고 전체 반응 조회](#5-회고-전체-반응-조회)

---

## 1. 사용 가능한 모든 반응 조회

반응 선택 UI에서 사용할 수 있는 전체 반응 목록을 가져옵니다.

```
GET /api/reaction
```

### Response `200`

```json
{
  "reactions": [
    {
      "id": 1,
      "emojiCode": "LEC01",
      "description": "대단해"
    },
    {
      "id": 2,
      "emojiCode": "LEC02",
      "description": "완벽해"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| `reactions` | `array` | 반응 목록 |
| `reactions[].id` | `number` | 반응 ID |
| `reactions[].emojiCode` | `string` | 이모지 코드 (예: `LEC01`) |
| `reactions[].description` | `string` | 이모지 설명 (예: `대단해`) |

---

## 2. 최근 사용한 반응 조회

내가 최근에 사용한 반응을 중복 없이 최신순으로 N개 가져옵니다.  
반응 선택 UI 상단 "최근 사용" 영역에 활용합니다.

```
GET /api/reaction/recent?limit={N}
```

### Query Parameters

| 파라미터 | 타입 | 필수 | 기본값 | 설명 |
|---|---|---|---|---|
| `limit` | `number` | N | `8` | 가져올 반응 개수 |

### Request 예시

```
GET /api/reaction/recent?limit=8
```

### Response `200`

```json
{
  "reactions": [
    {
      "id": 3,
      "emojiCode": "LEC03",
      "description": "최고야"
    },
    {
      "id": 1,
      "emojiCode": "LEC01",
      "description": "대단해"
    }
  ]
}
```

> 최신순 정렬, 한 번도 반응을 남기지 않은 경우 빈 배열 반환

---

## 3. 회고 반응 생성

특정 회고 답변에 반응을 답니다.  
**하나의 답변에 여러 이모지를 달 수 있습니다.** 단, 같은 이모지를 같은 답변에 중복으로 달 수는 없습니다.

```
POST /space/{spaceId}/retrospect/{retrospectId}/reaction
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|---|---|---|
| `spaceId` | `number` | 스페이스 ID |
| `retrospectId` | `number` | 회고 ID |

### Request Body

```json
{
  "emojiCode": "LEC01",
  "answerId": 5
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `emojiCode` | `string` | Y | 사용할 이모지 코드 (예: `LEC01`) |
| `answerId` | `number` | Y | 반응을 달 답변 ID |

### Response

| 상태코드 | 설명 |
|---|---|
| `201` | 반응 생성 성공 |
| `400` | 해당 답변에 동일한 이모지를 이미 달았음 |
| `403` | 해당 스페이스 멤버가 아님 |
| `404` | 존재하지 않는 이모지 코드 |

---

## 4. 회고 반응 삭제

내가 단 반응을 삭제합니다. **본인이 단 반응만 삭제 가능합니다.**  
한 답변에 여러 반응을 달 수 있으므로, **반응 조회 API에서 받은 `retrospectReactionId`로 삭제할 반응을 특정해야 합니다.**

```
DELETE /space/{spaceId}/retrospect/{retrospectId}/reaction/{retrospectReactionId}
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|---|---|---|
| `spaceId` | `number` | 스페이스 ID |
| `retrospectId` | `number` | 회고 ID |
| `retrospectReactionId` | `number` | 삭제할 반응 ID — 반응 조회 API의 `reactions[].retrospectReactionId` 값 사용 |

### Response

| 상태코드 | 설명 |
|---|---|
| `200` | 반응 삭제 성공 |
| `403` | 본인의 반응이 아님 |
| `404` | 존재하지 않는 회고 반응 ID |

---

## 5. 회고 전체 반응 조회

특정 회고의 **모든 답변**에 달린 반응을 한 번에 가져옵니다.  
`memberId` 비교를 통해 내가 단 반응과 다른 사람의 반응을 구분할 수 있습니다.

```
GET /space/{spaceId}/retrospect/{retrospectId}/reaction
```

### Path Parameters

| 파라미터 | 타입 | 설명 |
|---|---|---|
| `spaceId` | `number` | 스페이스 ID |
| `retrospectId` | `number` | 회고 ID |

### Response `200`

```json
{
  "answerReactions": [
    {
      "answerId": 10,
      "reactions": [
        {
          "retrospectReactionId": 1,
          "emojiCode": "LEC01",
          "description": "대단해",
          "memberId": 29,
          "memberName": "홍길동",
          "memberProfileImgUrl": "https://example.com/profile.png"
        },
        {
          "retrospectReactionId": 2,
          "emojiCode": "LEC05",
          "description": "고생했어",
          "memberId": 31,
          "memberName": "김철수",
          "memberProfileImgUrl": "https://example.com/profile2.png"
        }
      ]
    },
    {
      "answerId": 11,
      "reactions": []
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|---|---|---|
| `answerReactions` | `array` | 답변별 반응 목록 |
| `answerReactions[].answerId` | `number` | 답변 ID |
| `answerReactions[].reactions` | `array` | 해당 답변에 달린 반응 목록 (없으면 빈 배열) |
| `reactions[].retrospectReactionId` | `number` | 회고 반응 ID — **삭제 시 이 값을 사용** |
| `reactions[].emojiCode` | `string` | 이모지 코드 (예: `LEC01`) |
| `reactions[].description` | `string` | 이모지 설명 (예: `대단해`) |
| `reactions[].memberId` | `number` | 반응을 단 멤버 ID — **내 반응 여부 판단에 사용** |
| `reactions[].memberName` | `string` | 반응을 단 멤버 이름 |
| `reactions[].memberProfileImgUrl` | `string` | 반응을 단 멤버 프로필 이미지 URL |

---

## 전형적인 사용 흐름

```
1. GET /api/reaction                          → 전체 반응 목록 가져오기 (반응 선택 UI용)
2. GET /api/reaction/recent?limit=8           → 최근 사용 반응 가져오기 (UI 상단 영역)
3. GET /space/{id}/retrospect/{id}/reaction   → 현재 회고의 반응 상태 가져오기
4. POST .../reaction                          → 반응 생성
5. DELETE .../reaction/{id}                  → 반응 취소
```

## 내가 단 반응 판단 방법

한 사람이 같은 답변에 여러 이모지를 달 수 있으므로, `filter`로 내 반응 목록을 모두 가져와야 합니다.

```js
// 내가 단 반응 목록 (여러 개일 수 있음)
const myReactions = reactions.filter(r => r.memberId === currentMemberId);

// 특정 이모지를 내가 이미 달았는지 확인
const alreadyReacted = (emojiCode) =>
  myReactions.some(r => r.emojiCode === emojiCode);

// 특정 이모지 반응 취소 시 — retrospectReactionId로 특정
const cancelReaction = (emojiCode) => {
  const target = myReactions.find(r => r.emojiCode === emojiCode);
  if (target) {
    await deleteReaction(target.retrospectReactionId);
  }
};
```
