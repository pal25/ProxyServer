/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pal25
 */
public class ProxyServerThread {

    private Connection client;
    private Connection server;

    public ProxyServerThread(Socket socket) throws IOException {
        client = new Connection(socket);
    }

    public void run() {
        try {
            client.getData();
            String input = client.readData();
            System.out.println(input);

            System.out.println("geting inet addr");
            server = new Connection(new Socket("129.22.170.87", 80));

            System.out.println("writing...");
            server.write(client.readData(), client.inputLength);
            server.flush();
            server.getData();
            System.out.println(server.readData());

            client.closeConnection();
            server.closeConnection();

        } catch (IOException ex) {
            Logger.getLogger(ProxyServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
