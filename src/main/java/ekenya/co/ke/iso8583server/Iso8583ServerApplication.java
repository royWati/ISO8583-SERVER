package ekenya.co.ke.iso8583server;

import ekenya.co.ke.iso8583server.isoconfig.ISO8583RequestListener;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ServerChannel;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.LogSource;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Iso8583ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Iso8583ServerApplication.class, args);
    }


    @PostConstruct
    public void startIsoServer() throws ISOException {

        Logger logger  = new Logger();
        logger.addListener(new SimpleLogListener(System.out));

        ServerChannel channel = new ASCIIChannel("localhost",8000,
                new GenericPackager("./packager/genericpackager.xml"));

        ((LogSource) channel).setLogger(logger, "channel");

        ISOServer server = new ISOServer(8000, channel, null);
        server.setLogger(logger, "server");

        server.addISORequestListener(new ISO8583RequestListener());

        new Thread(server).start();
    }
}
