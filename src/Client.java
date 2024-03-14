import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;

public class Client {



    private DatagramSocket sendReceiveSocket;
    private int writeOrReadInteger;

    private SharedDataInterface sharedData;

    public Client(SharedDataInterface sharedData) {
        try {
            sendReceiveSocket = new DatagramSocket(10);
            this.sharedData = sharedData;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public void sendData(int i, String filename, String mode) {

        try {
            DatagramPacket datagramPacket;

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
            datagramPacket.setPort(23); // Port for the Intermediate
            sendReceiveSocket.send(datagramPacket);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public void run(String filename, String mode) {
        try {
            for (int i = 0; i <= 11; i++) {
                System.out.println("Iteration : " + i);

                sendData(i, filename, mode);

                acknowledgeIntermediateReply();

                Thread.sleep(5 * 1000);

                requestIntermediate();

                //finally recieve Server processed data
                byte[] receieveIntermediateData = new byte[4];
                DatagramPacket receieveIntermediateDataPacket = new DatagramPacket(receieveIntermediateData, receieveIntermediateData.length);
                sendReceiveSocket.receive(receieveIntermediateDataPacket);
                System.out.println("Client received" + Arrays.toString(receieveIntermediateDataPacket.getData()));

                System.out.println("----------------------------------------------------------------------------------------------");


            }

            sendReceiveSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void requestIntermediate() {
        try {
            String clientRequest = "Client is requesting data...";
            byte[] requestIntermediatebytes = clientRequest.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(requestIntermediatebytes, requestIntermediatebytes.length, InetAddress.getLocalHost(), 23);
            sendReceiveSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acknowledgeIntermediateReply() {

        try {
            byte[] incomingResponseBuffer = new byte[1024];
            DatagramPacket incomingResponse = new DatagramPacket(incomingResponseBuffer, incomingResponseBuffer.length);
            sendReceiveSocket.receive(incomingResponse);
            System.out.println("Received Intermediate Reply...: " + new String(incomingResponse.getData()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public DatagramPacket generateRequest(String filename, String mode) {
        String requestData;
        if (writeOrReadInteger % 2 == 0) {
            requestData = "01" + filename + "\0" + mode + "\0"; // Write request
        } else {
            requestData = "02" + filename + "\0" + mode + "\0"; // Read request
        }
        byte[] requestDataBytes = requestData.getBytes();
        writeOrReadInteger++;
        return new DatagramPacket(requestDataBytes, requestDataBytes.length);
    }

    public DatagramPacket invalidRequest() {
        String invalidRequest = "Invalid Request";
        byte[] invalidRequestBytes = invalidRequest.getBytes();
        return new DatagramPacket(invalidRequestBytes, invalidRequestBytes.length);
    }

    public static void main(String[] args) {
        try {
            SharedDataInterface sharedData = (SharedDataInterface) Naming.lookup("rmi://localhost/SharedData");

            Client client = new Client(sharedData);
            client.run("someFile.txt", "someMode");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}