package proxyserver;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        ExecutorService pool = Executors.newFixedThreadPool(50);
        ServerSocket proxyServer = new ServerSocket(PORT);
        
        while(listening) {
            Socket connectionSocket = proxyServer.accept();
            pool.execute(new ProxyServerThread(connectionSocket));
        }
        
    }
}
