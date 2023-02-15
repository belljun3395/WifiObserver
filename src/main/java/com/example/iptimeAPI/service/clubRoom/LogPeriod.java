package com.example.iptimeAPI.service.clubRoom;

import java.time.LocalDate;

public enum LogPeriod {
    YEAR("YEAR",
        LocalDate.now()
            .minusYears(1L)),
    MONTH("MONTH",
        LocalDate.now()
            .minusMonths(1L)),
    WEEK("WEEK",
        LocalDate.now()
            .minusWeeks(1L)),
    ;


    private String type;

    private LocalDate beforeLocalDate;


    LogPeriod(String type, LocalDate beforeLocalDate) {
        this.type = type;
        this.beforeLocalDate = beforeLocalDate;
    }

    public String getType() {
        return type;
    }

    public LocalDate getBeforeLocalDate() {
        return beforeLocalDate;
    }
}
