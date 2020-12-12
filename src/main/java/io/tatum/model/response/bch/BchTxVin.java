package io.tatum.model.response.bch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class BchTxVin {

    private String txid;
    private BigDecimal vout;
    private BchTxScriptSig scriptSig;
    private String coinbase;
    private BigDecimal sequence;

}
