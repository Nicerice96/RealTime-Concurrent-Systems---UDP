import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Intermediate {
    public static void main (String [] args) throws IOException {

        ServerSocket intermediateListeningSocket = new ServerSocket(4999);
        Socket intermediateSocket = intermediateListeningSocket.accept();
        System.out.println("Client sent a message to intermediate!");
        Socket serverSocket = new Socket("localhost", 5000);

    }
}
