import java.io.IOException;
import java.net.*;

public class Intermediate {

    DatagramPacket sendPacket, receivePacket;
    DatagramSocket sendReceiveSocket;
    DatagramSocket receiveSocket;


    Intermediate(){

        try {


            receiveSocket = new DatagramSocket(23);
            sendReceiveSocket = new DatagramSocket();

        }catch(Exception e){

            System.out.println("Oops!");
        }

    }

    public void Receive(){
        try {
            byte[] incomingMessage = new byte[100];

            receivePacket = new DatagramPacket(incomingMessage, incomingMessage.length);

            sendReceiveSocket.receive(receivePacket);


            System.out.println("Received Request: " + new String(receivePacket.getData()));
        }
            catch(Exception e){

                System.out.println("ERROR :: INTERMEDIATE :: Receive()");

            }
    }


    public void Forward() {
        try {
            sendPacket = new DatagramPacket(receivePacket.getData(), receivePacket.getLength(), InetAddress.getLocalHost(), 69);
            System.out.println("Sending Request to Server: " + new String(sendPacket.getData()));
            sendReceiveSocket.send(sendPacket);


        }catch(Exception e){

            System.out.println("ERROR :: INTERMEDIATE :: Forward()");

        }
    }

    public void Start(){


        try{

            Receive();
            Forward();

        }catch(Exception e){


        }





    }


    public static void main(String [] args) throws IOException {


    }





}
