
import java.io.*;
import java.net.*;

/** Description of ReceiverThread
 *
 * @author Azhar Asmal
 * @author Matthew Scott
 * @author Gyashka Manilall
 * @version 3 Build April 13, 2021.
 */

public class ReceiverThread extends Thread {
    private DatagramSocket socket;
    
	public ReceiverThread(DatagramSocket socket) throws SocketException {
        this.socket = socket;
    }

    public void run() {

        //This will create an array, it will receive packets
        byte[] data = new byte[1024];

        while (true) {
            if (stopped)
            return;

            // Datagram Packet for data retrieval
            DatagramPacket packet = new DatagramPacket(data, data.length);

            try {
                //socket get Packet
                socket.receive(packet);
                System.out.println("Receiving");
                //get response from packet
                String serverReply =  new String(packet.getData(), 0, packet.getLength());


                System.out.println(serverReply); //Screen Output

            }
            catch (IOException ex) {
            System.err.println(ex);
            }
        }
    }
}
