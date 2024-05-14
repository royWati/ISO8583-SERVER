package ekenya.co.ke.iso8583server.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class NetworkManagement {
    /**
     *  define our own network check
     *  field 48 - 10000000 -sign on
     *  field 48 - 20000000 - sign off
     *  field 48 - 30000000 - echo
     */

    private static HashMap<String, NetworkConnectionInformation> hashMap = new HashMap<>();
    public static boolean processNetworkCall(ISOSource isoSource, ISOMsg isoMsg)
            throws ISOException, IOException {

        switch (isoMsg.getString(48)) {
            case "10000000":
                return performSignOn(isoSource, isoMsg);
            case "20000000":
                return performSignOff(isoSource, isoMsg);
            case "30000000":
                return performEcho(isoSource, isoMsg);
            default:
                throw new RuntimeException();
        }

    }

    private static boolean performSignOn(ISOSource isoSource, ISOMsg isoMsg) throws ISOException, IOException {

        if (isoSource instanceof BaseChannel) {
            Socket socket = ((BaseChannel) isoSource).getSocket();
            String clientIp = socket.getInetAddress().getHostAddress();

            System.out.println("connecting ip address : " + clientIp);

            // save the above on db
            NetworkConnectionInformation information = NetworkConnectionInformation
                    .builder()
                    .ipAddress(clientIp)
                    .signOnTime(LocalDateTime.now())
                    .build();

            hashMap.put(clientIp, information);

        }

        ISOMsg response  = (ISOMsg) isoMsg.clone();

        response.setMTI("0810");
        response.set(39, "00");

        response.unset(48);

        isoSource.send(response);

        return true;
    }

    private static boolean performSignOff(ISOSource isoSource, ISOMsg isoMsg) throws ISOException, IOException {

        String field39 = "";
        if (isoSource instanceof BaseChannel) {
            Socket socket = ((BaseChannel) isoSource).getSocket();
            String clientIp = socket.getInetAddress().getHostAddress();

            if (hashMap.containsKey(clientIp)) {
                field39 = "00";
                hashMap.remove(clientIp);
            } else {
                field39 = "002"; // no connection found
            }
        }

        ISOMsg response  = (ISOMsg) isoMsg.clone();

        response.setMTI("0810");
        response.set(39, field39);
        response.unset(48);


        isoSource.send(response);

        return true;
    }
    private static boolean performEcho(ISOSource isoSource, ISOMsg isoMsg) throws ISOException, IOException {
        String field39 = "";
        if (isoSource instanceof BaseChannel) {
            Socket socket = ((BaseChannel) isoSource).getSocket();
            String clientIp = socket.getInetAddress().getHostAddress();

            if (hashMap.containsKey(clientIp)) {
                field39 = "00"; // connection okay
            } else {
                field39 = "002"; // no connection found
            }
        }

        ISOMsg response  = (ISOMsg) isoMsg.clone();

        response.setMTI("0810");
        response.set(39, field39);
        response.unset(48);

        isoSource.send(response);

        return true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class NetworkConnectionInformation{

        private String ipAddress;
        private LocalDateTime signOnTime;
    }

}
