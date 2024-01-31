import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Random;

public class Client {


//    public static void main (String [] args) throws IOException {
//        Socket clientSocket = new Socket("localhost", 4999);
//        System.out.println("Client sent a signal!");
//    }


    DatagramPacket datagramPacket;
    DatagramSocket sendReceiveSocket;

    Client(){
        try {
            sendReceiveSocket = new DatagramSocket();
        }catch(Exception e){

            System.out.println("ERROR :: CLIENT:: CONSTRUCTOR");
        }
    }

    public void ReadWriteRequest(String filename, String mode) {

        //Read : [0, 1, filename, 0, mode, 0]
        //Write : [0, 2, filename, 0, mode, 0]

        try {
            for (int i = 0; i < 11; i++) {


                datagramPacket = generateRequest(filename, mode);

                System.out.println("Client is sending Request...: " + new String(datagramPacket.getData()));

                datagramPacket.setAddress(InetAddress.getLocalHost());
                datagramPacket.setPort(23);
                sendReceiveSocket.send(datagramPacket);


                byte[] incomingRequestBuffer = new byte[100];
                DatagramPacket incomingRequest = new DatagramPacket(incomingRequestBuffer, incomingRequestBuffer.length);

                sendReceiveSocket.receive(incomingRequest);

                System.out.println("Recieved Response...: " + new String(incomingRequest.getData()));


            }

            sendReceiveSocket.close();

        } catch (Exception e) {

            System.out.println("ERROR :: CLIENT :: ReadWriteRequest");
        }
    }


    public DatagramPacket generateRequest(String filename, String mode){

            Random random = new Random();
            int rand = random.nextInt(2);

            String requestData = ((rand == 0)? "01" : "02" +  filename + "\0" + mode + "\0");

            byte [] requestDataBytes = requestData.getBytes();

            return new DatagramPacket(requestDataBytes, requestDataBytes.length);

    }

    public static void main(String [] args){
        Client c = new Client();
        c.ReadWriteRequest("someStupidFile.txt", "someStupidMode");
    }

}
