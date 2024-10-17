package com.jabiseo.controller;

import com.jabiseo.fcm.FcmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmTestController {

    private final FcmSender fcmSender;
    @GetMapping("/send/{token}")
    public String send(@PathVariable String token) throws Exception {

        fcmSender.sendMessage(token);

        return "OK";
    }


}
