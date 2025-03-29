package com.personalinformation.vta.features.role;

import com.personalinformation.vta.entities.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void createNormalRole(){
        Role role = new Role();
        role.setName("NORMAL");
        role.setDescription("User who have normal role only access their personal information and edit these things. Other things is banned");
        roleRepository.save(role);
    }

    @Test
    public void createAdminRole(){
        Role role = new Role();
        role.setName("ADMIN");
        role.setDescription("User who have admin role only access their personal information and implements CRUD in other people who have normal role. Other things is banned");
        roleRepository.save(role);
    }

    @Test
    public void createSuperAdminRole(){
        Role role = new Role();
        role.setName("SUPER_ADMIN");
        role.setDescription("User who have super admin role have all prerequisites of admin role. Besides they can create new roles and new permissions and assign them to other people");
        roleRepository.save(role);
    }

    @Test
    public void createTestingRole(){
        Role role = new Role();
        role.setName("TESTING");
        role.setDescription("This role is used for only testing");
        Role savedRole = roleRepository.save(role);
        assertThat(savedRole).isNotNull();
    }

    @Test
    public void deleteTestingRole(){
        Integer roleId = 5;
        roleRepository.deleteById(roleId);
        Optional<Role> roleOPT = roleRepository.findById(roleId);
        assertThat(roleOPT).isNotPresent();
    }

    @Test
    public void checkUniqueRoleName(){
        String roleName = "NORMAL";
        boolean isUnique = roleRepository.existsByName(roleName);

        assertThat(isUnique).isTrue();
    }

    @Test
    public void checkUniqueId(){
        Integer roleId = 3;

        boolean isUnique = roleRepository.existsById(roleId);

        assertThat(isUnique).isTrue();


    }
}
