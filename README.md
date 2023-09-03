# Wifi Observer

## 프로젝트 소개

Wifi Observer 프로젝트는 2022-2에 진행하였던 [에코노 프로젝트](https://github.com/JNU-econovation/econo-forest-be-iptime)를 기반으로 하여, 더욱 발전시킨 프로젝트입니다.

## 프로젝트 주요 코드

프로젝트 주요 코드는 다음과 같습니다.

- [Client 모듈](https://github.com/belljun3395/WifiObserver/pull/1)
- [Batch 모듈](https://github.com/belljun3395/WifiObserver/pull/2)

---
## 프로젝트 설치 방법

### 1. 프로젝트 다운로드

````
git clone https://github.com/belljun3395/WifiObserver.git
````

### 2. 프로젝트 환경 구성 스크립트 실행

도커가 설치되어 있어야 합니다.

```
cd scripts/

./wifiobs-develop-env-reset
```
---

## 프로젝트 API 문서

[POSTMAN API 문서](https://documenter.getpostman.com/view/17873656/2s9Y5eLe3A)

---
## 프로젝트 베타 페이지

프로젝트 베타 페이지 [바로가기](http://api.wifiobs.store:8080/beta/iptime)

---
### 베타 페이지 사용법
<br>

#### 1. IPTIME 공유기에 접속

IPTIME 공유기에 접속하여, [관리자 페이지](http://192.168.0.1/sess-bin/login_session.cgi)로 들어갑니다.

로그인 합니다.

![A8BCBE1B-5DF6-4828-8FF1-9C848E7D3CD7](https://github.com/belljun3395/WifiObserver/assets/102807742/da532bad-0ff1-4cdc-bb5b-18f6461518eb)

동적 IP 주소를 확인합니다.

![647982FC-FDC2-42B3-85F3-52B1D60C97A1](https://github.com/belljun3395/WifiObserver/assets/102807742/a1e6f00b-277c-4173-9cca-69a5c6ae371c)

"고급 설정 / 보안 기능 / 공유기 접속/보안관리" 메뉴를 클릭합니다.

외부 접속 보안 아래 원격 관리 포트 사용을 클릭하고 적절한 포트번호(8080)을 입력합니다.

#### 2. 베타 페이지 접속

동적 IP 주소, 포트, 로그인 정보를 입력하여 와이파이 조회 버튼을 클릭합니다.

#### 3. 와이파이 조회

조회 결과를 확인합니다.
