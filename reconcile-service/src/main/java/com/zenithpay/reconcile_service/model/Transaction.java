package com.zenithpay.reconcile_service.model;

import com.zenithpay.reconcile_service.generator.TxnId;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @TxnId
    private String transactionId;
    private double amount;
    private String status;

    @Column(name = "transaction_ref")
    private String transactionReference;

    @CreationTimestamp
    @Column(name = "create_date_time", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    @Column(name = "update_date_time")
    private LocalDateTime updateDateTime;
}
