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
public class ProxyServerThread extends Thread {

    private Connection client;
    private Connection server;

    public ProxyServerThread(Socket socket) throws IOException {
        client = new Connection(socket);
    }

    public static String[] parseRequest(String input) {
        StringTokenizer in = new StringTokenizer(input);
        String[] tokens = new String[3];

        for (int i = 0; in.hasMoreTokens(); i++) {
            tokens[i] = in.nextToken();
        }

        return tokens;
    }

    @Override
    public void run() {
        try {
            String input;

            input = client.readLine();
            String[] clientReq = parseRequest(input);
            InetAddress address = InetAddress.getByName(new URL(clientReq[1]).getHost());
            server = new Connection(new Socket(address.getHostAddress(), 80));

            server.writeLine(input);
            System.out.println("Sending: " + input);
//            line = client.readLine();
//            server.writeLine(line);
//            System.out.println("Sending: " + line);

            while (client.ready()) {
                input = client.readLine();
                server.writeLine(input);
                System.out.println("Sending: " + input);
            }
            server.writeLine("\n");
            System.out.println("Pushing to server");
            server.flush();

            while ((input = server.readLine()) != null) {
                client.writeLine(input);
                System.out.println("Receiving: " + input);
            }

            System.out.println("Pushing to client");
            client.flush();

            client.closeConnection();
            server.closeConnection();

        } catch (IOException ex) {
            Logger.getLogger(ProxyServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
