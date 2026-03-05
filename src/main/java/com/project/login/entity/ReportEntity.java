package com.project.login.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_contract")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =====================
       USER / CONTRACT INFO
       ===================== */
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "contract_no")
    private String contractNo;

    @Column(name = "sr_no")
    private String srNo; // 🔧 FIX: must match gen_bill (String)

    @Column(name = "contract_date")
    private LocalDate contractDate;

    /* =====================
       PARTY DETAILS
       ===================== */
    @Column(name = "weaver_name")
    private String weaverName;

    @Column(name = "trader_name")
    private String traderName;

    @Column(name = "broker_name")
    private String brokerName;

    /* =====================
       JOB DETAILS
       ===================== */
    @Column(name = "quality")
    private String quality;

    @Column(name = "quantity_meters")
    private Integer quantityMeters;  // 🔧 FIX: match gen_bill (Integer)

    @Column(name = "job_rate")
    private Double jobRate;

    @Column(name = "pick")
    private Double pick;

    @Column(name = "weaver_brokerage_percent")
    private Double weaverBrokeragePercent;

    @Column(name = "weaver_brokerage_paisa")
    private Double weaverBrokeragePaisa;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "brokerage_percent_amt")
    private Double brokeragePercentAmt;

    @Column(name = "brokerage_mtr_amt")
    private Double brokerageMtrAmt;

    @Column(name = "payment_days")
    private Integer paymentDays;

    @Column(name = "production_schedule")
    private String productionSchedule;

    @Column(name = "no_of_machines")
    private Integer noOfMachines;

    @Column(name = "remark")
    private String remark;

    /* =====================
       AUDIT FIELDS
       ===================== */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* =====================
       GETTERS & SETTERS
       ===================== */

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public LocalDate getContractDate() {
        return contractDate;
    }

    public void setContractDate(LocalDate contractDate) {
        this.contractDate = contractDate;
    }

    public String getWeaverName() {
        return weaverName;
    }

    public void setWeaverName(String weaverName) {
        this.weaverName = weaverName;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Integer getQuantityMeters() {
        return quantityMeters;
    }

    public void setQuantityMeters(Integer quantityMeters) {
        this.quantityMeters = quantityMeters;
    }

    public Double getJobRate() {
        return jobRate;
    }

    public void setJobRate(Double jobRate) {
        this.jobRate = jobRate;
    }

    public Double getPick() {
        return pick;
    }

    public void setPick(Double pick) {
        this.pick = pick;
    }

    public Double getWeaverBrokeragePercent() {
        return weaverBrokeragePercent;
    }

    public void setWeaverBrokeragePercent(Double weaverBrokeragePercent) {
        this.weaverBrokeragePercent = weaverBrokeragePercent;
    }

    public Double getWeaverBrokeragePaisa() {
        return weaverBrokeragePaisa;
    }

    public void setWeaverBrokeragePaisa(Double weaverBrokeragePaisa) {
        this.weaverBrokeragePaisa = weaverBrokeragePaisa;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBrokeragePercentAmt() {
        return brokeragePercentAmt;
    }

    public void setBrokeragePercentAmt(Double brokeragePercentAmt) {
        this.brokeragePercentAmt = brokeragePercentAmt;
    }

    public Double getBrokerageMtrAmt() {
        return brokerageMtrAmt;
    }

    public void setBrokerageMtrAmt(Double brokerageMtrAmt) {
        this.brokerageMtrAmt = brokerageMtrAmt;
    }

    public Integer getPaymentDays() {
        return paymentDays;
    }

    public void setPaymentDays(Integer paymentDays) {
        this.paymentDays = paymentDays;
    }

    public String getProductionSchedule() {
        return productionSchedule;
    }

    public void setProductionSchedule(String productionSchedule) {
        this.productionSchedule = productionSchedule;
    }

    public Integer getNoOfMachines() {
        return noOfMachines;
    }

    public void setNoOfMachines(Integer noOfMachines) {
        this.noOfMachines = noOfMachines;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
