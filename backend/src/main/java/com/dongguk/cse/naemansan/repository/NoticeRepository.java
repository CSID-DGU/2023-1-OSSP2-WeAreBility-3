package com.dongguk.cse.naemansan.repository;

import com.dongguk.cse.naemansan.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Optional<Notice> findByTitleAndStatus(String title, Boolean status);

    Optional<Notice> findByIdAndStatus(Long noticeId, Boolean status);

    Optional<Notice> findByIdNotAndTitleAndStatus(Long noticeId, String title, Boolean status);

    Page<Notice> findAllByStatus(Boolean status, Pageable pageable);
}
