package proxyserver;

import java.util.StringTokenizer;
import java.util.regex.*;
/**
 * 
 * @author pal25
 */
public class Request {
    private static Pattern reqPattern = Pattern.compile("(GET|POST) (.*) (HTTP\\/1\\.1|HTTP\\/1\\.0)");
    
    public String reqType;
    public String url;
    public String version;
    
    public Request(String reqType, String url, String version) {
        this.reqType = reqType;
        this.url = url;
        this.version = version;
    }
    
    public static Request parseRequest(String input) {
        StringTokenizer in = new StringTokenizer(input);
        String[] tokens = new String[3];
        
        for(int i=0;in.hasMoreTokens();i++) {
            tokens[i] = in.nextToken();
        }
        
        return new Request(tokens[0], tokens[1], tokens[2]);
    }
    
    public static void addHeaderField(String input) {
        
    }
    
}
