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

## 4. 개발 담당 파트 및 주요 기능 소개 
##### ( * 펼쳐보시면 상세 내용을 확인하실 수 있습니다. )

![로그인](https://user-images.githubusercontent.com/93200574/185052178-770531f4-0ed5-4e4c-8c3f-1d6ac20853ec.gif)
### 소셜 로그인 및 로그아웃 API

<details>
<summary>구글,카카오,네이버 3가지 종류의 소셜로그인 구현</summary>
<div markdown="1">       

#### [application-oauth.yml🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/resources/application-oauth.yml)  

</div>
</details>


<details>
<summary>소셜사이트로부터 받아온 데이터를 저장시 Optional 활용 </summary>
<div markdown="1">       

#### [CustomOAuth2UserService🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/oauth/service/CustomOAuth2UserService.java)   
User가 null일 경우(=신규회원) DB에 등록되는 과정에서 Optional로 감싼 부분을 원래 `orElseThrow`로 던져 오류가 발생했어서  
첫 수정 시에는, `orElse(null)`로 받도록 처리했었으나 `null을 반환하지 않기 위해 Optional로 감싸는 건데 null을 반환하는 건 의미가 없다`고  
생각해서 null을 반환하지 않는 형식의 삼항연산자로 처리하였습니다.

![수정전](https://user-images.githubusercontent.com/93200574/185783092-86656ada-1970-46a2-9fb9-1968020df82b.png)
</div>
</details>

<details>
<summary>JWT Token을 백에서 쿠키에 저장</summary>
<div markdown="1">       

#### [OAuth2AuthenticationSuccessHandler🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/oauth/handler/OAuth2AuthenticationSuccessHandler.java), [CookieUtil🔗](https://github.com/nnakki/eventcafecloud/blob/2dea14bd587ac6b7a26268ed98c6c76556cd94b0/src/main/java/com/eventcafecloud/utils/CookieUtil.java)  

원래는 토큰을 생성하고 토큰을 포함한 프론트엔드의 URI를 반환하도록 설계되어있었으나,  
대부분의 페이지에 `thymeleaf`를 적용하면서 ssr방식으로 페이지를 반환하다보니 프론트의 uri에 토큰을 심는 게 적합하지 않았습니다.   
> `[방법1]` (사용한 방법)     
Handler에서 cookie에 토큰을 저장해줄 때, HttpOnly 세팅을 잡은 addCookie메소드와 HttpOnly(false) 세팅을 잡은 addCookie메소드를 각각 만들어주고,
리프레시 토큰과 액세스토큰을 각각의 방식으로 따로 저장 (리프레시 토큰은 Http에서 접근할 수 있어선 안되기 때문에 세팅을 바꿔주도록 한다.)  
`[방법2]`
> 1. Handler의 redirect경로를 바꿔서 소셜 로그인 후, queryParam의 형태로 전해진 token을 클라이언트가 볼 수 없는 경로로 전달.  
model.attribute를 통해 view로 토큰값을 넘겨준다.
> 2. JS에서 넘겨받은 토큰값을 쿠키에 저장해준다.(set.cookie)
> 3. ajax 콜을 서버로 보낼 때, Header에 token값을 넣어서 보내준다.  
SpringSecurity의 filter가 Header 토큰을 인식, JWT Token을 인증해줄 수 있도록 -> 이 방법 또한 AJAX를 사용하지 않는 파트가 많아 사용하지x

</div>
</details>



</br>  

</details>
</br>  


![프로필페이지](https://user-images.githubusercontent.com/93200574/185052202-eb1bcb0c-0afe-4971-bca0-69dd00d65d48.gif)
![카페관리](https://user-images.githubusercontent.com/93200574/185052231-dd817a1a-7eb2-4faf-a2fa-5e9b3836f7d8.gif)

### 프로필페이지 CRUD (프로필 수정, 내 북마크, 내 카페, 내 게시글, 카페 및 예약내역 관리 등) 및 페이징</b></summary>

<details>
<summary>프로필 수정</summary>
<div markdown="1">       

#### [profileController🔗](https://github.com/nnakki/eventcafecloud/blob/develop/src/main/java/com/eventcafecloud/user/controller/ProfileController.java), [UserService🔗](https://github.com/nnakki/eventcafecloud/blob/d7e9f086e8b8ad15ccc583e6b2c49eef460e7d9b/src/main/java/com/eventcafecloud/user/service/UserService.java) 

프로필 수정은 `고객 피드백`을 반영하여 `1. 닉네임 중복불가`, `2. 프로필사진, 닉네임 중 한쪽만 선택적으로 수정 가능` 로직을 추가했습니다. 
UserRequestDto에서 유효성 검사가 가능하도록 어노테이션을 추가하였고, 사진이나 닉네임은 원래 저장 되어 있던 정보를 불러와 넘겨줌으로서, 수정하지 않으면 원래 정보가 그대로 기입되도록 설계했습니다. 

</div>
</details>


<details>
<summary>내가 작성한 글, 카페, 이벤트 등 불러오기(페이징)</summary>
<div markdown="1">       

#### 람다식을 이용한 Dto 반환 페이징  
repository에서 Page<CafeBookmark>를 사용하여 북마크리스트를 전달받은 이후, 전달받은 리스트를 `.map`을 사용하여 Page<CafeBookmarkResponseDto>로 반환받았습니다. 
```java
@Data
public class CafeBookmarkResponseDto {

    private Long id;
    .
    .
    private Integer cafeBookmarkCount;

    public CafeBookmarkResponseDto(CafeBookmark bookmark) {
        id = bookmark.getId();
        cafeId = bookmark.getCafe().getId();
        cafeName = bookmark.getCafe().getCafeName();
        cafeInfo = bookmark.getCafe().getCafeInfo();
        cafeImgUrl = bookmark.getCafe().getCafeImages().get(0).getCafeImageUrl();
        cafeWeekdayPrice = bookmark.getCafe().getCafeWeekdayPrice();
        cafeReviewCount = bookmark.getCafe().getCafeReviews().size();
        cafeBookmarkCount = bookmark.getCafe().getCafeBookmarks().size();
    }
}
```

```java
@Transactional
public Page<CafeBookmarkResponseDto> findCafeBookmarkByUserId(Long userId, Pageable pageable) {
    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 6, Sort.Direction.DESC, "id");
    Page<CafeBookmark> result = cafeBookmarkRepository.findAllByUserId(userId, pageable);
    return result.map(CafeBookmarkResponseDto::new);
}
```
</div>
</details>


<details>
<summary>휴무일 등록 시, 일정이 있는 날짜엔 등록이 되지 않도록 처리(프론트) </summary>
<div markdown="1">       

1. 카페에 등록된 휴무일(Date)정보와, 일정(Schedule)정보를 모두 받아옵니다. 이 때, DB에는 시작일과 종료일만 등록되어있기 때문에  
사이 기간에 대한 모든 날짜를 받아올 수 있도록 함수를 구현. [CafeService🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/java/com/eventcafecloud/cafe/service/CafeService.java)
```java
final String DATE_PATTERN = "yyyy-MM-dd";
SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
ArrayList<String> dates = new ArrayList<>();

for (int i = 0; i < eventList.size(); i++) {
    String inputEventStartDate = eventList.get(i).getEventStartDate();
    String inputEventEndDate = eventList.get(i).getEventEndDate();

    Date startDate = sdf.parse(inputEventStartDate);
    Date endDate = sdf.parse(inputEventEndDate);
    Date currentEvent = startDate;

    while (currentEvent.compareTo(endDate) <= 0) {
        dates.add(sdf.format(currentEvent));
        Calendar c = Calendar.getInstance();
        c.setTime(currentEvent);
        c.add(Calendar.DAY_OF_MONTH, 1);
        currentEvent = c.getTime();
    }
}
```
2. 모든 날짜에 대한 정보를 Date 리스트에 담고, View로 전달해줍니다. [hostProfileController🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/java/com/eventcafecloud/user/controller/hostProfileController.java)
```java
@GetMapping("/{id}/cafes/{cafeId}/schedule")
 public String getReservationByCafe(@PageableDefault Pageable pageable, @PathVariable Long cafeId, Model model, User loginUser) throws ParseException {
     Page<Event> eventList = eventService.findEventListByCafe(cafeId, pageable);
     Page<CafeSchedule> scheduleList = cafeScheduleService.findCafeScheduleByCafeId(cafeId, pageable);
     ArrayList<String> dates = cafeService.AllReservationListByCafe(cafeId);
     .
     .
     //휴무일등록시, 등록 정보를 받아올 객체를 넘김
     model.addAttribute("requestDto", new CafeScheduleRequestDto());

     return "profile-host/host-schedule";
 }
```
3. View의 JS에서는 해당 날짜의 리스트를 받고, Full Calendar API가 인식할 수 있는 형태로 바꾸어준뒤  
FullCalendar의 DisableDate에 넣어줍니다. [host-schedule.html 🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/resources/templates/profile-host/host-schedule.html)
```js
let dates = [[${dates}]];

$.datepicker.setDefaults({
            dateFormat: 'yy-mm-dd',
            .
            .
            beforeShowDay: disableSomeDay
        });

        // 제외할 날짜 (받아오기) - 휴무일, 예약된 날짜
        let disabledDays = dates;
        for (let index = 0; index < disabledDays.length; index++) {
            disabledDays[index] = disabledDays[index].replace(/-0+/g, '-')
        }

        // 날짜 선택을 막기위한 함수
        function disableSomeDay(date) {
            let dates = date.getDate();
            let month = date.getMonth();
            let year = date.getFullYear();

            for (let i = 0; i < disabledDays.length; i++) {
                if ($.inArray(year + '-' + (month + 1) + '-' + dates, disabledDays) !== -1) {
                    return [false];
                }
            }
            return [true];
        }
```
</div>
</details>

 
</br> 

![어드민페이지](https://user-images.githubusercontent.com/93200574/185052242-2b71839f-3941-4dee-b078-a26c1d4cf3a0.gif)

### 관리자페이지 CRUD, 페이징 및 필터, 스프링 메일인증 API

<details>
<summary>필터 처리</summary>
<div markdown="1">       

#### [AdminController🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/java/com/eventcafecloud/user/controller/AdminController.java)
```java
public Page<UserResponseDto> findAllUserList(RoleType roleType, Pageable pageable) {

    Page<User> userList;

    int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
    pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");

    if (roleType == null) {
        userList = userRepository.findAll(pageable);
    } else {
        userList = userRepository.findAllByRole(roleType, pageable);
    }

    return userList.map(UserResponseDto::new);
}
```
```java
@GetMapping("/users")
public String getUserList(@PageableDefault Pageable pageable, @RequestParam(required = false, value = "roleType") RoleType roleType, Model model) {
    Page<UserResponseDto> userList = userService.findAllUserList(roleType, pageable);
    model.addAttribute("users", userList);
    model.addAttribute("userRequestDto", new UserRequestDto());
    return "admin/admin-user";
}
```

</div>
</details>


<details>
<summary>호스트 승인 로직+스프링 메일 인증</summary>
<div markdown="1">       

</br>

> 이 프로젝트는 이벤트를 등록하고자 하는 일반 유저와, 카페를 등록하는 호스트(사장) 유저로 권한이 구분됩니다.    
따라서 일반 유저로 가입한 이후, 자신이 호스트라면 `호스트로 등록하는 과정`이 필요하다고 생각했습니다. 


1. 사용자가 사업자등록탭을 눌러 사용자 등록을 하게 되면 `어드민페이지`의 `호스트승인`탭에 추가됩니다. [UserService🔗](https://github.com/nnakki/eventcafecloud/blob/d7e9f086e8b8ad15ccc583e6b2c49eef460e7d9b/src/main/java/com/eventcafecloud/user/service/UserService.java)
```java
@Transactional
   public void saveHostUser(HostUserCreateRequestDto hostUserCreateRequestDto) {

       User user = userRepository.findByUserEmail(hostUserCreateRequestDto.getUserEmail())
               .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND.getMessage()));

       Optional<HostUser> checkUser = hostUserRepository.findByUserEmail(hostUserCreateRequestDto.getUserEmail());

       if (checkUser.isEmpty()) {
           HostUser hostUser = HostUser.builder()
                   .userEmail(hostUserCreateRequestDto.getUserEmail())
                   .certificationFile(hostUserCreateRequestDto.getCertificationFile())
                   .isApprove(ApproveType.WAITING)
                   .build();

           user.registHost(hostUser);
       } else {
           checkUser.get().updateIsApprove();
       }
   }
```
2. 호스트 승인 탭에 추가 된 사용자는 승인/거절에 따라 호스트 유저로 권한이 변경되고, 결과가 가입한 사용자의 메일로 결과가 전송됩니다. [MailService🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/java/com/eventcafecloud/mail/MailService.java)    
```JAVA
 @PostMapping("/hosts/{id}/pass")
    public String updateNormalUserToHostUser(@PathVariable Long id) {
        userService.modifyNormalUserToHostUser(id);
        String userEmail = userService.getUserEmailById(id);

        MailTO mail = new MailTO();
        mail.setAddress(userEmail);
        mail.setTitle("EC2 호스트 승인메일입니다.");
        mail.setMessage("호스트로 승인 되셨습니다. 지금 EC2에 접속해 카페를 등록해보세요!\n www.eventcafecloud.com");
        mailService.sendMail(mail);

        return "redirect:/admin/hosts";
    }

    @PostMapping("/hosts/{id}/fail")
    public String updateApproveIsFail(@PathVariable Long id) {
        userService.approveIsFail(id);
        String userEmail = userService.getHostUserEmailById(id);

        MailTO mail = new MailTO();
        mail.setAddress(userEmail);
        mail.setTitle("EC2 호스트 승인메일입니다.");
        mail.setMessage("호스트로 승인이 거절되었습니다. 사업자 등록증을 다시 확인하신 뒤 신청해주세요.\n www.eventcafecloud.com");
        mailService.sendMail(mail);

        return "redirect:/admin/hosts";
    }
}
``` 

#### [AdminController🔗](https://github.com/nnakki/eventcafecloud/blob/1278081d6001056e19cfa08c406aba4c1057ebc9/src/main/java/com/eventcafecloud/user/controller/AdminController.java)


</div>
</details>



</br>

## 5. 핵심 트러블 슈팅 

### 5-1. Jwt토큰 사용 시, 토큰을 언제 쿠키에 심어줄 수 있는가?
- 보안의 강화를 위해 소셜 로그인 시, Jwt토큰을 사용하여 사용자의 정보를 주고받을 수 있도록 개발했습니다.
  
- 대체적으로 쿠키에 토큰을 심는 일은 프론트에서 이루어집니다. 하지만 Thymeleaf를 사용한 저희 프로젝트의 특성상 SSR방식으로 한번에 데이터가 전달이 되어 ajax 처럼 url을 통해 데이터를 주고 받지 않는 기능이 많아 url로 토큰을 전달하는 방식에 어려움이 있었습니다. 
  
- 때문에 로그인 시, 해당 과정을 백엔드에서 처리할 수 있도록 코드를 수정했습니다. 
  
- [상세설명](https://velog.io/@nnakki/SpringSecurityJWTOAuth2를-사용한-소셜로그인)  

</br>  
  
### 5-2. 페이징 시, 다대일 관계에서 지연로딩 이후 초기화가 안되는 문제
- **북마크 레포지토리를 통해 북마크에 저장 된 카페 정보를 불러오는 과정에서, 카페정보가 모두 null이 되는 오류가 발생했습니다.**

- FetchType이 Lazy가 아니면 제대로 값이 들어오는 걸 확인했고, 지연로딩 이후 초기화가 안되는 문제임을 확인했습니다.

- 영속성 컨텍스트의 생존범위에 대한 문제로, 북마크에 해당하는 도메인을 바로 반환하지 않고 Dto로 변환한 이후 반환하는 것으로 문제를 해결했습니다.  

- [상세설명](https://velog.io/@nnakki/페이징시-다대일-관계에서-지연로딩-이후-초기화가-안되는-이유) 

</br>  

### 5-3. 프론트와 백의 역할 분리
- `이벤트의 시작일이 7일이내면 취소가 불가능하게 처리`하는 로직을 아래와 같이 구현하고자 했습니다. 
```
1. EventDB에 CancelAvail이라는 취소가 가능한 상태인지 점검해주는 컬럼 추가
2. 백엔드에서, 이벤트 시작일과 현재일을 비교하여 7일 이내라면 DB의 CancelAvail값을 false로 업데이트
3. 프론트엔드에서 cancelAvail의 컬럼값을 비교하여, true면 버튼을 활성화하고, false면 버튼을 비활성화
```
- 🔻문제점 
```
1. 버튼이 보이고 안보이고는 온전히 프론트엔드의 영역. 백엔드에서부터 DB에 접근하여 컬럼을 건드리는 건 매우 비효율적
   (DB에 잦은 접근은 어플리케이션의 성능을 가장 크게 떨어뜨리는 요인)
2. cancelAvail이라는 `컬럼`은 db의 정보를 통해 정보를 수정하는 컬럼으로 쓸데없이 db에 접근만 늘리는 것으로 추가할 필요가 없다. 
3. 위의 방식은 url을 타고 직접 api에 접근했을 때 수정이나, 삭제를 막아주는 역할을 하지 못한다. 
   즉, 근본적으로 백엔드에 관한 처리가 되어있지 않다.
```
- 프론트와 백의 역할 자체를 혼동하고 있다는 걸 깨달았고, 프론트, 백엔드에서의 처리를 구분하는 것으로 문제를 해결했습니다.
```
--백엔드--
1. Service클래스에 StartDate와 현재날짜를 비교하여, true/false를 리턴하는 메소드 추가 
2. delete를 구현하는 Api controller에 생성한 메소드를 추가하여, 
   메소드가 true일 때만 삭제 로직을 수행하고, false면 삭제 로직을 수행하지 않음
   (DB에 접근할 필요가 X, 효율적으로 삭제처리를 막을 수 있다.)

--프론트엔드--
1. Event엔티티를 받아서 매핑해줄 EventResponseDto를 생성
2. EventResponseDto에 CancelAvail이라는 변수 추가 
   (DB에 컬럼을 추가한 게 아니라, Dto에만 컬럼추가. DB의 StartDate에 관한 정보만을 받아서
   생성자에서 해당 정보로 CancelAvail에 대한 결과를 리턴해주는 메소드를 따로 만들어서, 
   값을 넣어줌  -> DB에는 없지만, 프론트엔드쪽으로는 cancleAvail에 관련된 값이 넘어감)
3. if문을 사용하여 해당 변수의 값이 true면 버튼 활성화, false면 버튼 비활성화
```

<details>
<summary>상세설명(코드보기)</summary>
<div markdown="1">      

#### 백엔드 [EventService🔗](https://github.com/nnakki/eventcafecloud/blob/df05bef524e1dd565d582ea5309c676bc66b5634/src/main/java/com/eventcafecloud/event/service/EventService.java), [EventController🔗](https://github.com/nnakki/eventcafecloud/blob/df05bef524e1dd565d582ea5309c676bc66b5634/src/main/java/com/eventcafecloud/event/controller/EventController.java) 
```java
/**
 * 이벤트기간과 오늘을 비교해서 취소상태를 변경하는 메소드
 */
public boolean isEventCancleAvail(Long id) {
    Date now;
    Calendar cal = java.util.Calendar.getInstance();
    cal.add(Calendar.DATE, +7);
    now = cal.getTime();

    Event event = eventRepository.getById(id);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String deadline = sdf.format(now);

    return event.getEventStartDate().compareTo(deadline) > 0;
  }
}
```

```java
//이벤트삭제(마이페이지)
    @DeleteMapping("/profile/{userId}/delete/{eventNumber}")
    public String deleteEventFromProfile(@PathVariable Long eventNumber, @PathVariable Long userId) {
        boolean result = eventService.isEventCancleAvail(eventNumber);
        if (result == true) {
            eventService.removeEvent(eventNumber);
        } else {
            return "redirect:/users/profile/" + userId + "/reservation";
        }
        return "redirect:/users/profile/" + userId + "/reservation";
    }

```

#### 프론트엔드 [reservation.html🔗](https://github.com/nnakki/eventcafecloud/blob/472a1d1191a6b05daf457b58a5ceb4ebc32269e5/src/main/resources/templates/profile/fragments/reservation.html)
```html
<form id="delete-form" method="post"
      th:action="@{'/profile/'+${userId}+'/delete/'+${event.id}}">
    <input name="_method" type="hidden" value="DELETE"/>
    <button class="delete-button" onclick="return confirm(this.getAttribute('data-confirm-delete'))"
            th:if="${event.isCancel==true}"
            th:data-confirm-delete="|정말 취소하시겠습니까?|">취소
    </button>
</form>
```

</div>
</details>



</br>  

 
</br>

## 6. 그 외 프로젝트를 진행하며 경험하고 체득한 내용
  
#### [🔗 AOP를 사용한 코드 리팩토링 - 스프링 시큐리티 로그인 성능개선](https://velog.io/@nnakki/AOP를-사용한-코드-리팩토링-HandlerMethodArgumentResolver)  
#### [🔗 JavaBean 패턴을 적용한 코드 리팩토링](https://velog.io/@nnakki/프로젝트-코드-리팩토링-적용-사례-effective-java)  
#### [🔗 프로젝트 기획 및 관리, 협업 방법](https://velog.io/@nnakki/프로젝트-관리-과정)  
#### [🔗 글로벌 예외처리 적용하기](https://velog.io/@nnakki/글로벌-예외처리-적용하기) 
#### [🔗Dto의 반환위치? Controller vs Service](https://velog.io/@nnakki/Dto의-반환위치-Controller-vs-Service)  

</br>
 
  
