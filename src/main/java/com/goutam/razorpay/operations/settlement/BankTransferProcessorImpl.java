package com.goutam.razorpay.operations.settlement;

import com.goutam.razorpay.common.entity.Money;
import com.goutam.razorpay.common.util.RandomizerUtil;
import com.goutam.razorpay.operations.settlement.dto.BankTransferResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class BankTransferProcessorImpl implements BankTransferProcessor {

    @Override
    public BankTransferResultDto initiate(UUID settlementId, UUID merchantId, Money amount,
                                       String bankAccount, String ifsc) {

        // Call the Bank API

        String registrationRef = "TXN_"+ RandomizerUtil.randomBase64(12);

        log.debug("Bank Transfer call completed for settlementId: {}, registrationRef: {}",
                settlementId, registrationRef);

        return new BankTransferResultDto(registrationRef);
    }
}
