import java.io.*;
import java.net.*;

/** Description of Client
 *
 * @author Azhar Asmal
 * @author Matthew Scott
 * @author Gyashka Manilall
 * @version 3 Build April 13, 2021.
 */

public class Client {

    public static void main(String args[]) throws Exception {

        //Default port
        String localHost = "localhost";
        int cPort = 7777;

        if (args.length < 1) {
           System.out.println("Connected to server " + localHost + ", on port " + cPort);
        }
        // Get the port number to use from the command line
        else {
           cPort = Integer.valueOf(args[0]).intValue();
           System.out.println("Connected to server " + localHost + ", on port " + cPort);
        }

        try{
            // Get the IP address of the local machine - we will use this as the address to send the data to
            InetAddress address = InetAddress.getByName(localHost);
            SenderThread sender = new SenderThread(address, cPort);
            sender.start();
            ReceiverThread receiver = new ReceiverThread(sender.getSocket());
            receiver.start();
        }
        /**
	       *
	       * @throws Exception
	       */
        catch(Exception ex){
            System.out.println("Cannot Reach Server");
        }
    }
}
