package com.personalinformation.vta.features.permission;

import com.personalinformation.vta.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
