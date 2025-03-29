package com.personalinformation.vta.features.security.deleteExpiredRefreshToken;

import com.personalinformation.vta.infrastructure.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class RefreshTokenRemovalScheduledTask {


    private static final Logger log = LoggerFactory.getLogger(RefreshTokenRemovalScheduledTask.class);

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenRemovalScheduledTask(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Scheduled(fixedDelayString = "${app.refresh-token.removal.interval}", initialDelay = 5000)
    @Transactional
    public void deleteExpiredRefreshToken(){
        int deletedToken = refreshTokenRepository.deleteExpiredRefreshToken();

        log.info("Number of deleted tokens " + deletedToken);
    }
}
