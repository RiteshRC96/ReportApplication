package com.project.login.service;
import com.project.login.service.WeaverTraderService;
import com.project.login.entity.WeaverTrader;
import com.project.login.repository.WeaverTraderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class WeaverTraderService {

    private final WeaverTraderRepository repository;

    public WeaverTraderService(WeaverTraderRepository repository) {
        this.repository = repository;
    }

    @SuppressWarnings("null")
    public WeaverTrader save(WeaverTrader wt) {
        return repository.save(wt);
    }

    public List<WeaverTrader> findByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    public Optional<WeaverTrader> findByIdAndUser(Long id, Long userId) {
        return repository.findByIdAndUserId(id, userId);
    }
    @Transactional
    public void delete(Long id, Long userId) {
        repository.deleteByIdAndUserId(id, userId);
    }
    public List<WeaverTrader> getWeavers(Long userId) {
        return repository.findByUserIdAndType(userId, "WEAVER");
    }

    public List<WeaverTrader> getTraders(Long userId) {
        return repository.findByUserIdAndType(userId, "TRADER");
    }

    public Optional<WeaverTrader> findByNameAndUser(String name, Long userId) {
        return repository.findByNameAndUserId(name, userId);
    }

    public Optional<WeaverTrader> findByNameAndPhnoAndTypeAndUser(String name, Long phno, String type, Long userId) {
        return repository.findByNameAndPhnoAndTypeAndUserId(name, phno, type, userId);
    }

}
