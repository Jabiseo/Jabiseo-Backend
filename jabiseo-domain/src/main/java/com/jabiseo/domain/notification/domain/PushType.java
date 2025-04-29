package com.jabiseo.domain.notification.domain;

public enum PushType {

    DAILY_PLAN_COMPLETED("DAIL_PLAN","학습 플랜 달성", "어제 플랜을 달성했네요! 오늘도 파이팅!!"),
    DAILY_PLAN_INCOMPLETE("DAIL_PLAN", "학습 플랜 달성","어제 플랜을 달성하지 못했어요. 오늘은 문제를 풀어볼까요?");


    private String type;
    private String title;
    private String message;

    PushType(String type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
