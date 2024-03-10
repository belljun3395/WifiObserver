# Batch

## 목차

- [패키지 구조](#패키지-구조)
- [패키지 설명](#패키지-설명)
- [config](#config)
- [job](#job)
  - [job/xxx/step](#jobxxxstep)
  - [job/xxx/XXXConfig.java](#jobxxxxxxconfigjava)
  - [job/xxx/utils](#jobxxxutils)
- [schedule](#schedule)

## 배치 서버에 정의된 Job 문서

- [IptimeBrowse](JOBREADME.md)

## 패키지 구조

```
batch
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── observer
│   │   │           └── batch
│   │   │               └── config
│   │   │               └── job
│   │   │               └── schedule
│   │   │               └── BatchMain.java
```

## 패키지 설명

- `BatchMain.java`: Spring Batch Application 실행 클래스
- `config`: Batch 서버에서 사용하는 설정 클래스를 정의하기 위한 패키지
- `job`: Batch 서버의 Job을 정의하기 위한 패키지
- `schedule`: Batch 서버의 Job을 실행하기 위한 스케줄러를 정의하기 위한 패키지

## config

- `BatchConfig.java`: Batch 서버에서 사용하는 설정 클래스를 정의하기 위한 클래스
- `BatchDataSourceConfig.java`: 배치 정보 및 배치 실행 로그를 저장하기 위한 데이터베이스 설정 클래스
- `BatchFactoryConfig.java`: JobBuilderFactory, StepBuilderFactory와 같은 Factory 설정 클래스
- `BatchJobConfig.java`: Job 관련 설정 클래스
- `BatchLaunchConfig.java`: JobLauncher 관련 설정 클래스
- `BatchPropertyConfig.java`: BatchProperty 설정 클래스
- `DelegatedBatchConfigurer.java`: BatchConfigurer 설정 클래스

## job

배치 서버에서 수행할 Job을 정의하기 위한 패키지

### job/xxx/step

`xxx` Job에서 수행할 Step을 정의하기 위한 패키지

Step의 종류에는 `Reader`, `Processor`, `Writer`가 있습니다.

해당 패키지의 클래스는 위의 종류를 postfix로 가집니다.

### job/xxx/XXXConfig.java

`xxx` Job 관련 설정 클래스

Job에 필요한 Step을 정의하고 조합합니다.

### job/xxx/utils

`xxx` Job에서 사용하는 유틸리티 클래스를 정의하기 위한 패키지

`XXXlistener`, `TimeStamper` 등의 클래스가 있습니다.

## schedule

배치 서버의 Job을 실행하기 위한 스케줄러를 정의하기 위한 패키지
