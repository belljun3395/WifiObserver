## 1차 API 설명

### 아키텍처
![Group 1](https://user-images.githubusercontent.com/102807742/210286789-df2c8fc9-8668-48cd-8555-8092a4aebe0c.png)

현재 아키텍처의 모습은 위와 같다.

클라이언트가 요청을 하면 서버에서 iptime에게 요청하여 현제 접속하고 있는 macAddress 리스트를 반환받는다.

이후 서버에서 mySql에서 등록되어 있는 회원들의 macAddress 리스트를 조회하여 iptime에서 받은 값과 비교하여 일치하는 값만 클라이언트에게 제공한다.

### Iptime에서 리스트 조회
```java
    @Override
    public List<String> getMacAddressesList() throws IOException {
        String cookieValue = iptime.getCookieValue();
        iptime.login(cookieValue);
        return iptime.getList(cookieValue);
    }
```
위는 Iptime에서 리스트를 가져오는 Service의 구현 코드이다.

<img width="977" alt="IptimeCookieConfig" src="https://user-images.githubusercontent.com/102807742/210257545-66e0f957-7eb9-4f9a-ad95-dface0fc8b6e.png">
위는 Iptime에서 로그인에 필요한 cookie의 설정이다.

위의 사진에서 확인할 수 있듯 cookie가 session cookie로 만료시간이 따로 없고 세션이 만료되면 삭제된다.

즉, 세션을 유지할 수 있다면 cookie를 위의 코드처럼 매번 유지할 필요가 없다.
그리고 로그인 역시 매번 다시할 필요가 없다.

따라서 2차 구현에서는 우선 위의 부분에 대한 수정을 진행할 것이다.