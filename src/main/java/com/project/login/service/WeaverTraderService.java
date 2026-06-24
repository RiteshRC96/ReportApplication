package com.project.login.service;
import com.project.login.service.WeaverTraderService;
import com.project.login.entity.WeaverTrader;
import com.project.login.repository.WeaverTraderRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
public class WeaverTraderService {

    private final WeaverTraderRepository repository;

    public WeaverTraderService(WeaverTraderRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("null")
    @Caching(evict = {
        @CacheEvict(value = "weavers", key = "#wt.userId"),
        @CacheEvict(value = "traders", key = "#wt.userId")
    })
    public WeaverTrader save(WeaverTrader wt) {
        return repository.save(wt);
    }

    public List<WeaverTrader> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public Page<WeaverTrader> findByUserPaginated(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    public Optional<WeaverTrader> findByIdAndUser(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }
    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "weavers", key = "#userId"),
        @CacheEvict(value = "traders", key = "#userId")
    })
    public void delete(Long id, Long userId) {
        repository.deleteByIdAndUserId(id, userId);
    }
    @Cacheable(value = "weavers", key = "#userId")
    public List<WeaverTrader> getWeavers(Long userId) {
        return repository.findByUserIdAndType(userId, "WEAVER");
    }

    @Cacheable(value = "traders", key = "#userId")
    public List<WeaverTrader> getTraders(Long userId) {
        return repository.findByUserIdAndType(userId, "TRADER");
    }

    public Optional<WeaverTrader> findByNameAndUser(String name, Long userId) {
        return repository.findByNameAndUserId(name, userId);
    }

    public Optional<WeaverTrader> findByNameAndPhnoAndTypeAndUser(String name, Long phno, String type, Long userId) {
        return repository.findByNameAndPhnoAndTypeAndUserId(name, phno, type, userId);
    }

    public long countWeavers(Long userId) {
        return repository.countByUserIdAndType(userId, "WEAVER");
    }

    public long countTraders(Long userId) {
        return repository.countByUserIdAndType(userId, "TRADER");
    }

}
