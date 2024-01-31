import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class Server {

        DatagramPacket sendPacket, receivePacket;
        DatagramSocket receiveSocket;

        Server() throws SocketException {
            try {

                receiveSocket = new DatagramSocket(69);
            }catch(Exception e){

                System.out.println("Oops!");
            }
        }



    public void Send(DatagramPacket requestPacket, String requestPacketData) throws IOException {
        if (isValidRequest(requestPacketData)) {
            byte[] responseData;

            // Extract operation code from the request packet
            String operationCode = requestPacketData.substring(0, 2);

            // Determine response based on the operation code
            if ("01".equals(operationCode)) {
                // Read request, send back 0 3 0 1
                responseData = new byte[]{0, 3, 0, 1};
            } else if ("02".equals(operationCode)) {
                // Write request, send back 0 4 0 0
                responseData = new byte[]{0, 4, 0, 0};
            } else {
                // Invalid operation code, handle accordingly
                System.out.println("Invalid operation code: " + operationCode);
                return;
            }

            // Print response packet information
            System.out.println("Sending Response: " + Arrays.toString(responseData));

            // Send the response packet to the client
            DatagramSocket responseSocket = new DatagramSocket();
            DatagramPacket responsePacket = new DatagramPacket(
                    responseData, responseData.length,
                    requestPacket.getAddress(), requestPacket.getPort()
            );
            responseSocket.send(responsePacket);

            // Close the socket used for this response
            responseSocket.close();
        } else {
            // If the packet is invalid, handle accordingly
            System.out.println("Invalid request packet: " + requestPacketData);
        }
    }




    public void start() {
        try {
            while (true) {
                byte[] requestBuffer = new byte[100];
                DatagramPacket requestPacket = new DatagramPacket(requestBuffer, requestBuffer.length);
                receiveSocket.receive(requestPacket);

                System.out.println("Received Request: " + new String(requestPacket.getData()));

                // Parse the packet to confirm its validity
                Send(requestPacket, new String(requestPacket.getData()));
            }
        } catch (Exception e) {
            System.out.println("Error in server: " + e.getMessage());
            // Continue to the next iteration of the loop
        }
        finally {
            // Close the receiving socket in the finally block to ensure proper cleanup
            receiveSocket.close();
        }
    }

    private boolean isValidRequest(String data) {
        // Implement logic to validate the request packet format
        // For simplicity, assume the format is valid if it contains 0 1 or 0 2

        System.out.println("Received Packet Content: " + (data));



        return data.length() >= 4 && (data.startsWith("01") || data.startsWith("02"));

    }

    public static void main(String[] args) throws SocketException {

        Server server = new Server();
        server.start();
    }





}
