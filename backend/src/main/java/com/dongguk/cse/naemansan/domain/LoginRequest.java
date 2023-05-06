package com.dongguk.cse.naemansan.domain;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String code;
    private LoginProviderType provider;
}