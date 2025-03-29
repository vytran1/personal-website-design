package com.personalinformation.vta.features.permission;

import com.personalinformation.vta.entities.RolePermission;
import com.personalinformation.vta.entities.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {


    @Query("SELECT rp FROM RolePermission rp WHERE rp.id.roleId = ?1")
    public List<RolePermission> findByRoleId(Integer roleId);
}
