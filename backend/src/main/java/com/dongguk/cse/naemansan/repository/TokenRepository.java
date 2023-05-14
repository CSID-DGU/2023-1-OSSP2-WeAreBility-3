package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Token;
import com.dongguk.cse.naemansan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenUser(User user);

}
