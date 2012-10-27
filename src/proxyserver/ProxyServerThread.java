/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.IOException;
import java.net.*;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author pal25
 */
public class ProxyServerThread implements Runnable {

    private Socket senderSkt;
    private Socket receiverSkt;

    public ProxyServerThread(Socket socket) throws IOException {
        senderSkt = socket;
    }

    @Override
    public void run() {
        try {   
            Request senderRequest = new Request(senderSkt.getInputStream());
            senderRequest.addHeader("X-Forwarded-For", senderSkt.getInetAddress().toString());
            
            receiverSkt = new Socket(InetAddress.getByName(senderRequest.getHost()), 80);
            IOUtils.write(senderRequest.rebuildRequest(), receiverSkt.getOutputStream());
            
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
