# Client/Router

## 목차

- [패키지 구조](#패키지-구조)
- [패키지 설명](#패키지-설명)
    - [http](#http)
    - [service](#service)
    - [support](#support)

## 패키지 구조

```
client
├── router
│   ├── config
│   ├── exception
│   ├── http
│   ├── service
│   └── support
```

## 패키지 설명

- `config`: client/router 모듈 설정을 위한 패키지
- `exception`: client/router 모듈에서 발생하는 예외를 정의한 패키지
- `http`: http 통신을 위한 client 정의 패키지
- `service`: router 서비스를 위한 패키지
- `support`: router 서비스를 위한 지원 패키지

### http

#### http 하위 패키지 구조

```
http
├── client
│   ├── iptime
│   ├── AuthClient.java
│   ├── HealthClient.java
│   └── UsersClient.java
├── dto
│   ├── generator
│   └── http
```

해당 패키지는 라우터를 http 통신을 통해 사용하기 위한 패키지이다.

해당 서비스에서 라우터와 통신이 필요한 부분은 아래의 3가지다.

- 인증
- 사용자
- 헬스체크

해당 패키지에서는 위의 기능을 아래와 같이 인터페이스를 정의하였습니다.

- `AuthClient`: 인증을 위한 인터페이스
- `UsersClient`: 사용자를 위한 인터페이스
- `HealthClient`: 헬스체크를 위한 인터페이스

#### http 통신 구현체

현재 프로젝트는 `Jsoup`, `Feign`을 사용하여 http 통신을 구현하였습니다.

`Jsoup`은 html 파싱에 장점이 있는 라이브러리입니다.

해당 프로젝트는 와이파이 공유기의 관리자 페이지의 접속 기기 목록을 가져오기 때문에 html 파싱이 필요였고 이에 `Jsoup`이 적합하다 판단하였습니다.

따라서 html 파싱이 필요한 경우 `Jsoup`을 그렇지 않은 경우 `Feign`을 사용하여 통신을 하도록 구현하였습니다.

### service

해당 패키지는 http 패키지에서 정의한 client를 사용하여 서비스를 제공하는 패키지입니다.

#### 예시: RouterAuthService

```html

<html>
<head></head>
<body>
<form name="form" method="GET" action="login.cgi" onsubmit=""></form>
<script>
  // <!--
  functio
  n
  deleteOldCookie(path)
  {
    document.cookie = 'efm_session_id=; Path=/' + path + '; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
  }
  deleteOldCookie('');
  deleteOldCookie('sess-bin');

  function setCookie(session_id) {
    document.cookie = "efm_session_id=" + session_id + '; path=/';
  }

  setCookie('2Lr3BFTO4DCFeGQF');
  document.form.submit();
  // -->
</script>
</body>
</html>
```

위의 코드는 `Jsoup`으로 구현한 `AuthClient`를 사용하여 http 통신을 수행한 결과입니다.

결과를 통해 알 수 있듯 http 구현체만으로는 사용자가 필요한 정보를 추출하기 어렵습

`RouterAuthService`에서는 해당 결과를 바탕으로 이후 과정에서 필요한 인증 정보를 추출하여 반환하도록 구현하였습니다.

### support

해당 패키지는 외부에서 서비스를 사용하기 위한 지원 패키지입니다.

#### support/dto

해당 패키지는 외부에서 서비스를 사용하기 위한 dto를 정의한 패키지입니다.

request/response에 대해 BasicWifiServiceRequest, BasicWifiServiceResponse라는 이름의 추상 클래스를 상속받아 구현하여야 합니다.
