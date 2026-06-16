package com.project.login.repository;

import com.project.login.entity.WeaverTrader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface WeaverTraderRepository extends JpaRepository<WeaverTrader, Long> {

    List<WeaverTrader> findByUserId(Long userId);

    Page<WeaverTrader> findByUserId(Long userId, Pageable pageable);

    Optional<WeaverTrader> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
    
    List<WeaverTrader> findByUserIdAndType(Long userId, String type);

    @Query("SELECT w FROM WeaverTrader w WHERE w.name = :name AND w.userId = :userId")
    Optional<WeaverTrader> findByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);

    Optional<WeaverTrader> findByNameAndPhnoAndTypeAndUserId(String name, Long phno, String type, Long userId);

    long countByUserIdAndType(Long userId, String type);
}
