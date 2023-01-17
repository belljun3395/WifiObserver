# iptimeAPI

## 클래스 다이어그램
![클래스다이어그램](https://user-images.githubusercontent.com/102807742/212796768-f871c4c2-3aea-4c75-9099-52c82955941d.png)


## API 설명


### 사이트 접속시

+ 사이트 접속시 동방 wifi를 통해 접속한지 판단할 수 있어야 한다.
    + `POST /clubs/in`
        + `IptimeService#isInIptime` : 동방 wifi로 접속 여부를 판단한다.

+ 사이트 접속시 동방 wifi를 통해 접속하였다면 서비스에 등록된 mac주소를 가진 기기로 접속하였는지 판단후 서비스를 이용할 수 있다.
    + `POST /clubs/entrance`
        + `MacAddressService#findMemberMacAddress` : 서비스 DB에 저장된 member의 macAddress를 조회한다.
        + `IptimeService#isExistMacAddress` : 위의 macAddress가 iptime의 리스트에 존재하는지 확인한다.
        + `ClubRoomLogService#save` : member의 동방 출석을 저장한다.
+ 사이트 접속시 동방 wifi를 통해 접속하였고 서비스에 등록된 mac주소를 가진 기기로 접속한 것이 아니라면 macAddress를 다시 등록하여야 한다.
    + `PUT /macs`
        + `MacAddressService#registerMacAddress` : member의 macAddress를 수정한다.


### 동방 인원 현황

+ 동방 wifi에 접속한 인원이 몇명인지 그리고 그 인원들의 정보를 알 수 있어야 한다.
    + `GET /clubs/members`
        + `MacAddressService#browseMacAddresses` : 서비스에 저정된 macAddress를 모두 조회한다.
        + `IptimeService#browseExistMembers` : 서비스에 저장된 macAddress와 iptime의 리스트를 비교하여 동방에 있는 동아리원을 파악한다.

    
### mac 주소 관리

+ 서비스를 위해 mac 주소를 등록할 수 있어야 한다.
    + `POST /macs`
        + `MacAddressService#registerMacAddress` : macAddress를 서비스에 등록할 수 있다.

+ 자신이 등록한 mac 주소가 어떠한 것인지 알 수 있어야 한다.
    + `GET /macs/{memberId}`
        + `MacAddressService#findMemberMacAddress` : 서비스에 저장된 member의 macAddress를 조회할 수 있다.

+ 자신이 등록한 mac 주소를 수정할 수 있어야 한다.
    + `PUT /macs`
        + `MacAddressService#editMacAddress` : 서비스에 저장된 member의 macAddress를 수정할 수 있다.

    
### 랭킹

+ 년간, 월간, 주간 랭킹을 확인할 수 있어야 한다.
    + `GET /clubs/rankings/{type}`
        + `MacAddressService#browseMacAddressesMembers`
        + `ClubRoomLogService#getRanking`
