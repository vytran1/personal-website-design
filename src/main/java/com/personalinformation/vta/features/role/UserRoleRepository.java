package com.personalinformation.vta.features.role;

import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository  extends JpaRepository<UserRole, UserRoleId> {



    @Query("SELECT ur FROM UserRole ur WHERE ur.id.userId = ?1")
    public List<UserRole> findByUserId(Integer userId);
}
