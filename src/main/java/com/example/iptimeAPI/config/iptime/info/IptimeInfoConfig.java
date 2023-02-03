package com.example.iptimeAPI.config.iptime.info;

/**
 * IptimeInfoConfig는 iptime 설정페이지에 접속하기 위한 기본적이 정보를 설정하고 조회하는 인터페이스입니다.
 */
public interface IptimeInfoConfig {

    String getHostUrl();

    String getHost();

    String getOrigin();

    String getPasswd();

    String getUsername();

    String getIp();

    /**
     * isIp는 접속한 사용자의 ip와 서비스의 ip가 일치하는지 검증하는 메서드입니다.
     * @param ip 접속한 사용자의 ip
     * @return 접속한 사용자의 ip와 서비스의 ip가 일치여부
     */
    boolean isIp(String ip);

}
