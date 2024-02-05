import java.net.*;
import java.util.Arrays;

/**
 * Generates messages in alternating sequence to be sent to the Intermediate class
 *
 * @author Zarif
 * @version 1.0
 */

public class Client {

    DatagramPacket datagramPacket;
    DatagramSocket sendReceiveSocket;
    int writeOrReadInteger;


    /**
     * Constructor : Initializes the Client socket to port "10"
     */
    Client() {
        try {
            sendReceiveSocket = new DatagramSocket(10);
        } catch (Exception e) {

            System.out.println("ERROR :: CLIENT:: CONSTRUCTOR " + e);
        }
    }

    /**
     * Sends data to Intermediate class, and receives data from Intermediate
     *
     * @param filename
     * @param mode
     */

    public void ReadWriteRequest(String filename, String mode) {

        //Read : [0, 1, filename, 0, mode, 0]
        //Write : [0, 2, filename, 0, mode, 0]

        try {
            for (int i = 0; i <= 11; i++) {

                System.out.println("Iteration : " + i);


                if (i == 11) {

                    datagramPacket = invalidRequest();
                    System.out.println("Client has reached 11th iteration...: " + new String(datagramPacket.getData()));

                } else if (writeOrReadInteger % 2 == 0) {
                    datagramPacket = generateRequest(filename, mode);

                    System.out.println("Client is sending Write Request...: " + new String(datagramPacket.getData()));
                } else {
                    datagramPacket = generateRequest(filename, mode);
                    System.out.println("Client is sending Read Request...: " + new String(datagramPacket.getData()));
                }


                datagramPacket.setAddress(InetAddress.getLocalHost());
                datagramPacket.setPort(23);
                sendReceiveSocket.send(datagramPacket);


                byte[] incomingRequestBuffer = new byte[100];
                DatagramPacket incomingRequest = new DatagramPacket(incomingRequestBuffer, incomingRequestBuffer.length);

                sendReceiveSocket.receive(incomingRequest);

                System.out.println("Recieved Response...: " + Arrays.toString(incomingRequest.getData()));


            }

            sendReceiveSocket.close();

        } catch (Exception e) {

            System.out.println("ERROR :: CLIENT :: ReadWriteRequest");
        }
    }

    /**
     * generates a Write or Read request in alternating form
     *
     * @param filename
     * @param mode
     * @return
     */


    public DatagramPacket generateRequest(String filename, String mode) {

        String requestData;

        if (writeOrReadInteger % 2 == 0) {

            requestData = "01" + filename + "\0" + mode + "\0";

        } else {
            requestData = "02" + filename + "\0" + mode + "\0";
        }
        byte[] requestDataBytes = requestData.getBytes();
        writeOrReadInteger++;

        return new DatagramPacket(requestDataBytes, requestDataBytes.length);
    }

    /**
     * generates an invalid request message
     *
     * @return
     */
    public DatagramPacket invalidRequest() {

        String invalidRequest = "Invalid Request";
        byte[] invalidRequestBytes = invalidRequest.getBytes();


        return new DatagramPacket(invalidRequestBytes, invalidRequestBytes.length);

    }

    /**
     * Main thread for Client class
     *
     * @param args
     */

    public static void main(String[] args) {
        Client c = new Client();
        c.ReadWriteRequest(" someStupidFile.txt ", " someStupidMode ");
    }

}
