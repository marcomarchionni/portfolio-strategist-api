package com.marcomarchionni.ibportfolio.repositories;

import com.marcomarchionni.ibportfolio.model.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {


    @Query("SELECT p FROM position p WHERE " +
            "(:symbol is null or p.symbol = :symbol) and" +
            "(:assetCategory is null or p.assetCategory = :assetCategory) and" +
            "(:tagged is null or ((:tagged = true and p.strategy is not null ) or (:tagged = false and p.strategy is null)))")
    List<Position> findByParams(@Param("tagged") Boolean tagged,
                                @Param("symbol") String symbol,
                                @Param("assetCategory") String assetCategory);

    void deleteByReportDateBefore(LocalDate reportDate);
}
