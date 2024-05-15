package ekenya.co.ke.iso8583server.transactions;

import org.jpos.iso.ISOMsg;

public class Finance {

    public static ISOMsg balanceInquiry(ISOMsg isoMsg){

        ISOMsg response = (ISOMsg) isoMsg.clone();

        response.set(39, "000");
        response.set(48, "ACTUAL BALANCE|5000 ~ AVAILABLE BALANCE|5000 ~ BLOCKED BALANCE|5000");
        response.set(54, "Balance retrived successfully");

        return response;
    }

    public static ISOMsg fundsTransfer(ISOMsg isoMsg) {
        ISOMsg response = (ISOMsg) isoMsg.clone();

        response.set(39, "000");
        response.set(48, "DEBITOR BALANCE|4000 ~ CREDITOR BALANCE|10000");
        response.set(54, "funds transfer was successful");

        return response;
    }

    public static ISOMsg reverseTransaction(ISOMsg isoMsg) {
        ISOMsg response = (ISOMsg) isoMsg.clone();

        response.set(39, "000");
        response.set(54, "transaction is reversed successfully");

        return response;
    }
}
