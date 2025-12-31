package com.zenithpay.reconcile_service.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;


public class TxnIdGenerator extends SequenceStyleGenerator {

    public static final String PREFIX = "ZTXN-";
    public static final String NUMBER_FMT = "%9d";

    @Override
    public Object generate(SharedSessionContractImplementor session, Object o) {
        Object seqValue = super.generate(session, o);
        return PREFIX + String.format(NUMBER_FMT, seqValue);
    }

}
