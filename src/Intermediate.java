import java.net.*;

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

    public void ReceiveAndForward() {
        try {
            while (true) {
                byte[] incomingMessage = new byte[100];
                receivePacket = new DatagramPacket(incomingMessage, incomingMessage.length);
                receiveSocket.receive(receivePacket);

                System.out.println("Received Request: " + new String(receivePacket.getData()));

                Forward();
            }
        } catch (Exception e) {
            System.out.println("ERROR :: INTERMEDIATE :: ReceiveAndForward()");
        }
    }

    public void Forward() {
        try {
            sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), InetAddress.getLocalHost(), 69);
            System.out.println("Sending Request to Server: " + new String(sendPacket.getData()));
            sendReceiveSocket.send(sendPacket);
        } catch (Exception e) {
            System.out.println("ERROR :: INTERMEDIATE :: Forward()");
        }
    }

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

    public static void main(String[] args) throws Exception {
        Intermediate intermediateHost = new Intermediate();
        intermediateHost.Start();
    }
}
