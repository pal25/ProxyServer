package proxyserver;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author pal25
 */
public class ProxyServer {

    public static final int PORT = 6501;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        boolean listening = true;
        ServerSocket proxyServer = new ServerSocket(PORT);
        
        while(listening) {
            new ProxyServerThread(proxyServer.accept()).start();
        }
        
    }
}
