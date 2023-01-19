package com.example.iptimeAPI.service.clubRoom;


import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class EnterClubEvent extends ApplicationEvent {
    private List<Long> memberIds;
    public EnterClubEvent(List<Long> memberIds) {
        super(memberIds);
        this.memberIds = memberIds;
    }
}
