package com.personalinformation.vta.features.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.personalinformation.vta.entities.Candidate;
import com.personalinformation.vta.entities.Provider;
import com.personalinformation.vta.entities.User;
import com.personalinformation.vta.entities.UserRole;
import com.personalinformation.vta.features.candidate.CandidateRepository;
import com.personalinformation.vta.features.role.UserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void firstTest(){
        User user1 = new User();
        user1.setEmail("vy.tn171003@gmail.com");

        String rawPassword = "0966272357";
        String encodePassword = passwordEncoder.encode(rawPassword);

        user1.setPassword(encodePassword);

        //User saveduser = userRepository.save(user1);

        //assertThat(saveduser).isNotNull();
    }

    @Test
    public void testFindByEmail(){
        String email = "vy.tn171003@gmail.com";

        Optional<User> user = userRepository.findByEmail(email);

        assertThat(user).isPresent();
    }


    @Test
    public void testCreatAdminAccount(){
        User user = new User();
        user.setProvider(Provider.DATABASE);
        user.setPassword(passwordEncoder.encode("0979847481"));
        user.setEmail("adminaccounttesting@gmail.com");
        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole(savedUser.getId(),2);
        userRoleRepository.save(userRole);

        assertThat(userRole).isNotNull();
    }


    @Test
    public void testCreateNormalAccountForDeleteFunction(){

        User user = new User();
        user.setEmail("normalaccounttesting@gmail.com");
        user.setProvider(Provider.DATABASE);
        user.setPassword(passwordEncoder.encode("0979847481"));
        User savedUser = userRepository.save(user);

        UserRole userRole = new UserRole(savedUser.getId(),1);
        userRoleRepository.save(userRole);

        Candidate candidate = new Candidate();
        candidate.setFirstName("Your first name");
        candidate.setLastName("Your last name");
        candidate.setEmail(savedUser.getEmail());
        candidate.setAddress("You should set up address by yourself in dashboard");
        candidate.setLongDescription("You should set up long description by yourself in dashboard");
        candidate.setShortDescription("You should set up short description by yourself in dashboard");
        candidate.setPhoneNumber("01234567891");
        candidate.setImage("default.png");
        candidate.setDob(LocalDate.now());
        Candidate savedCandidate = candidateRepository.save(candidate);

        assertThat(userRole).isNotNull();
        assertThat(savedUser).isNotNull();
        assertThat(savedCandidate).isNotNull();

    }
}
