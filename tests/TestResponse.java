/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import proxyserver.Response;

/**
 *
 * @author pal25
 */
public class TestResponse {
    
    Response testResponse;
    Response testChunkResponse;
       
   @Before 
   public void setUp() throws FileNotFoundException, IOException {
       InputStream fakeStream = new FileInputStream("C:\\Users\\pal25\\Desktop\\ProxyServer\\tests\\FakeResponse.txt");
       InputStream fakeChunkStream = new FileInputStream("C:\\Users\\pal25\\Desktop\\ProxyServer\\tests\\FakeResponse.txt");
       testResponse = new Response(fakeStream);
       testChunkResponse = new Response(fakeChunkStream);
   } 
   
   @Test
   public void test_getResponseText() {
       assertTrue(testResponse.getResponseText().equals("OK"));
   }
   
   @Test
   public void test_getResponseStatus() {
       assertTrue(testResponse.getResponseStatus().equals("200"));
   }
   
   @Test
   public void test_getResponseVersion() {
       assertTrue(testResponse.getResponseVersion().equals("HTTP/1.1"));
   }
   
   @Test
   public void test_getContent() {
       assertTrue(testResponse.getContent().equals("<html>Test!</html>"));
   }
   
   @Test
   public void test_getChunkResponseText() {
       assertTrue(testResponse.getResponseText().equals("OK"));
   }
   
   @Test
   public void test_getChunkResponseStatus() {
       assertTrue(testResponse.getResponseStatus().equals("200"));
   }
   
   @Test
   public void test_getChunkResponseVersion() {
       assertTrue(testResponse.getResponseVersion().equals("HTTP/1.1"));
   }
   
   @Test
   public void test_getChunkContent() {
       assertTrue(testResponse.getContent().equals("<html>Test!</html>"));
   }
}
