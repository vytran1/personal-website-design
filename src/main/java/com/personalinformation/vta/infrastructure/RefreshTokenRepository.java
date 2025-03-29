package com.personalinformation.vta.infrastructure;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {


    @Query("SELECT r FROM RefreshToken r WHERE r.user.email = ?1")
    public List<RefreshToken> findRefreshTokenByUsername(String username);

    @Query("DELETE FROM RefreshToken r WHERE r.expiryTime <= CURRENT_TIME")
    @Modifying
    public int deleteExpiredRefreshToken();
}
