import java.io.IOException;
import java.net.*;
import java.util.Arrays;


/**
 * Receives data from the Intermediate class and parses them check if they are valid; sends a message back according to the message received
 *
 * @author Zarif
 * @version 1.0
 */

public class Server {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket receiveSocket;


    /**
     * Constructor : Initializes Server Socket
     */
    Server() {
        try {

            receiveSocket = new DatagramSocket(69);
        } catch (Exception e) {

            System.out.println("Oops!");
        }
    }

    /**
     * Forwards response to Intermediate according to data received upon confirming if the data is valid
     *
     * @param requestPacket
     * @param requestPacketData
     * @throws IOException
     */

    public void ValidateAndSend(DatagramPacket requestPacket, String requestPacketData) throws IOException {
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

            System.out.println("Sending Response: " + Arrays.toString(responseData));

            // Send the response packet to the client
            DatagramSocket responseSocket = new DatagramSocket();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, requestPacket.getAddress(), 23);
            responseSocket.send(responsePacket);

            // Close the socket used for this response
            responseSocket.close();
        } else {
            // If the packet is invalid, handle accordingly
            System.out.println("Invalid request packet: " + requestPacketData);
        }
    }

    /**
     * Beings the behaviour of the Server class
     */

    public void start() {
        try {
            while (true) {
                byte[] requestBuffer = new byte[100];
                DatagramPacket requestPacket = new DatagramPacket(requestBuffer, requestBuffer.length);
                receiveSocket.receive(requestPacket);

                System.out.println("Received Request: " + new String(requestPacket.getData()));

                // Parse the packet to confirm its validity
                ValidateAndSend(requestPacket, new String(requestPacket.getData()));
            }
        } catch (Exception e) {
            System.out.println("Error in server: " + e.getMessage());
            // Continue to the next iteration of the loop
        } finally {
            // Close the receiving socket in the final block to ensure proper cleanup
            receiveSocket.close();
        }
    }

    /**
     * determines if data received by the Server is valid or not
     *
     * @param data
     * @return
     */
    private boolean isValidRequest(String data) {


        System.out.println("Received Packet Content: " + (data));

        return data.length() >= 4 && (data.startsWith("01") || data.startsWith("02"));

    }


    /**
     * Main thread for Server Class
     *
     * @param args
     * @throws SocketException
     */

    public static void main(String[] args) throws SocketException {

        Server server = new Server();
        server.start();
    }


}
