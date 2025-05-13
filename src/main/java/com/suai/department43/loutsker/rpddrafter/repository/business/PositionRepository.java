package com.suai.department43.loutsker.rpddrafter.repository.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.UniversityPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<UniversityPosition, Long> {
}
