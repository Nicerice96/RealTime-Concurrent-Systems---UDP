import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * Constructor : Receives data from the Client class and forwards them to the Server Class
 *
 * @author Zarif
 * @version 1.0
 */
public class Intermediate {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket;
    DatagramSocket receiveSocket;

    Intermediate() {
        try {
            receiveSocket = new DatagramSocket(23);
            sendReceiveSocket = new DatagramSocket();
        } catch (Exception e) {
            System.out.println("Oops!");
        }
    }

    /**
     * Takes in data received from Client and forwards it to Server; Takes in data from Server and forwards it to the client
     */

    public void ReceiveAndForward() {
        try {
            while (true) {
                byte[] incomingMessage = new byte[50];
                receivePacket = new DatagramPacket(incomingMessage, incomingMessage.length);
                receiveSocket.receive(receivePacket);

                System.out.println("Received Request: " + new String(receivePacket.getData()));

                Forward(69);

                sleep(1000);

                sendBack();
            }
        } catch (Exception e) {
            System.out.println("ERROR :: INTERMEDIATE :: ReceiveAndForward()");
        }
    }


    /**
     * Forwards data to desired port
     *
     * @param port
     */

    public void Forward(int port) {

        if (port == 69) {
            try {
                sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), InetAddress.getLocalHost(), port);
                System.out.println("Sending Request to Server: " + new String(sendPacket.getData()));
                sendReceiveSocket.send(sendPacket);
            } catch (Exception e) {
                System.out.println("ERROR :: INTERMEDIATE :: Forward()");
            }
        }
        if (port == 10) {
            try {
                sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), InetAddress.getLocalHost(), port);
                System.out.println("Sending Request Client: " + Arrays.toString(sendPacket.getData()));
                sendReceiveSocket.send(sendPacket);
            } catch (Exception e) {
                System.out.println("ERROR :: INTERMEDIATE :: Forward()");
            }

        }
    }

    /**
     * Peers into Intermediate socket so that it may send it back to Client
     */

    public void sendBack() {


        try {
            byte[] incomingMessage = new byte[20];
            receivePacket = new DatagramPacket(incomingMessage, incomingMessage.length);
            receiveSocket.receive(receivePacket);

            Forward(10);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Begins Intermediate Class behaviour
     */

    public void Start() {
        try {
            ReceiveAndForward();
        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details
        } finally {
            receiveSocket.close();
            sendReceiveSocket.close();
        }
    }

    /**
     * Main thread for Intermediate Class
     *
     * @param args
     * @throws Exception
     */

    public static void main(String[] args) throws Exception {
        Intermediate intermediateHost = new Intermediate();
        intermediateHost.Start();
    }
}
