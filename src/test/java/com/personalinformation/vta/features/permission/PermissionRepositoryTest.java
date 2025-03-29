package com.personalinformation.vta.features.permission;
import static org.assertj.core.api.Assertions.assertThat;

import com.personalinformation.vta.entities.Permission;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository permissionRepository;



    @Test
    public void testCreateTable(){

    }


    @Test
    public void testCreateFirstPermission(){
        Permission permission = new Permission();
        permission.setName("MANAGE_ROLE");
        permission.setDescription("The person who have this permission in role could see list role");
        Permission savedPermission = permissionRepository.save(permission);
        assertThat(savedPermission).isNotNull();

    }


}
