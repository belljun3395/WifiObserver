# iptimeAPI
![Group 42](https://user-images.githubusercontent.com/102807742/215243156-37ddcf58-3206-4a23-a79f-c8138cc26004.png)

+ 에코노베이션 발표영상
  + [기획](https://youtu.be/wOqimi4O8H4?t=4433)(1:14:00)
  + [프런트](https://youtu.be/wOqimi4O8H4?t=4624)(1:17:00)
  + [백엔드](https://youtu.be/wOqimi4O8H4?t=5090)(1:25:00)
  
+ [에코노베이션 발표자료](https://docs.google.com/presentation/d/1GNo1h4OH3nUJPK5yMVGnbbaw2W5vjCPE/edit?usp=share_link&ouid=110092230635164765269&rtpof=true&sd=true)



## iptimeAPI는?

iptimeAPI는 [모여봐요 에코노숲]()(이하 모에숲)의 동방 출석, 출석을 기반으로한 랭킹 그리고 동방 현재 인원을 제공하는 API입니다.

동방 출석의 경우 동방 wifi의 ip를 통해 모에숲 페이지에 접속하는 것으로 파악할 수 있습니다.

하지만 동방 현재 인원의 경우 위와 동일한 방법으로 동방에 방문한 것은 확인 할 수 있지만 그 인원이 동방에서 나가는 것은 파악할 수 없었습니다.

그렇기에 이를 해결할 다른 방법이 필요하였고 저희는  iptime의 설정 페이지(http://192.168.0.1) 의 "사용중인 IP 주소 정보"(고급설정/내부 네트워크)를 활용하기로 했습니다.

<img width="891" alt="iptime내부내트워크" src="https://user-images.githubusercontent.com/102807742/213416863-c2d489d3-c23f-4eff-9d6b-189737c6666f.png">

구체적으로 "사용중인 IP 주소 정보" 중 "MAC 주소"를 활용합니다.

동방 wifi에 동아리원의 노트북이 연결되면 iptime 설정 페이지의 "사용중인 IP 주소 정보"에 해당 노트북에 할당된 IP와 MAC 주소가 업데이트됩니다.

조금 더 자세히 그 과정을 알아보면 다음과 같습니다.

동아리원이 동방 wifi에 접속하면 iptime 설정 페이지에 동아리원의 MAC 주소가 추가되고 동방 wifi와 연결이 끊기면(즉, 동방을 나가면) 동아리원의 MAC 주소가 제거됩니다.

이를 바탕으로 기존 동방 wifi의 ip를 통해서는 알 수 없었던 동방에서 나가는 것을 파악할 수 있게 되었습니다.

## 기술 스택

+ Java 11
+ Springboot
+ MySql / H2
+ Redis
+ Openfeign

## Docs
+ 기술 적인 부분에 대해서 더 궁금 하시다면 : [docs](https://github.com/JNU-econovation/econo-forest-be-iptime/tree/main/docs)
+ 프로젝트 진행 과정에 대해서 더 궁금 하시다면 : [medium](https://medium.com/@belljun3395)

## Swagger
http://43.200.230.93/swagger-ui/#/

Econovation의 내부 서비스인 관계로 외부인은 요청을 보낼 수 없습니다.

추후 Swagger 수정을 통해 외부인도 요청 기대값을 알 수 있도록 조치하도록 하겠습니다. 