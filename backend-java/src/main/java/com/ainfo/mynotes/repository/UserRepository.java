package com.ainfo.mynotes.repository;

import com.ainfo.mynotes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String userName);

    User findByUsername(String userName);
}
