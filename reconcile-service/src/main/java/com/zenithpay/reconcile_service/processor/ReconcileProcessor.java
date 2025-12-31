package com.zenithpay.reconcile_service.processor;

import com.zenithpay.reconcile_service.model.Transaction;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ReconcileProcessor implements ItemProcessor<Transaction, Transaction> {
    @Override
    public Transaction process(Transaction transaction) throws Exception {
        if (transaction.getAmount() <= 0) {
            return null;
        }
        transaction.setStatus("RECONCILED");
        return transaction;
    }
}
