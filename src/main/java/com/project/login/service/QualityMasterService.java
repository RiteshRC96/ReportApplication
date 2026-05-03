package com.project.login.service;

import com.project.login.entity.QualityMasterEntity;
import com.project.login.repository.QualityMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class QualityMasterService {
	
	private final QualityMasterRepository repository;
	
	public QualityMasterService(QualityMasterRepository repository) {
		this.repository = repository;
	}
	
	@SuppressWarnings("null")
	public QualityMasterEntity save(QualityMasterEntity qtEntity) {
		// Duplicate Check
		Optional<QualityMasterEntity> existing = repository.findByQualityNameAndUserId(qtEntity.getQualityName(), qtEntity.getUserId());
		
		if (existing.isPresent()) {
			// If it's a new record OR a different record being updated to an existing name
			if (qtEntity.getId() == null || !existing.get().getId().equals(qtEntity.getId())) {
				throw new RuntimeException("quality already exist");
			}
		}
		
		return repository.save(qtEntity);		
	}
	
	public List<QualityMasterEntity> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }
	
	public Optional<QualityMasterEntity> findByIdAndUser(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }
	
	@Transactional
	public void delete(Long id, Long userId) {
	     repository.deleteByIdAndUserId(id, userId);
	}

	public Optional<QualityMasterEntity> findByNameAndUser(String qualityName, Long userId) {
		return repository.findByQualityNameAndUserId(qualityName, userId);
	}
}
