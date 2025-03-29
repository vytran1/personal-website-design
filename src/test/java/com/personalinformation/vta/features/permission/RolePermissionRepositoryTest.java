package com.personalinformation.vta.features.permission;
import static org.assertj.core.api.Assertions.assertThat;

import com.personalinformation.vta.entities.Permission;
import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.RolePermission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RolePermissionRepositoryTest {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Test
    public void testLinkAdminRoleWithManageUserPermission(){
        Integer roleId = 2;

        Integer permissionId = 1;

        RolePermission rolePermission1 = new RolePermission();
        rolePermission1.getId().setRoleId(roleId);
        rolePermission1.getId().setPermissionId(permissionId);
        rolePermission1.setRole(new Role(roleId));
        rolePermission1.setPermission(new Permission(permissionId));

        RolePermission savedRolePermission = rolePermissionRepository.save(rolePermission1);


        assertThat(savedRolePermission).isNotNull();


    }
}
