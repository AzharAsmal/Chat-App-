import java.io.*;
import java.net.*;

/** Description of SenderThread
 *
 * @author Azhar Asmal
 * @author Matthew Scott
 * @author Gyashka Manilall
 * @version 3 Build April 13, 2021.
 */

public class SenderThread extends Thread {
    private boolean stopped = false;
    private DatagramSocket socket;
    private int port;
    private String username;
    private InetAddress IPAddress;

    public void run() {
        try {
        	//send blank message
        	byte[] data = new byte[1024];
        	data = "".getBytes();
        	DatagramPacket startMessage = new DatagramPacket(data,data.length , IPAddress, port);
                socket.send(startMessage);

        	// Create input stream
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                //Receive username
                System.out.println("Welcome to the UDP based Chat Application");
                System.out.println("To leave the chat room type \"Bye\"");
                System.out.println("Please enter your username: ");
                username = inFromUser.readLine();
                String message = "add-username$"+username+"";
                data = message.getBytes();
                DatagramPacket userPacket = new DatagramPacket(data, data.length, IPAddress, port);
                socket.send(userPacket);


            boolean loop = true;
            while (loop)
            {
                if (stopped)
                    return;

                // User's message to be sent
                String clientMessage = inFromUser.readLine();

                if(clientMessage != null || !clientMessage.equals("")){
                    // Byte Buffer that holds the message
                    byte[] sendData = new byte[1024];
                    // Put this message into our array
                    clientMessage = "broadcast$"+username+"$"+clientMessage;
                    sendData = clientMessage.getBytes();

                    // Create a packet
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    // Send to server
                    System.out.println("Sending...");
                    socket.send(sendPacket);

                    //Checks if user sent the prompt to stop
                    if (clientMessage.contains("Bye") || clientMessage.contains("bye")){
                        String exit = "exit$"+username;
                        sendData = exit.getBytes();
                        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        socket.send(sendPacket);
                        loop = false;
                        break;
                    }
                }
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public boolean login(){
        if(username == null){
            return false;
        }
        else{
            return true;
        }
    }

    public SenderThread(InetAddress address, int port) throws SocketException {
        this.IPAddress = address;
        this.port = port;
        this.socket = new DatagramSocket();
        this.socket.connect(IPAddress, port);
    }

    public void halt() {
        this.stopped = true;
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }
}
