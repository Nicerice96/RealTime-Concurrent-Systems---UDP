import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    public static void main(String [] args) throws IOException {

        ServerSocket serverListeningSocket = new ServerSocket(5000);
        System.out.println("Intermediate sent a signal to Server!");
        Socket serverSocket = serverListeningSocket.accept();

    }

}
