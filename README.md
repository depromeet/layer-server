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
- CI/CD 구축<br>
</td>
<td>
- 프로젝트 세팅
</td>
<td>
- 인증/인가 필터 구현
</td>
</tr>
	</table>
</div>

<br>
<br>

## 🤝 협업

1. 이슈를 생성한다.
2. 이슈를 기반으로 브랜치를 생성한다.
   - ex: `feat/#3`
3. 브랜치를 생성한 후에 작업을 진행한다.
4. 진행한 후에 커밋을 한다.
5. 작업이 완료되면 PR을 생성한다.
6. PR을 생성한 후에 팀원들에게 리뷰를 요청한다. 리뷰는 나머지 2명 모두에게 요청하되, 1명의 승인을 받으면 머지 가능한 상태가 된다.
7. 리뷰를 받은 후에 PR을 default branch에 merge한다.
8. merge된 후, 배포를 진행한다.

## ᛘ Branch
`main branch` : 배포 서버 branch

`dev branch` : 개발 서버 branch

`feature branch`: 로컬 개발 branch

-   issue는 노션에 생성한다.
-   노션에 생성된 issue 번호를 기반으로 branch 생성
    -   ex) feat/#{노션이슈번호}

## 🙏 Commit Convention
- <a href="https://udacity.github.io/git-styleguide/">유다시티 컨벤션

```
feat: 새로운 기능 구현
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
