import java.net.*;

public class Server {

        DatagramPacket sendPacket, receivePacket;
        DatagramSocket receiveSocket;




        Server(int port) throws SocketException {
            try {

                receiveSocket = new DatagramSocket(69);
            }catch(Exception e){

                System.out.println("Oops!");
            }
        }


    public void start() {
        try {
            while (true) {
                // Wait to receive a request
                byte[] requestBuffer = new byte[100];
                DatagramPacket requestPacket = new DatagramPacket(requestBuffer, requestBuffer.length);
                receiveSocket.receive(requestPacket);

                // Print received information
                System.out.println("Received Request: " + new String(requestPacket.getData()));

                // Parse the packet to confirm its validity
                if (isValidRequest(requestPacket.getData())) {
                    // If the packet is a valid read request, send back 0 3 0 1 (exactly four bytes)
                    // If the packet is a valid write request, send back 0 4 0 0 (exactly four bytes)
                    byte[] responseBytes = (requestPacket.getData()[1] == 1) ? new byte[]{0, 3, 0, 1} : new byte[]{0, 4, 0, 0};

                    // Print response packet information
                    System.out.println("Sending Response: " + new String(responseBytes));

                    DatagramSocket responseSocket = new DatagramSocket();

                    // Send the packet via the new socket to the port it received the request from
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseBytes, responseBytes.length,
                            requestPacket.getAddress(), requestPacket.getPort()
                    );
                    responseSocket.send(responsePacket);

                    // Close the socket used for this response
                    responseSocket.close();
                } else {
                    // If the packet is invalid, throw an exception and quit
                    throw new IllegalArgumentException("Invalid request packet");
                }
            }
        } catch (Exception e) {
            System.out.println("Error in server: " + e.getMessage());
        } finally {
            // Close the receiving socket in the finally block to ensure proper cleanup
            receiveSocket.close();
        }
    }

    private boolean isValidRequest(byte[] requestData) {
        // Implement logic to validate the request packet format
        // For simplicity, assume the format is valid if it contains 0 1 or 0 2
        return requestData.length >= 4 && (requestData[1] == 1 || requestData[1] == 2);
    }

    public static void main(String[] args) throws SocketException {
        // Specify the port number (e.g., 69)
        int port = 69;

        Server server = new Server(port);
        server.start();
    }





}
