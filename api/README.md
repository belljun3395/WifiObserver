# API

## 목차

- [패키지 구조](#패키지-구조)
- [패키지 설명](#패키지-설명)
    - [config](#config)
    - [domain](#domain)
    - [domain/service](#domainservice)
    - [domain/service/xxx/support](#domainservicexxxsupport)
    - [domain/usecase](#domainusecase)
- [API 문서](#api-문서)

## 패키지 구조

```
api
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── observer
│   │   │           └── common
│   │   │           └── config
│   │   │           └── domain
│   │   │           └── security
│   │   │           └── web
│   │   │           └── AppMain.java
```

## 패키지 설명

- `AppMain.java`: Spring Boot Application 실행 클래스
- `config`: API 서버에서 사용하는 설정 클래스를 정의하기 위한 패키지
- `domain`: 도메인의 유즈케이스를 정의 및 구현하기 위한 패키지
- `security`: 인증 및 권한 관련 클래스를 정의하기 위한 패키지
- `web`: API 서버의 컨트롤러 및 필터를 정의하기 위한 패키지

### 엔티티 패키지 이동에 관하여

- `entity`: 도메인에서 사용하는 엔티티 클래스를 정의하기 위한 패키지
- `persistence`: 도메인에서 사용하는 엔티티를 조회 및 저장하기 위한 패키지

기존 위의 두 패키지를 `data` 모듈로 이동하였습니다.

이는 공통의 엔티티를 사용하는 경우를 위한 선택이었습니다.

해당 프로젝트의 경우 공통의 엔티티를 `batch` 모듈에서 사용하고 있습니다.

이를 중복해서 정의하는 것은 비효율적이라고 판단하여 별도의 `data` 모듈을 생성하여 공통의 엔티티를 정의하였습니다.

`api` 모듈에서는 이미 `domain/external/dao` 패키지에 별도의 인터페이스르 정의하여 엔티티를 사용하고 있어 패키지 이동에 따른 변화를 최소화 할 수 있었습니다.

변화를 최소화 할 수 있었다는 것에서 알 수 있듯 `api` 모듈 `data` 모듈에 대한 의존성이 완전히 분리되어 있지는 않습니다.

이는 `data` 모듈의 엔티티를 `api` 모듈에서 사용할 객체로 변환하는 과정에서 발생하는 리소스가 크다 생각하여 엔티티는 `api` 모듈에서 사용하도록 결정하였습니다.

### config

config 패키지의 클래스를 통해 API 서버에서 어떠한 설정을 하고 있는지 한눈에 알 수 있도록 구성하는 것을 목표로합니다.

- `APIXXXConfig`/`APIXXXConfiguration`/`APIXXXConfigurer`: API 서버에서 사용하는 설정 클래스를 의미합니다.
- `APIXXXProperties`: API 서버에서 사용하는 설정 값을 정의하는 클래스를 의미합니다.
- 라이브러리 및 외부 모듈를 위한 설정이 필요한 경우 이를 위한 클래스를 생성합니다.
- 설정 클래스에서 정의한 빈의 이름의 경우 `ApiAppConfig#BEAN_NAME_PREFIX`를 활용한 설정 클래스별 prefix를 만들어 정의합니다.

### domain

도메인 패키지에서 도메인별 유즈케이스를 정의 및 구현하는 것을 목표로합니다.

도메인 정의 및 구현하기 위한 패키지입니다.

- `dto`: 외부에서 특정 도메인에 대한 요청 및 응답을 위한 DTO 클래스를 정의하기 위한 패키지
- `external`: 다른 도메인 또는 외부 모듈/시스템을 이용하기 위한 클래스를 정의하기 위한 패키지
- `model`: 특정 도메인에서 사용하는 모델 클래스를 정의하기 위한 패키지
- `service`: 특정 도메인에서 사용하는 서비스 클래스를 정의하기 위한 패키지
- `usecase`: 특정 도메인에서 사용하는 유즈케이스 클래스를 정의하기 위한 패키지

##### domain/service

서비스에는 3가지 타입의 클래스가 존재합니다.

- `XXXQuery`: 여러 유즈케이스에서 사용하는 단순 쿼리를 정의하기 위한 클래스
- `XXXCommand`: 여러 유즈케이스에서 사용하는 단순 커멘드를 정의하기 위한 클래스
- `XXXService`: 단순 커리, 커멘드가 아닌 복잡한 비즈니스 로직을 정의하기 위한 클래스

**주의 사항**
`XXXCommand`, `XXXService`(일부)는 트랜잭션을 사용하는 클래스입니다.

이때 새로운 트랜잭션이 필요한 경우 해당 클래스를 인터페이스로 분리한 이후 `NTX`가 접두로 붙는 구현 클래스를 만들어 사용합니다.

해당 클래스들의 네이밍 규칙은 다음과 같습니다.

`XXXOOOType`

- `XXX`: 클래스의 행동을 의미합니다. ex) Calculate, Get, Save
- `OOO`: 행동의 대상을 의미합니다. ex) TryCount, EmailAuthHistory, Member
- `Type`: 클래스의 타입을 의미합니다. ex) Query, Command, Service

##### domain/service/xxx/support

xxx 도메인를 외부에서 활용할 수 있도록 지원하는 클래스를 정의하기 위한 패키지입니다.

- `XXXSupport`: 해당 도메인을 외부에서 활용할 수 있도록 해당 도메인이 제공하는 정보를 정의하기 위한 클래스

**주의 사항**

`domain/service/xxx/support`에 정의하는 서비스 클래스의 경우 해당 도메인의 정보를 외부에서 활용하기 위함입니다.

쿼리를 통한 조회는 가능하지만 커멘드를 통한 변경은 불가능합니다.

따라서 해당 클래스의 메서드는 읽기 전용으로 정의합니다.

##### domain/usecase

유즈케이스는 엔드포인트와 1:1로 매핑되는 클래스입니다.

데이터의 유효성을 제외한 비즈니스로직의 경우 모델을 만들어 처리합니다.

모델만을 통해 처리하지 못하는 비즈니스 로직의 경우 서비스를 만들어 처리합니다.

## API 문서

[API 문서 바로가기](https://wifiobserver.store/swagger-ui/index.html)

### 로컬에서 확인

API 문서의 경우 컨트롤러 테스트를 통해 자동으로 생성되는 문서를 사용합니다.

해당 문서를 확인하기 위해서는 아래의 테스크를 실행합니다.

```bash
./gradlew :api:openapi3
```

해당 테스크를 실행하면 `api/src/main/resources/static` 디렉토리에 `openapi3.json` 파일이 생성됩니다.
