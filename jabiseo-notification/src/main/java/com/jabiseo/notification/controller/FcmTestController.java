package com.jabiseo.notification.controller;

import com.jabiseo.notification.fcm.FcmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Profile({"dev", "local"})
public class FcmTestController {

    private final FcmSender fcmSender;
    @GetMapping("/send/{token}")
    public String send(@PathVariable String token) throws Exception {

        fcmSender.sendMessage(token);

        return "OK";
    }


}
