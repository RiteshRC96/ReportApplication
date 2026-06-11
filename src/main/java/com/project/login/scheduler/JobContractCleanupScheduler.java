package com.project.login.scheduler;

import com.project.login.repository.JobContractRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JobContractCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(JobContractCleanupScheduler.class);
    private final JobContractRepository jobContractRepository;

    public JobContractCleanupScheduler(JobContractRepository jobContractRepository) {
        this.jobContractRepository = jobContractRepository;
    }
    @Scheduled(cron = "0 0 0 3 * ?")
    public void hardDeleteOldContracts() {
        logger.info("Starting scheduled cleanup of soft-deleted job contracts older than 60 days...");
        
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(60);
        
        try {
            jobContractRepository.deleteSoftDeletedContractsOlderThan(cutoffDate);
            logger.info("Scheduled cleanup of soft-deleted job contracts completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during scheduled cleanup of job contracts: ", e);
        }
    }
}
