package com.dongguk.cse.naemansan.controller;

import com.dongguk.cse.naemansan.dto.request.NotificationRequestDto;
import com.dongguk.cse.naemansan.dto.request.RequestDto;
import com.dongguk.cse.naemansan.service.FirebaseCloudMessageService;
import com.google.firebase.iid.FirebaseInstanceId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
//이벤트 처리하고 삭제 예정
@RestController
@RequiredArgsConstructor
public class MainController {
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @PostMapping("/api/fcm")
    public ResponseEntity pushMessage(@RequestBody RequestDto requestDto) throws IOException {
        System.out.println(requestDto.getTargetToken() + " "
                + requestDto.getTitle() + " " + requestDto.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDto.getTargetToken(),
                requestDto.getTitle(),
                requestDto.getBody());
        return ResponseEntity.ok().build();
    }
}
