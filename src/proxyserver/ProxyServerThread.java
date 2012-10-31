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

//            System.out.println("----Request Received----");
//            System.out.println("Host: " + senderRequest.getHost());
//            System.out.println("RequestType: " + senderRequest.getRequestType());
//            System.out.println("RequestURI: " + senderRequest.getRequestURI());
//            System.out.println("RequestVersion: " + senderRequest.getRequestVersion());
//            System.out.println("RequestContentType: " + senderRequest.getHeaderField("Accept-Charset"));
//            System.out.println("");

            receiverSkt = new Socket(InetAddress.getByName(senderRequest.getHost()), 80);
            IOUtils.write(senderRequest.rebuildRequest(), receiverSkt.getOutputStream());

//            System.out.println("----Request Sent----");
//            System.out.println("");

            Response recvResponse = new Response(receiverSkt.getInputStream());
            IOUtils.write(recvResponse.rebuildHeadersResponse(), senderSkt.getOutputStream());

            if ("".equals(recvResponse.getHeaderField("Content-Length"))) {
                StringBuilder str = new StringBuilder();
                int chunkSize = 0;
                int state = 0;

                while (true) {
                    if (state == 0) {
                        byte[] chunkSizeValue = new byte[1];
                        str.setLength(0);
                        while (receiverSkt.getInputStream().read(chunkSizeValue) != -1) {
                            if (chunkSizeValue[0] == '\r') {
                                break;
                            }
                            str.append(new String(chunkSizeValue));
                        }
                        receiverSkt.getInputStream().read(chunkSizeValue); //Read extra \n on each line

                        if (str.toString().length() == 0) {
                            chunkSize = 0;
                        } else {
                            chunkSize = Integer.parseInt(str.toString().toUpperCase(), 16);
                        }

                        if (chunkSize > 0) {
                            state = 1;
                        } else {
                            break;
                        }
                    } else if (state == 1) {
                        IOUtils.copyLarge(receiverSkt.getInputStream(), senderSkt.getOutputStream(), 0L, chunkSize);
                        state = 0;
                    }
                }
            } else {
                IOUtils.copyLarge(receiverSkt.getInputStream(), senderSkt.getOutputStream(), 0L, Long.parseLong(recvResponse.getHeaderField("Content-Length")));
            }

//            System.out.println("----Response Received----");
//            System.out.println("ResponseVersion: " + recvResponse.getResponseVersion());
//            System.out.println("ResponseStatus: " + recvResponse.getResponseStatus());
//            System.out.println("ResponseText: " + recvResponse.getResponseText());
//            System.out.println("ResponseType: " + recvResponse.getHeaderField("Content-Type"));
//            System.out.println("ResponseEncoding: " + recvResponse.getHeaderField("Content-Encoding"));
//            System.out.println("ResponseLength: " + recvResponse.getHeaderField("Content-Length"));
//            System.out.println("");
//
//            System.out.println("----Response Sent----");
//            System.out.println("");

            senderSkt.close();
            receiverSkt.close();

        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
}
