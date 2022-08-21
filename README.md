# ☕ | Event Cafe Cloud 
- 기간 :  2022.06.24 - 2022.07.28  
- 프로젝트 인원 :  4명 

</br>

> Event Cafe Cloud는 새로운 문화현상으로 급부상한 [이벤트카페]를 대상으로  
**카페사장님과 고객을 연결하는 <U>✨카페대여 중개플랫폼✨</U> 입니다.**  

</br>


## 1. 사용기술
#### `Back-end`    
Java11, SpringBoot(2.6.9), MySQL, SpringDataJPA, SpringSecurity, Gradle  
#### `Cloud`    
AWS S3 Docker, EC2, RDS, ECR, Route53

</br>

## 2. [ERD설계]()

![image](https://user-images.githubusercontent.com/93200574/184498197-a78eead7-570a-4ca5-bbb1-33f2e45bc1b9.png)

</br>

## 3. 아키텍쳐
<div align="center">

![image](https://user-images.githubusercontent.com/93200574/184498136-c0ea2482-b5ad-4bc8-af8f-fac04c354ce1.png)

</div>

</br>

## 4. 개발 담당 파트 및 기능 소개 
##### ( * 펼쳐보시면 상세 내용을 확인하실 수 있습니다. )

![로그인](https://user-images.githubusercontent.com/93200574/185052178-770531f4-0ed5-4e4c-8c3f-1d6ac20853ec.gif)
<details>
<summary><b>소셜 로그인 및 로그아웃 API</b></summary>
<div markdown="1">

[application-oauth.yml🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/resources/application-oauth.yml)  
- 구글,카카오,네이버 3가지 종류의 소셜로그인 구현

[CustomOAuth2UserService🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/oauth/service/CustomOAuth2UserService.java)  

![수정전](https://user-images.githubusercontent.com/93200574/185783092-86656ada-1970-46a2-9fb9-1968020df82b.png)

- 소셜사이트로부터 받아온 데이터를 저장,  
User가 null일 경우(=신규회원) DB에 등록되는 과정에서 Optional로 감싼 부분을 원래 `orElseThrow`로 던져 오류가 발생했어서  
첫 수정 시에는, `orElse(null)`로 받도록 처리했었으나 `null을 반환하지 않기 위해 Optional로 감싸는 건데 null을 반환하는 건 의미가 없다`고  
생각해서 null을 반환하지 않는 형식의 삼항연산자로 처리하였습니다.

[OAuth2AuthenticationSuccessHandler🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/oauth/handler/OAuth2AuthenticationSuccessHandler.java)  
- 원래는 토큰을 생성하고 토큰을 포함한 프론트엔드의 URI를 반환하도록 설계되어있었으나,  
대부분의 페이지에 `thymeleaf`를 적용하면서 ssr방식으로 페이지를 반환하다보니 프론트의 uri에 토큰을 심는 게 적합하지 않았습니다. 

[CookieUtil🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/utils/CookieUtil.java)  
![image](https://user-images.githubusercontent.com/93200574/185788566-abe9d3fd-6aa4-440a-80a5-6fbb23ecaca0.png)  
 URI의 토큰을 프론트에서 쿠키에 저장하는 방식에서 -> 백에서 토큰을 쿠키에 저장하는 방식으로 코드를 수정했습니다. (방법1 사용)

---

</br>  

</details>
</br>  


![프로필페이지](https://user-images.githubusercontent.com/93200574/185052202-eb1bcb0c-0afe-4971-bca0-69dd00d65d48.gif)
<details>
<summary><b>프로필페이지 CRUD (프로필 수정, 내 북마크, 내 카페, 내 게시글 등) 및 페이징</b></summary>
<div markdown="1">

[profileController🔗](https://github.com/nnakki/eventcafecloud/blob/develop/src/main/java/com/eventcafecloud/user/controller/ProfileController.java)  
[hostProfileController🔗](https://github.com/nnakki/eventcafecloud/blob/d7e9f086e8b8ad15ccc583e6b2c49eef460e7d9b/src/main/java/com/eventcafecloud/user/controller/hostProfileController.java)  
[UserRequestDto](https://github.com/nnakki/eventcafecloud/blob/d7e9f086e8b8ad15ccc583e6b2c49eef460e7d9b/src/main/java/com/eventcafecloud/user/dto/UserRequestDto.java)
[UserService🔗](https://github.com/nnakki/eventcafecloud/blob/d7e9f086e8b8ad15ccc583e6b2c49eef460e7d9b/src/main/java/com/eventcafecloud/user/service/UserService.java)  
#### 프로필 수정
프로필 수정은 `고객 피드백`을 반영하여 `1. 닉네임 중복불가`, `2. 프로필사진, 닉네임 중 한쪽만 선택적으로 수정 가능` 로직을 추가했습니다. 
UserRequestDto에서 유효성 검사가 가능하도록 어노테이션을 추가하였고, 사진이나 닉네임은 원래 저장 되어 있던 정보를 불러와 넘겨줌으로서, 수정하지 않으면 원래 정보가 그대로 기입되도록 설계했습니다. 

#### 내가 작성한 글, 카페, 이벤트 등 불러오기
Page객체로 바로 불러올 수 있도록 설계했습니다.  
```JAVA
//UserID로 게시글 조회
  @Transactional(readOnly = true)
  public Page<Post> findPostListByUser(Long userId, Pageable pageable) {
      int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
      pageable = PageRequest.of(page, 5, Sort.Direction.DESC, "id");

      return postRepository.findAllByUserId(userId, pageable);
  }
```
---

</br>  

</details>
 
</br> 

![카페관리](https://user-images.githubusercontent.com/93200574/185052231-dd817a1a-7eb2-4faf-a2fa-5e9b3836f7d8.gif)

<details>
<summary><b>카페관리페이지 CRUD (예약 내역 및 휴무일 조회 및 삭제) 및 페이징</b></summary>
<div markdown="1">

[hostProfileController🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/user/controller/hostProfileController.java)

---

</br>  

</details>
</br>  

![어드민페이지](https://user-images.githubusercontent.com/93200574/185052242-2b71839f-3941-4dee-b078-a26c1d4cf3a0.gif)

<details>
<summary><b>관리자페이지 CRUD, 페이징 및 필터, 스프링 메일인증 API</b></summary>
<div markdown="1">

[AdminController🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/user/controller/AdminController.java)

---

</br>  
  
</details>


</br>

## 5. 핵심 트러블 슈팅 

### 5-1. Jwt토큰 사용 시, 토큰을 언제 쿠키에 심어줄 수 있는가?
- 보안의 강화를 위해 소셜 로그인 시, Jwt토큰을 사용하여 사용자의 정보를 주고받을 수 있도록 개발했습니다.
  
- 대체적으로 쿠키에 토큰을 심는 일은 프론트에서 이루어집니다. 하지만 Thymeleaf를 사용한 저희 프로젝트의 특성상 SSR방식으로 한번에 데이터가 전달이 되어 ajax 처럼 url을 통해 데이터를 주고 받지 않는 기능이 많아 url로 토큰을 전달하는 방식에 어려움이 있었습니다. 
  
- 때문에 로그인 시, 해당 과정을 백엔드에서 처리할 수 있도록 코드를 수정했습니다. 
  
[🔗 SpringSecurity+JWT+OAuth2를 사용한 소셜로그인](https://velog.io/@nnakki/SpringSecurityJWTOAuth2를-사용한-소셜로그인)  

</br>  
  
### 5-2. 페이징 시, 다대일 관계에서 지연로딩 이후 초기화가 안되는 문제
[🔗 페이징 시, 다대일 관계에서 지연로딩 이후 초기화가 안되는 이유](https://velog.io/@nnakki/페이징시-다대일-관계에서-지연로딩-이후-초기화가-안되는-이유) 

</br>  

### 5-3. 프론트와 백의 역할 분리
[🔗 Dto의 반환위치? Controller vs Service](https://velog.io/@nnakki/Dto의-반환위치-Controller-vs-Service)  

</br>  

 
</br>

## 6. 그 외 프로젝트를 진행하며 경험하고 체득한 내용
  
#### [🔗 AOP를 사용한 코드 리팩토링 - 스프링 시큐리티 로그인 성능개선](https://velog.io/@nnakki/AOP를-사용한-코드-리팩토링-HandlerMethodArgumentResolver)  
#### [🔗 JavaBean 패턴을 적용한 코드 리팩토링](https://velog.io/@nnakki/프로젝트-코드-리팩토링-적용-사례-effective-java)  
#### [🔗 SpringBoot에서 테스트 코드를 작성하기](https://velog.io/@nnakki/SpringBoot에서-테스트코드를-작성하자)  
#### [🔗 프로젝트 기획 및 관리, 협업 방법](https://velog.io/@nnakki/프로젝트-관리-과정)  
#### [🔗 글로벌 예외처리 적용하기](https://velog.io/@nnakki/글로벌-예외처리-적용하기)  

</br>
 
  
