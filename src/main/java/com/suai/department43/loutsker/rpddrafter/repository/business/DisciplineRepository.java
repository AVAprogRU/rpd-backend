package com.suai.department43.loutsker.rpddrafter.repository.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<DisciplineEntity, Long> {
    Optional<DisciplineEntity> findByName(String name);
}
