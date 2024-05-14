package ekenya.co.ke.iso8583server.isoconfig;

import ekenya.co.ke.iso8583server.network.NetworkManagement;
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
            if (isoMsg.getMTI().equals("0800")) {

                NetworkManagement.processNetworkCall(isoSource, isoMsg);
            }
        } catch (ISOException | IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
