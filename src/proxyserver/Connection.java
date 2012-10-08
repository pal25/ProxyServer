/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proxyserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This is class is an HTTP connection. It contains two streams input and
 * output. recvLine = The stream we are reading in. sendLine = The stream we are
 * writing to.
 *
 * @author pal25
 */
public class Connection {

    private Socket socket = null;
    private BufferedInputStream recvLine = null;
    private byte[] inputBuffer;
    public int inputLength = 0;
    
    private BufferedOutputStream sendLine = null;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;

        recvLine = new BufferedInputStream(this.socket.getInputStream());
        sendLine = new BufferedOutputStream(this.socket.getOutputStream());
    }

    public void closeConnection() throws IOException {
        sendLine.close();
        recvLine.close();
        socket.close();
    }

    // Keep reading bytes until we get \n\r\n
    public void getData() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte curByte;
        int state = 0;

        for (int i = 0; state != 3; i++) {
            curByte = (byte) recvLine.read();
            out.write(curByte);
            inputLength++;
            
            //Keep a state machine to see if we are at end of msg
            if(curByte == (byte) '\n' && state == 0) {
                state = 1;
            }
            else if(curByte == (byte) '\r' && state == 1) {
                state = 2;
            }
            else if(curByte == (byte) '\n' && state == 2) {
                inputBuffer = out.toByteArray();
                out.close();
                state = 3;
                break;
            }
            else {
                state = 0;
            }
        }
    }

    public String readData() {

        return new String(inputBuffer);
    }

    public void write(String str, int len) throws IOException {
        byte[] buffer = str.getBytes();
        System.out.println(str);
        sendLine.write(buffer, 0, len);
    }

    public void flush() throws IOException {
        sendLine.flush();
    }
}
