package proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pal25
 */
public class Request {

    private HashMap<String, String> headers;
    private String host;
    private String requestType;
    private String requestURI;
    private String requestVersion;

    public Request(InputStream input) throws IOException {
        List<String> lines = readLines(input);

        Iterator iter = lines.iterator();
        if (iter.hasNext() == false) {
            throw new IOException("No Request Received");
        } else {
            String firstLine = (String) iter.next();
            String[] fields = firstLine.split(" ", 3);
            requestType = fields[0];
            requestURI = fields[1];
            requestVersion = fields[2];

            iter.remove();
        }

        headers = new HashMap<>();
        for (String line : lines) {
            headers.putAll(parseLine(line));
        }

        host = headers.get("Host");
    }

    private List<String> readLines(InputStream input) throws IOException {
        byte[] value = new byte[1]; //Allocate buffer to read values from InputStream

        List<String> lines = new LinkedList<>();
        StringBuilder str = new StringBuilder(); //Damn you immutable strings!

        while (true) {
            str.setLength(0);
            while (input.read(value) != -1) {
                if (value[0] == '\r') {
                    break;
                }
                str.append(new String(value));
            }
            input.read(value); //Read extra \n on each line
            if (str.length() == 0) {
                break;
            }
            lines.add(str.toString());
        }
        return lines;
    }

    private HashMap<String, String> parseLine(String line) {
        String[] splitValues;
        splitValues = line.split(": ", 2);

        HashMap<String, String> temp = new HashMap<>(); //Hack-y version of tuple
        temp.put(splitValues[0], splitValues[1]);

        return temp;

    }

    public String rebuildRequest() {
        StringBuilder out = new StringBuilder();
        out.append(requestType).append(" ").append(requestURI).append(" ").append(requestVersion);
        out.append("\r\n");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            out.append(entry.getKey());
            out.append(": ");
            out.append(entry.getValue());
            out.append("\r\n");
        }
        out.append("\n"); //To note end of request

        return out.toString();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHost() {
        return host;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getRequestVersion() {
        return requestVersion;
    }

    public String getHeaderField(String key) {
        if (headers.get(key) != null) {
            return headers.get(key);
        } else {
            return "";
        }
    }
}
