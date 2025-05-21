package com.suai.department43.loutsker.rpddrafter.repository.business;

import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RPDRepository extends JpaRepository<RPDEntity, Long> {

    @Query("""
    SELECT r FROM RPDEntity r
    WHERE (:disciplineName IS NULL OR r.disciplineName LIKE CONCAT('%', :disciplineName, '%'))
           AND (:programCode IS NULL OR r.programCode LIKE CONCAT('%', :programCode, '%'))
           AND (:enrollYear IS NULL OR r.enrollYear = :enrollYear)
           AND (:authorName IS NULL OR r.authorName LIKE CONCAT('%', :authorName, '%'))
    """)
    List<RPDEntity> search(
            @Param("disciplineName") String disciplineName,
            @Param("programCode") String programCode,
            @Param("enrollYear") Integer enrollYear,
            @Param("authorName") String authorName
    );

    Optional<RPDEntity> findByDiscipline_Id(Long disciplineId);
}
