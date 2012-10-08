package proxyserver;

import com.google.common.base.Splitter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author pal25
 */
public class Request {

    public enum request_method {

        HEAD,
        GET,
        POST,
        PUT,
        DELETE,
        TRACE,
        OPTIONS,
        CONNECT,
        PATCH,
        UNKNOWN
    }
    private request_method method;
    private String resource;
    private String httpVersion;
    private String host;
    private HashMap<String, String> headers;

    public Request() {
    }

    public Request(String req_text) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(req_text));

        String line = reader.readLine();
        Iterator iter = Splitter.on(" ").split(line).iterator(); //Split string and iterate over the values
        this.method = _parseMethod((String) iter.next());
        this.resource = (String) iter.next();
        this.httpVersion = (String) iter.next();

        this.host = _parseRequestHost(reader.readLine());

        headers = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            iter = Splitter.on(": ").split(line).iterator();
            String key = (String) iter.next();
            String value = (String) iter.next();
            this.headers.put(key, value);
        }
    }

    private request_method _parseMethod(String req_method) {
        switch (req_method.toUpperCase()) {
            case "HEAD":
                return request_method.HEAD;
            case "GET":
                return request_method.GET;
            case "POST":
                return request_method.POST;
            case "PUT":
                return request_method.PUT;
            case "DELETE":
                return request_method.DELETE;
            case "TRACE":
                return request_method.TRACE;
            case "OPTIONS":
                return request_method.OPTIONS;
            case "CONNECT":
                return request_method.CONNECT;
            case "PATCH":
                return request_method.PATCH;
            default:
                return request_method.UNKNOWN;

        }
    }

    private String _parseRequestHost(String req_text) {
        System.out.println(req_text);
        Iterator iter = Splitter.on(": ").split(req_text).iterator();
        if ("Host".compareTo((String) iter.next()) == 0) {
            return (String) iter.next();
        }

        return "prod-snscholar.case.edu";
    }
}
