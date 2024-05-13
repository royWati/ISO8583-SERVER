package ekenya.co.ke.iso8583server.isoconfig;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

public class ISO8583RequestListener implements ISORequestListener, Configurable {
    @Override
    public void setConfiguration(Configuration configuration)
            throws ConfigurationException {

        // set any configuration as needed

    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {

        // this is the point where all the messages will be coming from



        return false;
    }
}
