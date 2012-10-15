/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author pal25
 */
public class ProxyServerThread implements Runnable {

    private Connection client;
    private Connection server;

    public ProxyServerThread(Socket socket) throws IOException {
        client = new Connection(socket);
    }

    @Override
    public void run() {
        //TODO   
    }
}
