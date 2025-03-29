package com.personalinformation.vta.features.role;
import static org.assertj.core.api.Assertions.assertThat;

import com.personalinformation.vta.entities.Role;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.entities.UserRoleId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRoleRepositoryTest {


    @Autowired
    private UserRoleRepository userRoleRepository;



    @Test
    public void testSaveNormalRoleToOneUser(){
        Integer userId = 2;
        Integer roleId = 1;
        UserRoleId id = new UserRoleId();
        id.setUserId(userId);
        id.setRoleId(roleId);
        UserRole userRole = new UserRole();
        userRole.setId(id);
        userRole.setRole(new Role(roleId));
        userRole.setUser(new User(userId));

        UserRole userRole1 = userRoleRepository.save(userRole);

        assertThat(userRole1).isNotNull();

        if(userRole1 != null){
            System.out.println("User name " + userRole1.getUser().getEmail());
            System.out.println("Role name " + userRole1.getRole().getName());
        }
    }


    @Test
    public void testDeleteUserRole(){
        UserRole userRole = new UserRole();
        userRole.getId().setUserId(3);
        userRole.getId().setRoleId(1);


        userRoleRepository.deleteById(userRole.getId());

        Optional<UserRole> userRoleOptional = userRoleRepository.findById(userRole.getId());

        assertThat(userRoleOptional).isNotPresent();
    }
}
