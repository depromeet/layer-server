# layer server 
![Group 110](https://github.com/depromeet/layer/assets/19422885/b85ee18d-ca94-4f20-9058-c03e41188291)

## 👨‍👨‍👧 팀원 소개 
<div align="center">
	<table>
  <th><a href="https://github.com/raymondanythings"> 엽용현 </th>
	<th><a href="https://github.com/mikekks"> 송민규 </th>
  <th><a href="https://github.com/clean2001"> 김세정 </th>
	<tr>
		<td><img width="300" alt="용현" src="https://github.com/depromeet/layer-server/assets/100754581/ac4aec41-499a-46ae-af58-5c9800391865">
    </td>
		<td><img width="300" alt="민규" src="https://github.com/depromeet/layer-server/assets/100754581/e7d1a057-287d-48bd-a8c2-af009f2cc219">
    </td>
    <td><img width="300" alt="세정" src="https://github.com/depromeet/layer-server/assets/100754581/45e216a1-ae8c-40b4-b028-24c51c39abd7">
    </td>
	</tr>
<th> BE </th>
<th> BE, Lead </th>
<th> BE </th>
<tr>
<td>
- CI/CD 및 k3s 구축<br>
- 회고 스페이스, 멤버 관련 기능 구현 <br>
- PreSignedUrl 구현 및 이미지 통신 구축<br>
- Cloud 로그 시스템 적용 및 NCP 인프라 구축
</td>
<td>
- 멀티모듈 설계 및 프로젝트 세팅<br>
- 회고, 회고 질문 & 답변, 분석 관련 기능 구현<br>
- 회고마감 관련 배치 모듈 구현<br>
- OpenAI 통신 파이프라인 구축<br>
</td>
<td>
- 인증/인가 필터 구현<br>
- OAuth2.0 구현<br>
- 회고 템플릿, 실행 목표, 회원 관련 기능 구현<br>
- Redis 적용<br>
</td>
</tr>
	</table>
</div>

<br>
<br>

## 🏭 아키텍처

![layer-arch](https://github.com/user-attachments/assets/f3811440-ba52-4aea-a5a2-b38d5c2d70fa)

## 🧑‍🏭 기술스택
- Java 17
- Spring boot 3.3.1
- Docker, k3s
- MySQL
- Redis
- NCP Server, NCP storage, Cloud Log Analytics

## 🤝 협업

1. 이슈를 생성한다.
2. 이슈를 기반으로 브랜치를 생성한다.
   - ex: `feat/#3`
3. 브랜치를 생성한 후에 작업을 진행한다.
4. 진행한 후에 커밋을 한다.
5. 작업이 완료되면 PR을 생성한다.
6. PR을 생성한 후에 팀원들에게 리뷰를 요청한다. 리뷰는 PR 올린 시간 기준으로 24시간 내로는 2명의 승인, 그 이후로는 1명의 승인이 필요하다.
7. 리뷰를 받은 후에 PR을 default branch에 merge한다.
8. merge된 후, 배포를 진행한다.

## ᛘ Branch
`main branch` : 배포 서버 branch

`dev branch` : 개발 서버 branch

`feature branch`: 로컬 개발 branch

-   issue는 노션에 생성한다.
-   노션에 생성된 issue 번호를 기반으로 branch 생성
    -   ex) feat/#{노션이슈번호}
- dev 브랜치에 머지할 때는 브랜치 간소화 및 revert 용이성을 위해 스쿼시 머지를 진행한다. 

## 🙏 Commit Convention
- <a href="https://udacity.github.io/git-styleguide/">유다시티 컨벤션

```
feat: 새로운 기능 구현
add: 기능구현까지는 아니지만 새로운 파일이 추가된 경우
del: 기존 코드를 삭제한 경우
fix: 버그, 오류 해결
docs: README나 WIKI 등의 문서 작업
style: 코드가 아닌 스타일 변경을 하는 경우
refactor: 리팩토링 작업
test: 테스트 코드 추가, 테스트 코드 리팩토링
chore: 코드 수정, 내부 파일 수정
```

## 👨‍💻 Code Convention
> 💡 **동료들과 말투를 통일하기 위해 컨벤션을 지정합니다.**
> 오합지졸의 코드가 아닌, **한 사람이 짠 것같은 코드**를 작성하는 것이 추후 유지보수나 협업에서 도움이 됩니다. 내가 코드를 생각하면서 짤 수 있도록 해주는 룰이라고 생각해도 좋습니다!

### Code
- 하나의 메서드(method) 길이 12줄, 깊이(depth) 3 이내로 작성합니다.

### Entity
- id 자동 생성 전략은 IDENTITY를 사용합니다.
- @NoArgsConstructor 사용 시 access를 PROTECTED로 제한합니다.

### Enum
- Enum 값은 import static 호출로 사용합니다.

### DTO
- Controller에서 요청/응답하는 DTO와 Service에서 사용하는 DTO를 분리합니다.
    - Layered Architecture를 엄격하게 준수합니다.
    - 확장/번경에 용이하게 합니다.
    - 매개변수가 5개 미만일 경우 controller-service간 Dto를 사용하지 않습니다. 
- 네이밍은 아래와 같이 정의합니다.
    - Controller DTO: `${Entity명}${복수형일 경우 List 추가}${행위 또는 상태}${Request/Response}`
    - Service DTO: `${Entity명}${복수형일 경우 List 추가}${행위 또는 상태}Service${Request/Response}`

### Service, Repository
- DB를 호출하는 경우 메서드명에 save, find, update, delete 용어를 사용합니다.
- 비즈니스 로직일 경우 메서드명에 create, get, update, delete, 그 외 용어를 사용합니다.
- 복수형은 ${Entity명}s로 표현합니다.
- Service 파일이 비즈니스 로직 5개 이상으로 커지면 조회, 비조회(Transactional)로 클래스를 분리합니다.

<br/>
