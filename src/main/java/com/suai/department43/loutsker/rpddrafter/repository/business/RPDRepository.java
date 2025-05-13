package com.suai.department43.loutsker.rpddrafter.repository.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RPDRepository extends JpaRepository<RPDEntity, Long> {
    @Query("SELECT rpd FROM RPDEntity rpd WHERE " +
            "rpd.authorName = :author OR rpd.enrollYear = :year OR " +
            "rpd.programCode = :code OR rpd.disciplineName = :name")
    List<RPDEntity> findByProperties(@Param("author") String authorName, @Param("code") String programCode,
                                     @Param("year") int enrollYear,@Param("name") String disciplineName);
}
