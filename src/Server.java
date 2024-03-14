import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class Server {

    private DatagramSocket requestSocket;
    private DatagramSocket receiveSocket;

    private SharedDataInterface sharedData;

    public Server(SharedDataInterface sharedData) {
        try {
            this.sharedData = sharedData;
            receiveSocket = new DatagramSocket(69);
            requestSocket = new DatagramSocket(68);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ValidateAndSend(String requestPacketData) throws IOException {
        if (isValidRequest(requestPacketData)) {
            byte[] responseData;

            // Extract operation code from the request packet
            String operationCode = requestPacketData.substring(0, 2);

            if ("01".equals(operationCode)) {
                // Read request, send back 0 3 0 1
                responseData = new byte[]{0, 3, 0, 1};
            } else if ("02".equals(operationCode)) {
                // Write request, send back 0 4 0 0
                responseData = new byte[]{0, 4, 0, 0};
            } else {
                System.out.println("Invalid operation code: " + operationCode);
                return;
            }

            // Send the response packet to the Intermediate

            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, InetAddress.getLocalHost(), 23); // Port for the Intermediate
            System.out.println(Arrays.toString(responsePacket.getData()));
            receiveSocket.send(responsePacket);

        } else {
            // If the packet is invalid, handle accordingly
            System.out.println("Invalid request packet: " + requestPacketData);
        }
    }

    public synchronized void start() {
        try {
            while (true) {

                Thread.sleep(5 * 1000);
                // Send a request message to Intermediate
                String receiveRequestString = "Server requesting to receive data from Intermediate";
                byte[] receiveRequest = receiveRequestString.getBytes();
                DatagramPacket requestPacket = new DatagramPacket(receiveRequest, receiveRequest.length, InetAddress.getLocalHost(), 23); // Port for the Intermediate
                requestSocket.send(requestPacket);


                byte[] requestBuffer = new byte[100];
                DatagramPacket requestPacketFromClient = new DatagramPacket(requestBuffer, requestBuffer.length);
                receiveSocket.receive(requestPacketFromClient);
                System.out.println("Received Client Data: " + new String(requestPacketFromClient.getData()));

                // Parse the packet to confirm its validity
                ValidateAndSend(new String(requestPacketFromClient.getData()));


                byte[] receivedIntermediateConfirmation = new byte[40];
                DatagramPacket intermediateConfirmation = new DatagramPacket(receivedIntermediateConfirmation, receivedIntermediateConfirmation.length);
                receiveSocket.receive(intermediateConfirmation);

                System.out.println("----------------------------------------------------------------------------------------------");


            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            receiveSocket.close();
            requestSocket.close();
        }
    }

    private boolean isValidRequest(String data) {
        System.out.println("Received Packet Content: " + data);
        return data.length() >= 4 && (data.startsWith("01") || data.startsWith("02"));
    }

    public static void main(String[] args) throws SocketException {
        try {
            SharedDataInterface sharedData = (SharedDataInterface) Naming.lookup("rmi://localhost/SharedData");

            Server server = new Server(sharedData);
            server.start();
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}