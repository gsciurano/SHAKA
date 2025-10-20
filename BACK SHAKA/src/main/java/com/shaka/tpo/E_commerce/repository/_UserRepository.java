package com.shaka.tpo.E_commerce.repository;

import com.shaka.tpo.E_commerce.entity._User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface _UserRepository extends JpaRepository<_User, Long> {
    Optional<_User> findByEmail(String email);
}