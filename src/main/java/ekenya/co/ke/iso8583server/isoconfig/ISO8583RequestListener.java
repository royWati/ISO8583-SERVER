package ekenya.co.ke.iso8583server.isoconfig;

import ekenya.co.ke.iso8583server.network.NetworkManagement;
import ekenya.co.ke.iso8583server.transactions.ProcessTransactions;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

import java.io.IOException;

public class ISO8583RequestListener implements ISORequestListener, Configurable {
    @Override
    public void setConfiguration(Configuration configuration)
            throws ConfigurationException {

        // set any configuration as needed

    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {

        // this is the point where all the messages will be coming from

        // check for network messages
        try {

            switch (isoMsg.getMTI()) {
                case "0800":
                    return NetworkManagement.processNetworkCall(isoSource, isoMsg);
                case "0200":
                    return ProcessTransactions.processIncomingTransaction(isoSource, isoMsg);
                case "0400":
                    return ProcessTransactions.processReversalTransaction(isoSource, isoMsg);
                default:
                    throw new RuntimeException();
            }
        } catch (ISOException | IOException e) {
            throw new RuntimeException(e);
        }

    }
}
