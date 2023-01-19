## Club

### 기능 목록

1. 사용자는 모에숲에 출석할 수 있다.
    + 사용자가 에코노베이션 wifi를 사용하고 있는지 확인한다.
        + IptimeService#isInIptime(IpDTO): void
    + 사용자 정보를 바탕으로 서비스에서 등록된 맥 주소를 확인한다.
        + MacAddressService#findMemberMacAddress(memberId): MacAddress.MacAddressResponseDTO
    + iptime에서 조회한 맥 주소 리스트에 사용자의 맥 주소가 존재하는지 확인한다.
        + IptimeService#isExistMacAddress(String macAddress): void
    + 사용자가 동방에 출석 하였다는 기록을 서비스에 남긴다.
        + ClubRoomLogService#save(Long memberId): void


    
2. 사용자는 모에숲의 우측에서 현재 동방 인원 목록을 확인할 수 있다.
    + iptime에서 조회한 맥 주소 리스트와 서비스에 등록된 맥 주소 리스트를 비교하여 일치한다.
        + MacAddressService#browseMacAddresses(): List<MacAddress.MacAddressResponseDTO>
    + 일치하는 맥 주소를 바탕으로 서비스에 등록된 인원의 정보를 조회한 후 반환해준다.
        + IptimeService#browseExistMembers(macAddresses): List<Long>


### 1. 출석

#### 시퀀스 다이어그램

<img width="638" alt="스크린샷 2023-01-19 오후 9 31 59" src="https://user-images.githubusercontent.com/102807742/213443710-efce5602-fb34-428d-8341-d75ca0094617.png">

우선 동방 와이파이를 통해 접속한 인원인지 파악합니다.

이를 파악하는 이유는 동방 와이파이에서 접속하였다는 것을 동방에 출석하였다는 것으로 판단하기 때문입니다.

그래서 동방 와이파이로 접속하였다면 idp 서버에 유저에 대한 정보를 요청합니다.

그리고 반환받은 유저 정보를 가지고 MAC 주소를 조회 합니다.

이후 iptime에서 그 시점의 MAC 주소를 조회합니다.

그리고 조회시 목록에 MAC 주소가 존재한다면 로그를 저장합니다.

#### 예상되는 문제

iptime이 MAC 주소를 갱신하는 시점과 타이밍이 맞지 않을 수 있다. ([해결링크](https://github.com/JNU-econovation/econo-forest-be-iptime/blob/main/docs/problem/%EC%B6%9C%EC%84%9D%EA%B4%80%EB%A0%A8%EB%AC%B8%EC%A0%9C.md))

### 2. 현재 동방 인원

#### 시퀀스 다이어그램

<img width="590" alt="스크린샷 2023-01-19 오후 9 31 15" src="https://user-images.githubusercontent.com/102807742/213443576-9750633e-8f87-49bd-9b02-2501debaa36a.png">

서비스에 등록된 MAC 주소를 조회한다.

iptime에서 MAC 주소를 조회한다.

두 가지 주소를 비교하여 일치하는 MAC 주소의 사용자 정보를 조회한다.