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

<details>
<summary><b>소셜 로그인 및 로그아웃 API</b></summary>
<div markdown="1">

</details>

<details>
<summary><b>프로필페이지 CRUD (프로필 수정, 내 북마크, 내 카페, 내 게시글 등) 및 페이징</b></summary>
<div markdown="1">

</details>

<details>
<summary><b>카페관리페이지 CRUD (예약 내역 및 휴무일 조회 및 삭제) 및 페이징</b></summary>
<div markdown="1">

</details>


<details>
<summary><b>관리자페이지 CRUD, 페이징 및 필터, 스프링 메일인증 API</b></summary>
<div markdown="1">

</details>


</br>

## 5. 핵심 트러블 슈팅 

### 5-1. Jwt토큰 사용 시, 토큰을 언제 쿠키에 심어줄 수 있는가?
- 보안의 강화를 위해 소셜 로그인 시, Jwt토큰을 사용하여 사용자의 정보를 주고받을 수 있도록 개발했습니다.
  
- 대체적으로 쿠키에 토큰을 심는 일은 프론트에서 이루어집니다. 하지만 Thymeleaf를 사용한 저희 프로젝트의 특성상   
  SSR방식으로 한번에 데이터가 전달이 되어 ajax 처럼 url을 통해 데이터를 주고 받지 않는 기능이 많아 url로 토큰을  
  전달하는 방식에 어려움이 있었습니다. 
  
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
 
  
