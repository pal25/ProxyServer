package proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
        HashMap<String, Object> response = readLines(input);

        String[] fields = ((String) response.get("first")).split(" ", 3);
        responseVersion = fields[0];
        responseStatus = fields[1];
        responseText = fields[2];

        headers = new HashMap<>();
        for (String line : (List<String>) response.get("headers")) {
            headers.putAll(parseHeaderLine(line));
        }

        StringBuilder str = new StringBuilder();
        for (String line : (List<String>) response.get("content")) {
            str.append(line).append("\n");
        }
        content = str.toString();
    }

    private HashMap<String, Object> readLines(InputStream input) throws IOException {
        HashMap<String, Object> content = new HashMap<>();
        List<String> lines = new LinkedList<>();

        byte[] value = new byte[1]; //Allocate buffer to read values from InputStream
        StringBuilder str = new StringBuilder();

        boolean statuslineParsed = false;
        boolean headersParsed = false;

        while (true) {
            str.setLength(0);
            while (input.read(value) != -1) {
                if (value[0] == '\r') {
                    break;
                }
                str.append(new String(value));
            }
            input.read(value);

            if (!statuslineParsed) {
                content.put("first", str.toString());
                statuslineParsed = true;
            } else {
                if (str.length() == 0) {
                    if (headersParsed) {
                        content.put("content", lines);
                        lines.clear();
                        break;
                    } else {
                        content.put("headers", lines);
                        lines.clear();
                        headersParsed = true;
                    }
                }
                lines.add(str.toString());
            }
        }
        return content;
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
}
