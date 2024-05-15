package ekenya.co.ke.iso8583server.transactions;

import org.jpos.iso.ISOMsg;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessTransactionTests {



    @Test
    public void testBalanceInquiry(){
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.set(39, "000");
        isoMsg.set(48, "ACTUAL BALANCE|5000 ~ AVAILABLE BALANCE|5000 ~ BLOCKED BALANCE|5000");
        isoMsg.set(54, "Balance retrived successfully");

        ISOMsg response = Finance.balanceInquiry(isoMsg);

        assertEquals("000", response.getValue(39));
        assertEquals("ACTUAL BALANCE|5000 ~ AVAILABLE BALANCE|5000 ~ BLOCKED BALANCE|5000", response.getValue(48));
        assertEquals("Balance retrived successfully", response.getValue(54));
    }

    @Test
    public void testFundsTransfer() {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.set(102, "765552897651");
        isoMsg.set(103, "875349875175");
        isoMsg.set(4, "5000");

        ISOMsg response = Finance.fundsTransfer(isoMsg);

        assertEquals("000", response.getString(39));
        assertEquals("DEBITOR BALANCE|4000 ~ CREDITOR BALANCE|10000", response.getString(48));
        assertEquals("funds transfer was successful", response.getString(54));
    }



}
