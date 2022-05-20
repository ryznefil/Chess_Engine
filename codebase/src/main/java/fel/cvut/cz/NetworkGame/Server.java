package fel.cvut.cz.NetworkGame;

import fel.cvut.cz.GUI.Baners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;
    private InetAddress hostAddress;
    private Socket socket;

    public void createServer(){

        //Get local I.P.
        try{
            hostAddress = InetAddress.getLocalHost();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "I.P. getting fail", e);
            Baners.createErrorBanner("Failed to obtain the I.P.");
        }

        //Create server socket
        try {
            serverSocket = new ServerSocket(2706);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Server port is occupied", ex);
            Baners.createErrorBanner("Server port is already occupied!");
        }

    }

}
