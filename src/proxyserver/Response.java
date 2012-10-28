package proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pal25
 */
public class Response {

    private HashMap<String, String> headers;
    private String responseText;
    private String responseStatus;
    private String responseVersion;
    private String content;

    public Response(InputStream input) throws IOException {
        List<String> lines = readHeaderLines(input);

        Iterator iter = lines.iterator();
        if (iter.hasNext() == false) {
            throw new IOException("No Request Received");
        } else {
            String firstLine = (String) iter.next();
            String[] fields = firstLine.split(" ", 3);
            responseVersion = fields[0];
            responseStatus = fields[1];
            responseText = fields[2];
            iter.remove();
        }

        headers = new HashMap<>();
        for (String line : lines) {
            headers.putAll(parseHeaderLine(line));
        }

        if (headers.get("Content-Length") != null) {
            content = readContent(input, Integer.parseInt(headers.get("Content-Length"), 10));
        } else {
            content = readChunkedContent(input);
        }
    }

    private String readChunkedContent(InputStream input) throws IOException {
        List<String> dataList = new LinkedList<>();
        StringBuilder str = new StringBuilder();
        int chunkSize;
        int state = 0;
        
        while (true) {
            switch (state) {
                case (0): // Reading chunk size
                    byte[] chunkSizeValue = new byte[1];
                    str.setLength(0);
                    while (input.read(chunkSizeValue) != -1) {
                        if (chunkSizeValue[0] == '\r') {
                            break;
                        }
                        str.append((new Integer(Integer.parseInt(new String(chunkSizeValue), 16))).toString());
                    }
                    input.read(chunkSizeValue); //Read extra \n on each line
                    
                    dataList.add(str.toString());
                    chunkSize = Integer.parseInt(str.toString(), 10);
                    if (chunkSize > 0) {
                        state = 1;
                    } else {
                        state = 2;
                    }
                    break;

                case (1): // Reading data from chunk
                    chunkSize = Integer.parseInt(str.toString(), 10);
                    byte[] dataValue = new byte[chunkSize];
                    input.read(dataValue, 0, chunkSize);
                    
                    dataList.add(dataValue.toString());
                    
                    state = 0;
                    break;

                case (2): // Finalizing chunked data
                    str.setLength(0);
                    
                    for (String line : dataList) {
                        str.append(line);
                    }
                    
                    return str.toString();
            }
        }
    }

    private String readContent(InputStream input, int contentLength) throws IOException {
        byte[] value = new byte[contentLength];
        input.read(value, 0, contentLength);

        return new String(value);
    }

    private List<String> readHeaderLines(InputStream input) throws IOException {
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

    private HashMap<String, String> parseHeaderLine(String line) {
        String[] splitValues;
        splitValues = line.split(": ", 2);

        HashMap<String, String> temp = new HashMap<>(); //Hack-y version of tuple
        temp.put(splitValues[0], splitValues[1]);

        return temp;
    }

    public String getResponseText() {
        return responseText;
    }

    public String getResponseVersion() {
        return responseVersion;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public String getContent() {
        return content;
    }
    
    public String rebuildResponse() throws UnsupportedEncodingException {
        StringBuilder out = new StringBuilder();
        out.append(responseVersion).append(" ").append(responseStatus).append(" ").append(responseText);
        out.append("\r\n");
        
        for (Map.Entry<String, String> entry: headers.entrySet()) {
            out.append(entry.getKey());
            out.append(": ");
            out.append(entry.getValue());
            out.append("\r\n");
        }
        out.append("\r\n"); //To note end of headers
        out.append(content);
        
        return out.toString();
    }
    
    public String getHeaderField(String key) {
        if (headers.get(key) != null) {
            return headers.get(key);
        } else {
            return "";
        }
    }
}
