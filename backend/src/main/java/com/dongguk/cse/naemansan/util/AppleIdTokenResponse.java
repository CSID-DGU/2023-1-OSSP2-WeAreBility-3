package com.dongguk.cse.naemansan.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class AppleIdTokenResponse {
    private String access_token;
    private String token_type;
    private String expires_in;
    private String refresh_token;
    private String id_token;
}
