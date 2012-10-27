/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import proxyserver.Request;

/**
 *
 * @author pal25
 */
public class TestRequest {
    
    Request testRequest;
       
   @Before 
   public void setUp() throws FileNotFoundException, IOException {
       InputStream fakeStream = new FileInputStream("C:\\Users\\pal25\\Desktop\\ProxyServer\\tests\\FakeRequest.txt");
       testRequest = new Request(fakeStream);
   } 
   
   @Test
   public void test_getHost() {
       assertTrue(testRequest.getHost().equals("prod-snscholar.case.edu"));
   }
   
   @Test
   public void test_getRequestType() {
       assertTrue(testRequest.getRequestType().equals("GET"));
   }
   
   @Test
   public void test_getRequestURI() {
       assertTrue(testRequest.getRequestURI().equals("/index.html"));
   }
   
   @Test
   public void test_getRequestVersion() {
       assertTrue(testRequest.getRequestVersion().equals("HTTP/1.1"));
   }
}
