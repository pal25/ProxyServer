/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This is class is an HTTP connection.
 * It contains two streams input and output.
 * recvLine = The stream we are reading in.
 * sendLine = The stream we are writing to.
 * @author pal25
 */
public class Connection {

    private Socket socket = null;
    private BufferedReader recvLine = null;
    private DataOutputStream sendLine = null;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;

        recvLine = new BufferedReader(
                new InputStreamReader(
                this.socket.getInputStream()));

        sendLine = new DataOutputStream(this.socket.getOutputStream());
        sendLine.flush();
    }

    public void closeConnection() throws IOException {
        sendLine.close();
        recvLine.close();
        socket.close();
    }

    public String readLine() throws IOException {
        return recvLine.readLine();
    }
    
    public boolean ready() throws IOException {
        return recvLine.ready();
    }

    public void writeLine(String line) throws IOException {
        line = line + "\r\n";
        sendLine.writeBytes(line);
    }
    
    public void flush() throws IOException {
        sendLine.flush();
    }
}
