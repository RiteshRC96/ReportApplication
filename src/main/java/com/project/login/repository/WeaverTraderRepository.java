package com.project.login.repository;

import com.project.login.entity.WeaverTrader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeaverTraderRepository extends JpaRepository<WeaverTrader, Long> {

    List<WeaverTrader> findByUserId(Long userId);

    Optional<WeaverTrader> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);
    
    List<WeaverTrader> findByUserIdAndType(Long userId, String type);

}
