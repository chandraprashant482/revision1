package com.revision1.reposistory;

import com.revision1.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailId(String emailId);
    Optional<User> findByUsername(String username);
    Optional<User> findByMobile(String mobile);
}