
import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

public class Intermediate {

    public static boolean serverFlag = false;
    public static boolean clientFlag = false;


    private DatagramSocket sendReceiveSocket;
    private DatagramSocket receiveSocket;

    private SharedDataInterface sharedData;

    public Intermediate(SharedDataInterface sharedData) {
        try {
            this.sharedData = sharedData;
            receiveSocket = new DatagramSocket(23); // Port for receiving requests from both client and server
            sendReceiveSocket = new DatagramSocket(); // Socket for sending/receiving acknowledgment messages
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        try {


            while (true) {

                byte[] incomingMessage = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(incomingMessage, incomingMessage.length);
                receiveSocket.receive(receivePacket);


                sharedData.addMessage(new String(receivePacket.getData()));
                System.out.println("Added" + sharedData.getLast());

                // Send acknowledgment to Client
                String acknowledgmentMessage = "Intermediate received the Data";
                byte[] acknowledgmentData = acknowledgmentMessage.getBytes();
                DatagramPacket acknowledgmentPacket = new DatagramPacket(acknowledgmentData, acknowledgmentData.length, receivePacket.getAddress(), receivePacket.getPort());
                sendReceiveSocket.send(acknowledgmentPacket);



                //Wait for server Request

                byte[] incomingServerRequest = new byte[1024];
                DatagramPacket incomingServerRequestPacket = new DatagramPacket(incomingServerRequest, incomingServerRequest.length);
                receiveSocket.receive(incomingServerRequestPacket);
                System.out.println("Received Server Request for data " + new String(incomingServerRequestPacket.getData()));

                System.out.println("Going forward this to Server" + sharedData.getLast());
                // Forward data to the server for processing
                forwardClientDataToServer(sharedData.getMessage());

                //Server Returns Processed Data

                byte[] serverProcessedData = new byte[4];
                DatagramPacket serverProcessedPacket = new DatagramPacket(serverProcessedData, serverProcessedData.length);
                receiveSocket.receive(serverProcessedPacket);


                sharedData.addMessage(Arrays.toString(serverProcessedPacket.getData()));
                System.out.println("Added " + sharedData.getLast());


                //Intermediate Acknowledges the data
                String serverAcknowledgmentMessage = "Intermediate received the Data";
                byte[] serverAcknowledgmentMessageData = serverAcknowledgmentMessage.getBytes();
                DatagramPacket serverAcknowledgmentMessagePacket = new DatagramPacket(serverAcknowledgmentMessageData, serverAcknowledgmentMessageData.length, receivePacket.getAddress(), receivePacket.getPort());
                sendReceiveSocket.send(serverAcknowledgmentMessagePacket);

                //Wait for client request
                byte[] clientRequestData = new byte[1024];
                DatagramPacket clientRequestDataPacket = new DatagramPacket(clientRequestData, clientRequestData.length);
                receiveSocket.receive(clientRequestDataPacket);
                String clientRequestString = new String(clientRequestDataPacket.getData(), clientRequestDataPacket.getOffset(), clientRequestDataPacket.getLength()).trim();
                System.out.println("Received Request from Client: " + clientRequestString);

                System.out.println("Going forward this to Client" + sharedData.getLast());

                forwardDataToClient(sharedData.getMessage());

                System.out.println("----------------------------------------------------------------------------------------------");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void forwardClientDataToServer(String clientPacket) throws IOException {
        byte [] clientPacketBytes = clientPacket.getBytes();
        // Forward client data to server for processing
        DatagramPacket sendPacket = new DatagramPacket(clientPacketBytes, clientPacketBytes.length, InetAddress.getLocalHost(), 69); // Port for server
        sendReceiveSocket.send(sendPacket);
    }

    public void forwardDataToClient(String serverPacket) throws IOException {

        byte [] serverBytes = serverPacket.getBytes();
        // Forward processed data from server to client
        DatagramPacket forwardPacket = new DatagramPacket(serverBytes, serverBytes.length, InetAddress.getLocalHost(), 10); //send to Client
        sendReceiveSocket.send(forwardPacket);
    }

    public static void main(String[] args) {

        try {
            SharedData sharedData = new SharedData();
            System.setProperty("java.rmi.server.hostname","192.168.1.2");



            // Create and export the RMI registry
            LocateRegistry.createRegistry(1099);

            // Bind the remote object's stub in the registry
            Naming.rebind("SharedData", sharedData);

            Intermediate intermediateHost = new Intermediate(sharedData);
            intermediateHost.run();
        } catch (MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}