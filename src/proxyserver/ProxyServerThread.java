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
            
            System.out.println("----Request Received----");
            System.out.println("Host: " + senderRequest.getHost());
            System.out.println("RequestType: " + senderRequest.getRequestType());
            System.out.println("RequestURI: " + senderRequest.getRequestURI());
            System.out.println("RequestVersion: " + senderRequest.getRequestVersion());
            System.out.println("RequestContentType: " + senderRequest.getHeaderField("Accept-Charset"));
            System.out.println("");
            
            receiverSkt = new Socket(InetAddress.getByName(senderRequest.getHost()), 80);
            IOUtils.write(senderRequest.rebuildRequest(), receiverSkt.getOutputStream());
            
            System.out.println("----Request Sent----");
            System.out.println("");
            
            Response receiverResponse = new Response(receiverSkt.getInputStream());
            IOUtils.write(receiverResponse.rebuildResponse(), senderSkt.getOutputStream());
            
            System.out.println("----Response Received----");
            System.out.println("ResponseVersion: " + receiverResponse.getResponseVersion());
            System.out.println("ResponseStatus: " + receiverResponse.getResponseStatus());
            System.out.println("ResponseText: " + receiverResponse.getResponseText());
            System.out.println("ResponseType: " + receiverResponse.getHeaderField("Content-Type"));
            System.out.println("ResponseEncoding: " + receiverResponse.getHeaderField("Content-Encoding"));
            System.out.println("ResponseLength: " + receiverResponse.getHeaderField("Content-Length"));
            System.out.println("Content: " + receiverResponse.getContent());
            System.out.println("");
            
            System.out.println("----Response Sent----");
            System.out.println("");
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
