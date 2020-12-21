package io.tatum.transaction;

import com.google.common.io.BaseEncoding;
import io.tatum.blockchain.XRP;
import io.tatum.model.request.TransferXrp;
import io.tatum.model.response.xrp.AccountData;
import io.tatum.utils.ObjectValidator;
import io.xpring.xrpl.Signer;
import io.xpring.xrpl.Wallet;
import io.xpring.xrpl.XrpException;
import org.xrpl.rpc.v1.*;
import org.xrpl.rpc.v1.Common.Account;
import org.xrpl.rpc.v1.Common.Amount;
import org.xrpl.rpc.v1.Common.*;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class XrpTx {

    /**
     * Sign Xrp transaction with private keys locally. Nothing is broadcast to the blockchain.
     *
     * @param body content of the transaction to broadcast
     * @returns transaction data to be broadcast to blockchain.
     */
    public String prepareXrpSignedTransaction(TransferXrp body) throws ExecutionException, InterruptedException, XrpException {
        if (!ObjectValidator.isValidated(body)) {
            return null;
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                var fromAccount = body.getFromAccount();

                XRP xrp = new XRP();
                var fee = body.getFee() == null || body.getFee().equals(0) ?
                        body.getFee() : xrp.xrpGetFee().divide(BigDecimal.valueOf(1000000));

                XRPDropsAmount feeAmount = XRPDropsAmount.newBuilder().setDrops(fee.longValue()).build();

                AccountAddress senderAddress = AccountAddress.newBuilder().setAddress(fromAccount).build();
                Account account = Account.newBuilder().setValue(senderAddress).build();

                XRPDropsAmount sendAmount = XRPDropsAmount.newBuilder().setDrops(body.getAmount().longValue()).build();
                CurrencyAmount paymentAmount = CurrencyAmount.newBuilder().setXrpAmount(sendAmount).build();
                Amount amount = Amount.newBuilder().setValue(paymentAmount).build();

                AccountAddress destinationAddress = AccountAddress.newBuilder().setAddress(body.getTo()).build();
                Destination destination = Destination.newBuilder().setValue(destinationAddress).build();

                AccountData accountDataInfo = xrp.xrpGetAccountInfo(fromAccount);

                var sequenceInt = accountDataInfo.getSequence();
                Sequence sequence = Sequence.newBuilder().setValue(sequenceInt).build();

                var maxLedgerVersion = accountDataInfo.getLedgerCurrentIndex().add(BigDecimal.valueOf(5)).intValue();
                LastLedgerSequence lastLedgerSequence = LastLedgerSequence.newBuilder().setValue(maxLedgerVersion).build();

                SendMax sendMax = SendMax.newBuilder().setValue(paymentAmount).build();

                SourceTag sourceTag = SourceTag.newBuilder().setValue(body.getSourceTag()).build();

                DestinationTag destinationTag = DestinationTag.newBuilder().setValue(body.getDestinationTag()).build();

                Payment payment = Payment.newBuilder()
                        .setDestination(destination)
                        .setAmount(amount)
                        .setSendMax(sendMax)
                        .setDestinationTag(destinationTag)
                        .build();

                Transaction transaction = Transaction.newBuilder()
                        .setAccount(account)
                        .setFee(feeAmount)
                        .setSequence(sequence)
                        .setSourceTag(sourceTag)
                        .setLastLedgerSequence(lastLedgerSequence)
                        .setPayment(payment)
                        .build();

                Wallet wallet = new Wallet(body.getFromSecret());
                return BaseEncoding.base16().lowerCase().encode(Signer.signTransaction(transaction, wallet));

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }).get();
    }
}