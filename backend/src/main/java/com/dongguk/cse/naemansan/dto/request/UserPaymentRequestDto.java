package com.dongguk.cse.naemansan.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPaymentRequestDto {
    private String imp_uid;
    private Long amount;
}
