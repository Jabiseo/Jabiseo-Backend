package com.jabiseo.certificate;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Certificate {

    private String certificateId;

    private String name;

    @Builder
    public Certificate(String certificateId, String name) {
        this.certificateId = certificateId;
        this.name = name;
    }

}

