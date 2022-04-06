package distributedSystems;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String args[]) {
        new Server().openServer();
    }

    /* Define the socket that receives requests */
    ServerSocket providerSocket;

    /* Define the socket that is used to handle the connection */
    Socket connection = null;



    void openServer() {
        try {

            /* Create Server Socket */
            providerSocket = new ServerSocket(4321, 10);

            while (true) {
                /* Accept the connection */
                connection = providerSocket.accept();

                /* Handle the request */
                Thread t = new ActionsForClients(connection);
                t.start();


            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


}
