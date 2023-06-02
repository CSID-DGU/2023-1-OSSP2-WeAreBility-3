package com.dongguk.cse.naemansan.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class JWTSetKeys {
    private String kty;
    private String kid;
    private String use;
    private String alg;
    private String n;
    private String e;
}
