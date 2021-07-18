
import java.net.*;
import java.util.*;

/** Description of Server
 *
 * @author Azhar Asmal
 * @author Matthew Scott
 * @author Gyashka Manilall
 * @version 3 Build April 13, 2021.
 */
 
public class Server {	
 
    private static HashSet<Integer> portSet = new HashSet<Integer>();
    private static HashSet<String> usernames = new HashSet<String>();
    private static String currentUsername;
    private static InetAddress clientIP;
    private static int clientPort;
    private static DatagramSocket socket;

    public static void main(String args[]) throws Exception {
        //Set port  
        int serverPort = 7777;        

        if (args.length < 1) {
            System.out.println("Server started on port " + serverPort);
        } 
        //Else if the user enters a port number as an argument
        else {            
            serverPort = Integer.valueOf(args[0]).intValue();
            System.out.println("Server started on port " + serverPort);
        }
        
        // Open a new datagram socket on the specified port
        socket = new DatagramSocket(serverPort);        

        while(true){
            //Initialize an empty packet to receive to
            byte[] receiveData = new byte[1024];          
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            //wait until a packet is received
            socket.receive(receivePacket);           

            //Extract the message from the packet in the form of a string
            String clientMessage = (new String(receivePacket.getData())).trim();

            //Call method to 'decode' message and determine the server outputs
            decode(clientMessage);

            //Get Client's IP address
            clientIP = receivePacket.getAddress();

            //Obtain and add the client's port to list of ports
            clientPort = receivePacket.getPort();
            portSet.add(clientPort);

        }
    }
        
    private static void decode(String packet) throws Exception{
        //Each message is made up of a header and a body
        //The Header: command$sender/recepient$body
        //The body: message
        
        String[] data = packet.split("[$]");
        if(data == null || data[0] == null){
            //do nothing with the empty packet
        }
        else if(data[0].equals("add-username")){
            currentUsername = data[1];
            usernames.add(currentUsername);
            String user = "["+currentUsername+"]";
            String newUserMessage = user + " has connected to the server"; 
            System.out.println(newUserMessage);
            broadcast(newUserMessage.getBytes());
            String usernameList = "Connected users: " + usernames;
            //Send packet containing currently connected users to the new user
            makePacket(usernameList);         
        }
        
        else if (data[0].equals("broadcast")){
            currentUsername = data[1];
            String user = "["+currentUsername+"]: ";
            String message = user +""+ data[2];
            broadcast(message.getBytes());
        }
        else if(data[0].equals("exit")){
            currentUsername = data[1];
            String user = "["+currentUsername+"]";
            usernames.remove(user);
            String message = user + " has left the chat room.";
            broadcast(message.getBytes());
        }
    }
    
    private static void broadcast(byte[] data) throws Exception{
        for(Integer port : portSet){
            //Create a datagram packet to send
            DatagramPacket packet = new DatagramPacket(data, data.length, clientIP, port); 
            System.out.println("Broadcasting message");
            //broadcast message to each user        
            socket.send(packet);    
        }
    }
    
    private static void makePacket(String message) throws Exception{
        //Create an empty buffer/array of bytes to send back 
	byte[] data  = new byte[1024];
 
	//Assign the message to the send buffer
	data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, clientIP, clientPort); 
        socket.send(packet);
        
    }
}