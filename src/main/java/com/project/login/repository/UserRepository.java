package com.project.login.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.login.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Page<User> findAllByOrderByIdDesc(Pageable pageable);

    List<User> findByActiveTrue();
    List<User> findByActiveFalse();
}
