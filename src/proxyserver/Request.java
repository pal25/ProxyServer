package proxyserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author pal25
 */
public class Request {

    public HashMap<String, String> headers;
    public String host;

    public Request(InputStream input) throws IOException {
        List<String> lines = readLines(input);

        Iterator iter = lines.iterator();
        if (iter.hasNext() == false) {
            throw new IOException("No Request Received");
        }

        for (String line : lines) {
            headers.putAll(parseLine(line));
        }
        
        host = headers.get("Host");
    }

    private List<String> readLines(InputStream input) throws IOException {
        byte[] value = new byte[1]; //Allocate buffer to read values from InputStream

        List<String> lines = new LinkedList<>();
        StringBuilder str = new StringBuilder();

        while (true) {
            str.setLength(0);
            while (input.read(value) != -1) {
                if (value[0] == '\r') {
                    break;
                }
                str.append(value[0]);
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
        splitValues = StringUtils.split(line, ": ", 2);
        
        HashMap<String, String> temp = new HashMap<>(); //Hack-y version of tuple
        temp.put(splitValues[0], splitValues[1]);

        return temp;

    }
    
}
