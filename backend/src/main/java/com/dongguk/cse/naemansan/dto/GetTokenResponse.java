package com.dongguk.cse.naemansan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GetTokenResponse {
    public String clientId;
    public String clientSecret;
    public String code;
    public String grantType;
    public String redirectUri;
}
