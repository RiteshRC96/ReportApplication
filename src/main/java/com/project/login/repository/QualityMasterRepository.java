package com.project.login.repository;
import com.project.login.entity.QualityMasterEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QualityMasterRepository extends JpaRepository<QualityMasterEntity, Long> {

	List<QualityMasterEntity> findByUserId(Long userId);
	
	Optional<QualityMasterEntity> findByIdAndUserId(Long id, Long userId);
	
	void deleteByIdAndUserId(Long id, Long userId);

	@Query("SELECT q FROM QualityMasterEntity q WHERE q.qualityName = :qualityName AND q.userId = :userId")
	Optional<QualityMasterEntity> findByQualityNameAndUserId(@Param("qualityName") String qualityName, @Param("userId") Long userId);
	
	long countByUserId(Long userId);
}
