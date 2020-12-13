package io.tatum.model.response.offchain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class WithdrawalResponseData {
    /**
     * @type {Address}
     * @memberof WithdrawalResponseData
     */
    private Address address;

    /**
     * Amount of unprocessed transaction outputs, that can be used for withdrawal. Bitcoin, Litecoin, Bitcoin Cash only.
     *
     * @type {number}
     * @memberof WithdrawalResponseData
     */
    private BigDecimal amount;

    /**
     * Last used unprocessed transaction output, that can be used.
     * Bitcoin, Litecoin, Bitcoin Cash only. If -1, it indicates prepared vOut with amount to be transferred to pool address.
     *
     * @type {string}
     * @memberof WithdrawalResponseData
     */
    private String vIn;

    /**
     * Index of last used unprocessed transaction output in raw transaction, that can be used. Bitcoin, Litecoin, Bitcoin Cash only.
     *
     * @type {number}
     * @memberof WithdrawalResponseData
     */
    private BigDecimal vInIndex;

    /**
     * Script of last unprocessed UTXO. Bitcoin SV only.
     *
     * @type {string}
     * @memberof WithdrawalResponseData
     */
    private String scriptPubKey;
}