package com.project.login.service;

import com.project.login.entity.gen_bill;
import com.project.login.entity.User;
import com.project.login.repository.JobContractRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
@Service
public class JobContractService {

    private final JobContractRepository jobContractRepository;

    public JobContractService(JobContractRepository jobContractRepository) {
        this.jobContractRepository = jobContractRepository;
    }

    /* ==========================
       SAVE JOB CONTRACT
       (USER-WISE AUTO INCREMENT)
       ========================== */
    @Transactional
    public gen_bill saveJobContract(gen_bill jobContract, User user) {

        // ✅ gen_bill has userId, not User object
        jobContract.setUserId(user.getId());

        // ✅ AUTO INCREMENT CONTRACT NO (USER BASIS)
        jobContract.setContractNo(generateContractNo(user.getId()));
        jobContract.setSrNo(generateSrNo(user.getId()));

        return jobContractRepository.save(jobContract);
    }

    /* ==========================
       REPORT + EXCEL SEARCH
       (USER-ID BASED)
       ========================== */
    public List<gen_bill> searchReportsByUser(
            String userId,
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
    private Integer generateContractNo (Long userId) {

        Integer maxContractNo =
                jobContractRepository.findMaxContractNoByUser(userId);

        return maxContractNo + 1;
    }
    
    private Integer generateSrNo(Long userId) {
    	Integer maxSrNo = jobContractRepository.findMaxSrNoByUser(userId);
    	return maxSrNo + 1;
    }

}
