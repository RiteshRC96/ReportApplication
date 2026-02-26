package com.project.login.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weaver_trader")
public class WeaverTrader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Long phno;

    // WEAVER or TRADER
    @Column(nullable = false)
    private String type;

    @Column(name = "owner_mob_no")
    private Long ownerMobNo;

    @Column(name = "divanji_mob_no")
    private Long divanjMobNo;

    @Column(name = "weaver_brokerage_percent")
    private Double weaverBrokeragePercent;

    @Column(name = "weaver_brokerage_paisa")
    private Double weaverBrokeragePaisa;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getphno() { return phno; }
    public void setphno(Long phno) { this.phno = phno; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getOwnerMobNo() { return ownerMobNo; }
    public void setOwnerMobNo(Long ownerMobNo) { this.ownerMobNo = ownerMobNo; }

    public Long getDivanjMobNo() { return divanjMobNo; }
    public void setDivanjMobNo(Long divanjMobNo) { this.divanjMobNo = divanjMobNo; }

    public Double getWeaverBrokeragePercent() { return weaverBrokeragePercent; }
    public void setWeaverBrokeragePercent(Double weaverBrokeragePercent) { this.weaverBrokeragePercent = weaverBrokeragePercent; }

    public Double getWeaverBrokeragePaisa() { return weaverBrokeragePaisa; }
    public void setWeaverBrokeragePaisa(Double weaverBrokeragePaisa) { this.weaverBrokeragePaisa = weaverBrokeragePaisa; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
