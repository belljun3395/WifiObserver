## 2차 API 설명

### Iptime에서 리스트 조회
```java
    @Override
    public List<String> getMacAddressesList() throws IOException {
        String cookieValue = iptime.getCookieValue();
        iptime.login(cookieValue);
        return iptime.getList(cookieValue);
    }
```
```java
@Service
@RequiredArgsConstructor
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;
    private String cookieValue;

    @Autowired
    public IptimeServiceImpl(Iptime iptime) throws IOException {
        this.iptime = iptime;
        this.cookieValue = iptime.getCookieValue();
    }

    @Override
    public List<String> getMacAddressesList() throws IOException {
        List<String> list = iptime.getList(cookieValue);
        if (!list.isEmpty()) {
            return list;
        }
        this.cookieValue = iptime.getCookieValue();
        return iptime.getList(cookieValue);
    }

}
```

위는 Iptime에서 리스트를 가져오는 기존 Service의 구현 코드와 변경된 Service의 구현 코드이다.

로그인을 하는 과정을 생략하였고

cookie를 IptimeServiceImpl이 생성되는 시점에 가져온다.

이는 해당 쿠키가 세션을 유지하면되고 만료가 없는 세션 쿠키이기 때문에 위와 같은 결정을 할 수 있었다.


```java
    private String cookieValue;

    if (!list.isEmpty()) {
        return list;
    }
    this.cookieValue = iptime.getCookieValue();
    return iptime.getList(cookieValue);

```
그렇다면 세션을 유지 못하고 쿠키를 갱신해야하는 경우가 생길 수 있다.

쿠키 값 갱신을 위해 final을 cookieValue에 선언하지 않았다.

그리고 cookie 값이 잘못 입력될시 iptime은 다른 exception이 발생하지는 않고 null 값을 반환하기에 `isEmpty()`를 통해 null 확인을 해주고 이후에 cookieValue를 갱신해 주었다.

이제 iptime에서 리스트를 가지고 다른 더 이상 방법은 떠오르지 않는다.

따라서 이제 가져온 값을 활용하는 부분의 로직을 개선하여야겠다는 생각이 든다.

우선적으로 지금 할 수 있는 생각은 캐싱인데 다음 구현 버전에는 캐싱을 적용해 볼 생각이다.