package ekenya.co.ke.iso8583server.transactions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.io.IOException;
import java.util.HashMap;

public class ProcessTransactions {

    private static HashMap<String, Transaction> transactionHashMap = new HashMap<>();

    public static boolean processIncomingTransaction(ISOSource isoSource, ISOMsg isoMsg) throws ISOException, IOException {

        // check for duplicates
        if (transactionHashMap.containsKey(isoMsg.getString(37))) {
            isoMsg.set(39, "112");
            isoMsg.set(54, "Duplicate transaction found");

            isoMsg.setMTI("0210");

            isoSource.send(isoMsg);
            return true;
        }

        ISOMsg response;

        boolean successfulReqeuest = true;
        //peform the transactions
        switch (isoMsg.getString(3)) {

            case "150000":
                response = Finance.balanceInquiry(isoMsg);
                break;
            case "200000":
                response = Finance.fundsTransfer(isoMsg);
                break;
            default:
                response = (ISOMsg) isoMsg.clone();
                response.set(39, "116");
                response.set(54, "Invalid processing code provided");

                successfulReqeuest = false;

                break;
        }

        response.setMTI("0210"); // set mti response value

        if (successfulReqeuest) {

            // save the transaction in the hashmap

            Transaction t = Transaction.builder()
                    .request(isoMsg)
                    .response(response)
                    .successful(true)
                    .build();

            transactionHashMap.put(isoMsg.getString(37), t);
        }

        isoSource.send(response);
        return true;
    }

    public static boolean processReversalTransaction(ISOSource isoSource, ISOMsg isoMsg) throws ISOException, IOException {

        // check for original transaction
        if (!transactionHashMap.containsKey(isoMsg.getString(37))) {
            isoMsg.set(39, "120");
            isoMsg.set(54, "Original transaction not found");
            isoMsg.setMTI("0210");

            isoSource.send(isoMsg);
            return true;
        }

        Transaction t = transactionHashMap.get(isoMsg.getString(37));

        // check if the transaction was already reversed
        if (t.reversed) {
            isoMsg.set(39, "150");
            isoMsg.set(54, "Transaction already reversed");
            isoMsg.setMTI("0210");

            isoSource.send(isoMsg);
            return true;
        }


        ISOMsg response = Finance.reverseTransaction(isoMsg);

        response.setMTI("0430");

        isoSource.send(response);

        return true;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Transaction {

        private boolean successful;
        private boolean reversed;
        private ISOMsg request;
        private ISOMsg response;
        private ISOMsg reversedRequest;
        private ISOMsg reversedResponse;
    }

}
