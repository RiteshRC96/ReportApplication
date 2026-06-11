package com.project.login.service;

import com.project.login.entity.gen_bill;
import com.project.login.entity.User;
import com.project.login.repository.JobContractRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobContractService {

    private final JobContractRepository jobContractRepository;

    public JobContractService(JobContractRepository jobContractRepository) {
        this.jobContractRepository = jobContractRepository;
    }

    public boolean existsByUserIdAndContractNo(Long userId, Integer contractNo) {
        return jobContractRepository.existsByUserIdAndContractNo(userId, contractNo);
    }

    /* ==========================
       FIND (EDIT / DELETE)
       ========================== */
    public gen_bill getByUserIdAndContractNo(
            Long userId,
            Integer contractNo
    ) {
        return jobContractRepository
                .findByUserIdAndContractNo(userId, contractNo)
                .orElseThrow(() ->
                        new RuntimeException("Contract not found"));
    }

    /* ==========================
       DELETE (SOFT DELETE)
       ========================== */
    @Transactional
    public void deleteByUserIdAndContractNo(
            Long userId,
            Integer contractNo
    ) {

        gen_bill contract = getByUserIdAndContractNo(userId, contractNo);

        contract.setDeleted(true);
        contract.setDeletedAt(LocalDateTime.now());
        jobContractRepository.save(contract);
    }

    /* ==========================
       RESTORE SOFT-DELETED RECORD
       ========================== */
    @Transactional
    public void restoreByUserIdAndContractNo(
            Long userId,
            Integer contractNo
    ) {
        gen_bill contract = jobContractRepository.findAnyByUserIdAndContractNo(userId, contractNo)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setDeleted(false);
        contract.setDeletedAt(null);
        jobContractRepository.save(contract);
    }

    /* ==========================
       SAVE OR UPDATE
       ========================== */
    @Transactional
    public gen_bill saveOrUpdate(gen_bill bill, User user) {

        // bind logged in user
        bill.setUserId(user.getId());

        /* ===== CREATE OR UPDATE CHECK ===== */
        if (bill.getContractNo() == null) {
            bill.setContractNo(generateContractNo(user.getId()));
            bill.setSrNo(generateSrNo(user.getId()));
        } else {
            // Check if it already exists
            java.util.Optional<gen_bill> existingOpt = jobContractRepository.findByUserIdAndContractNo(
                    user.getId(), bill.getContractNo());

            if (existingOpt.isPresent()) {
                /* ===== UPDATE ===== */
                gen_bill existing = existingOpt.get();
                // preserve immutable fields
                bill.setId(existing.getId());
                bill.setSrNo(existing.getSrNo());
            } else {
                /* ===== CREATE WITH PRE-FILLED CONTRACT NO ===== */
                bill.setSrNo(generateSrNo(user.getId()));
            }
        }

        return jobContractRepository.save(bill);
    }

    /* ==========================
       LEGACY SAVE (KEEP)
       ========================== */
    @Transactional
    public gen_bill saveJobContract(gen_bill jobContract, User user) {

        jobContract.setUserId(user.getId());

        jobContract.setContractNo(generateContractNo(user.getId()));

        jobContract.setSrNo(generateSrNo(user.getId()));

        return jobContractRepository.save(jobContract);
    }

    /* ==========================
       LEGACY UPDATE (KEEP)
       ========================== */
    @Transactional
    public gen_bill updateJobContract(gen_bill bill) {

        return jobContractRepository.save(bill);
    }

    /* ==========================
       REPORT + EXCEL SEARCH
       ========================== */
    public List<gen_bill> searchReportsByUser(
            Long userId,
            String weaverName,
            String traderName,
            LocalDate fromDate,
            LocalDate toDate
    ) {

        return jobContractRepository.searchReports(
                userId,

                (weaverName == null || weaverName.isBlank())
                        ? null : "%" + weaverName + "%",

                (traderName == null || traderName.isBlank())
                        ? null : "%" + traderName + "%",

                fromDate,
                toDate
        );
    }

    /* ==========================
       CONTRACT NO GENERATOR
       ========================== */
    public Integer generateContractNo(Long userId) {

        Integer maxContractNo =
                jobContractRepository.findMaxContractNoByUser(userId);

        if (maxContractNo == null) {
            return 1;
        }

        return maxContractNo + 1;
    }

    /* ==========================
       SR NO GENERATOR
       ========================== */
    private Integer generateSrNo(Long userId) {

        Integer maxSrNo =
                jobContractRepository.findMaxSrNoByUser(userId);

        if (maxSrNo == null) {
            return 1;
        }

        return maxSrNo + 1;
    }
}