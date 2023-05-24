package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.type.LoginProviderType;
import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.domain.type.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @Query("SELECT u FROM User u WHERE u.id = :userId")
//    Optional<User> findUserId(@Param("userId") Long userId);
    Optional<User> findBySocialIdAndLoginProviderType(String socialId, LoginProviderType loginProviderType);

    @Query("SELECT u.id, u.userRoleType FROM User u WHERE u.id = :userId")
    Optional<Object[]> findUserForAuthentication(@Param("userId") Long userId);

    @Query("SELECT u.id, u.userRoleType FROM User u WHERE u.id = :userId AND u.isLogin = true AND u.refreshToken = :refreshToken")
    Optional<UserLoginForm> findByIdAndIsLoginAndRefreshToken(@Param("userId") Long userId, @Param("refreshToken") String refreshToken);

    Optional<User> findByIdAndIsLoginAndRefreshTokenIsNotNull(Long userId, Boolean isLogin);


    public interface UserLoginForm {
        Long getId();
        UserRoleType getUserRoleType();
    }
}