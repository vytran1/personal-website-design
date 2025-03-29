package com.personalinformation.vta.features.user;

import com.personalinformation.vta.entities.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


    @Query("SELECT u FROM User u JOIN FETCH u.userRoles WHERE u.email = ?1")
    public Optional<User> findByEmail(String email);


    @Query(
            "SELECT u FROM User u " +
                    "JOIN u.userRoles ur " +
                    "JOIN ur.role r " +
                    "GROUP BY u.id " +
                    "HAVING SUM(CASE WHEN r.name = 'NORMAL' THEN 1 ELSE 0 END) > 0 " +
                    "AND SUM(CASE WHEN r.name != 'NORMAL' THEN 1 ELSE 0 END) = 0"
    )
    public List<User> listAllUserWhoHaveOnlyNormalRole();


    @Query("SELECT u FROM User u WHERE u.id = ?1")
    public User findByUserId(Integer id);
}
