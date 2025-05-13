package com.suai.department43.loutsker.rpddrafter.repository.auth;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
