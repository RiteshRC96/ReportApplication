package com.project.login.repository;

import com.project.login.entity.gen_bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobContractRepository extends JpaRepository<gen_bill, Long> {

    /* ==========================
       REPORT + EXCEL QUERY
       ========================== */
    @Query("""
        SELECT j FROM gen_bill j
        WHERE j.userId = :userId
          AND j.isDeleted = false
          AND (:weaverName IS NULL OR j.weaverName ILIKE :weaverName)
          AND (:traderName IS NULL OR j.traderName ILIKE :traderName)
          AND (CAST(:fromDate AS date) IS NULL OR j.contractDate >= :fromDate)
          AND (CAST(:toDate AS date) IS NULL OR j.contractDate <= :toDate)
        ORDER BY j.contractNo  DESC
    """)
    List<gen_bill> searchReports(
            @Param("userId") Long userId,
            @Param("weaverName") String weaverName,
            @Param("traderName") String traderName,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    /* ==========================
       FIND FOR EDIT / DELETE (ACTIVE ONLY)
       ========================== */
    @Query("""
        SELECT j FROM gen_bill j
        WHERE j.userId = :userId
          AND j.contractNo = :contractNo
          AND j.isDeleted = false
    """)
    Optional<gen_bill> findByUserIdAndContractNo(
            @Param("userId") Long userId,
            @Param("contractNo") Integer contractNo
    );

    /* ==========================
       FIND ANY (INCLUDING SOFT DELETED) FOR RESTORE
       ========================== */
    @Query("""
        SELECT j FROM gen_bill j
        WHERE j.userId = :userId
          AND j.contractNo = :contractNo
    """)
    Optional<gen_bill> findAnyByUserIdAndContractNo(
            @Param("userId") Long userId,
            @Param("contractNo") Integer contractNo
    );

    /* ==========================
       (OPTIONAL) EXISTS CHECK (ACTIVE ONLY)
       ========================== */
    @Query("""
        SELECT COUNT(j) > 0 FROM gen_bill j
        WHERE j.userId = :userId
          AND j.contractNo = :contractNo
          AND j.isDeleted = false
    """)
    boolean existsByUserIdAndContractNo(
            @Param("userId") Long userId,
            @Param("contractNo") Integer contractNo
    );

    /* ==========================
       HARD DELETE SOFT-DELETED RECORDS OLDER THAN X DAYS
       ========================== */
    @Modifying
    @Transactional
    @Query("DELETE FROM gen_bill j WHERE j.isDeleted = true AND j.deletedAt < :cutoff")
    void deleteSoftDeletedContractsOlderThan(@Param("cutoff") LocalDateTime cutoff);

    /* ==========================
       USER-WISE AUTO INCREMENT
       ========================== */
    @Query(value = """
        SELECT COALESCE(MAX(contract_no), 0)
        FROM job_contract
        WHERE user_id = :userId
    """, nativeQuery = true)
    Integer findMaxContractNoByUser(
            @Param("userId") Long userId
    );

    @Query(value = """
        SELECT COALESCE(MAX(sr_no), 0)
        FROM job_contract
        WHERE user_id = :userId
    """, nativeQuery = true)
    Integer findMaxSrNoByUser(
            @Param("userId") Long userId
    );

    boolean existsByWeaverNameAndUserId(String weaverName, Long userId);
    boolean existsByTraderNameAndUserId(String traderName, Long userId);
    boolean existsByQualityAndUserId(String quality, Long userId);
}
