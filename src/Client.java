import java.io.IOException;
import java.net.*;

public class Client {


    public static void main (String [] args) throws IOException {
        Socket clientSocket = new Socket("localhost", 4999);
        System.out.println("Client sent a signal!");
    }
}
