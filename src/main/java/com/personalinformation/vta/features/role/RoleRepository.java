package com.personalinformation.vta.features.role;

import com.personalinformation.vta.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role,Integer> {


    @Query("SELECT COUNT(e) > 0 FROM Role e WHERE e.name = ?1")
    boolean existsByName(String name);
}
