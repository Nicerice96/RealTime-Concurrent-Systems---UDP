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

    int writeOrReadInteger;



    Client(){
        try {
            sendReceiveSocket = new DatagramSocket(10);
        }catch(Exception e){

            System.out.println("ERROR :: CLIENT:: CONSTRUCTOR");
        }
    }

    public void ReadWriteRequest(String filename, String mode) {

        //Read : [0, 1, filename, 0, mode, 0]
        //Write : [0, 2, filename, 0, mode, 0]

        try {
            for (int i = 0; i <= 11; i++) {

                System.out.println("Iteration : " + i);


                if(i == 11){

                    datagramPacket = invalidRequest();
                    System.out.println("Client is has reached 11th iteration...: " + new String(datagramPacket.getData()));

                }
                else if (writeOrReadInteger % 2 == 0) {
                    datagramPacket = generateRequest(filename, mode);

                    System.out.println("Client is sending Write Request...: " + new String(datagramPacket.getData()));
                }
                else{
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


    public DatagramPacket generateRequest(String filename, String mode){

//            Random random = new Random();
//            int rand = random.nextInt(2);
//
//            String requestData = ((rand == 0)? "01" + filename + "\0" + mode + "\0"  : "02" +  filename + "\0" + mode + "\0");
//
//            byte [] requestDataBytes = requestData.getBytes();
//
//            return new DatagramPacket(requestDataBytes, requestDataBytes.length);

        String requestData;

        if (writeOrReadInteger % 2 == 0){

            requestData = "01" + filename + "\0" + mode + "\0";

        }
        else{
            requestData = "02" +  filename + "\0" + mode + "\0";
        }
        byte [] requestDataBytes = requestData.getBytes();
        writeOrReadInteger++;

        return new DatagramPacket(requestDataBytes, requestDataBytes.length);
    }

    public static void main(String [] args){
        Client c = new Client();
        c.ReadWriteRequest("someStupidFile.txt", "someStupidMode");
    }



    public DatagramPacket invalidRequest(){

        String invalidRequest = "Invalid Request";
        byte [] invalidRequestBytes = invalidRequest.getBytes();


        return new DatagramPacket(invalidRequestBytes, invalidRequestBytes.length);

    }

}
