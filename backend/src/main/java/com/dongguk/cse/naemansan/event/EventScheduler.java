package com.dongguk.cse.naemansan.event;

import com.dongguk.cse.naemansan.domain.User;
import com.dongguk.cse.naemansan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")	// 매일 00시 정각
    public void updatePremium() throws Exception {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getExpirationDate() == null) {
                continue;
            }

            Date nowDate = Timestamp.valueOf(LocalDateTime.now());
            Date userDate = user.getExpirationDate();

            if (!nowDate.after(userDate)) {  // NowDate <= ExpirationDate
               continue;
            }

            user.updatePremium(0L);
        }
    }
}
