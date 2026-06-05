package com.boxing.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "join_batch_requests")
@Data
public class JoinBatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mobile;
    private String email;
    private String time;
    private String programType;
    private String service;          // new field added
    private LocalDate registeredDate;

    @PrePersist
    public void onCreate() {
        this.registeredDate = LocalDate.now();
    }
}
