package com.dongguk.cse.naemansan.domain.identifier;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
public class SocialId implements Serializable {
    private String socialId;
    private LoginProviderType provider;
}
