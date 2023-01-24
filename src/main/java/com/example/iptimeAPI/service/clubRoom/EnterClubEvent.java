package com.example.iptimeAPI.service.clubRoom;


import java.util.List;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EnterClubEvent extends ApplicationEvent {

    private List<Long> memberIds;

    public EnterClubEvent(List<Long> memberIds) {
        super(memberIds);
        this.memberIds = memberIds;
    }
}
