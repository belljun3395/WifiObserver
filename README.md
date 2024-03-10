# Wifi Observer API

해당
프로젝트는 [2023-2 에코노베이션 프로젝트](https://github.com/belljun3395/WifiObserver/tree/legacy/econo-forest-iptime?tab=readme-ov-file), [2024-2 개인 리펙토링 프로젝트](https://github.com/belljun3395/WifiObserver/tree/legacy/obs-v1?tab=readme-ov-file)
를 기반으로 진행되었습니다.

## 프로젝트 소개

해당 프로젝트는 와이파이 공유기의 관리자 페이지에서 제공하는 연결 기기 정보를 기반으로 연결된 기기, 연결 시간울 관리하는 프로젝트입니다.

## 프로젝트 문서 목록/링크

- [Architecture](./ARCHITECTURE.md)
- [Api](./api/README.md)
- [Batch](./batch/README.md)
- [Batch/Job](./batch/JOBREADME.md)
- [Client/Router](./client/router/README.md)

## 샘플 페이지

[샘플 페이지 바로가기](https://wifiobserver.store/sample)

<img width="1728" alt="스크린샷 2024-03-10 오후 8 05 51" src="https://github.com/belljun3395/WifiObserver/assets/102807742/42d92f58-fd33-43b5-b3b8-0acbd47526b9">

### 샘플 페이지 활용하기

1. 샘플 페이지에 접속합니다.
2. 샘플 페이지에서 조회할 공유기 정보를 입력합니다.
3. 조회 버튼을 클릭합니다.

#### 공유기 정보 확인 방법(IPTIME)

IPTIME 공유기에 접속하여, [관리자 페이지](http://192.168.0.1/sess-bin/login_session.cgi)로 들어갑니다.

로그인 합니다. => **공유기 아이디/비밀번호 정보입니다.**

![A8BCBE1B-5DF6-4828-8FF1-9C848E7D3CD7](https://github.com/belljun3395/WifiObserver/assets/102807742/da532bad-0ff1-4cdc-bb5b-18f6461518eb)

**동적 IP 주소**를 확인합니다. => **공유기 주소 정보입니다.**

![647982FC-FDC2-42B3-85F3-52B1D60C97A1](https://github.com/belljun3395/WifiObserver/assets/102807742/a1e6f00b-277c-4173-9cca-69a5c6ae371c)

"고급 설정/보안 기능/공유기 접속/보안관리" 메뉴를 클릭합니다.

외부 접속 보안 아래 원격 관리 포트 사용을 클릭하고 적절한 **포트번호**(8080)을 입력합니다. => **공유기 포트 정보입니다.**

## 프로젝트 배포 주소

주소: https://wifiobserver.store

[swagger-ui](https://wifiobserver.store/swagger-ui/index.html)에 정의된 API를 사용할 수 있습니다.

## 프로젝트를 시작하는 방법

### 1. 프로젝트를 클론합니다.

```bash
git clone https://github.com/belljun3395/WifiObserver.git
```

### 2. 프로젝트를 위한 환경을 구성합니다.

```bash
cd scripts/

/bin/bash ./local-develop-env
```

### 3. 프로젝트를 빌드합니다.

```bash
./gradlew  :api:build
```

### 4. API 문서를 생성합니다.

[swagger-ui](https://wifiobserver.store/swagger-ui/index.html): 배포된 API 문서

```bash
./gradlew  :api:openapi3
```

해당 테스크를 실행하면 `api/src/main/resources/static` 디렉토리에 `openapi3.json` 파일이 생성됩니다.

### 5. 프로젝트를 실행합니다.

```bash
cd api/build/libs

java -jar -Duser.timezone=Asia/Seoul api-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```
