## 3차 API 설명

### 아키텍처
![Group 1](https://user-images.githubusercontent.com/102807742/210286789-df2c8fc9-8668-48cd-8555-8092a4aebe0c.png)

현재 아키텍처의 모습은 이전과 동일하다.

### Iptime에서 리스트 조회
```java
@Service
public class IptimeServiceImpl implements IptimeService {

    private final Iptime iptime;
    private String cookieValue;

    private List<String> macAddressesList;

    @Autowired
    public IptimeServiceImpl(Iptime iptime) throws IOException {
        this.iptime = iptime;
        this.cookieValue = iptime.getCookieValue();
        this.macAddressesList = iptime.getList(cookieValue);
    }

    @Override
    public List<String> getLatestMacAddressesList() throws IOException {
        return this.macAddressesList;
    }

    @Scheduled(fixedDelay = 3000)
    public void renewalList() throws IOException {
        List<String> latestMacAddressesList = this.getMacAddressesList();
        if (!macAddressesList.equals(latestMacAddressesList)) {
            this.macAddressesList = latestMacAddressesList;
        }
    }

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

위는 Iptime에서 리스트를 가져오는 변경된 Service의 구현 코드이다.



```java

    @Autowired
    public IptimeServiceImpl(Iptime iptime) throws IOException {
        this.iptime = iptime;
        this.cookieValue = iptime.getCookieValue();
        this.macAddressesList = iptime.getList(cookieValue); // 추가
    }
    
    @Override
    public List<String> getLatestMacAddressesList() throws IOException {
        return this.macAddressesList;
    }

    @Scheduled(fixedDelay = 3000)
    public void renewalList() throws IOException {
        List<String> latestMacAddressesList = this.getMacAddressesList();
        if (!macAddressesList.equals(latestMacAddressesList)) {
            this.macAddressesList = latestMacAddressesList;
        }
    }
```

아래의 코드가 변경된 코드이다.

우선 가장 특징적인 변경은 `@Scheduled`를 통해 리스트 조회를 일정 시간 동안 자동으로 반복하도록 한 것이다.

그리고 `renewalList` 안에서 기존의 리스트와 조호해온 리스트를 비교하여 그 값이 같지 않다면 갱신하는 코드를 추가하였다.

이로인해 redis를 쓰지는 않았지만 캐싱의 효과를 얻을 수 있었다.

현재 프로젝트의 경우 에코노베이션만을 위한 프로젝트이기에 위와 같이 변수에 저장할 수 있었지만 그렇지 않다면 redis를 사용하는 것도 좋은 선택이라 생각한다.

<img width="369" alt="스크린샷 2023-01-03 오후 1 14 59" src="https://user-images.githubusercontent.com/102807742/210299350-485f91e1-b5d6-4f32-bf96-98776ee94b29.png">

추가적으로 리스트 조회 주기를 3초로 한 이유가 궁금할 수 있는데 이는 iptime에서 리스트를 갱신하는 주기가 위에서 확인할 수 있듯 3초이기 때문이다.

