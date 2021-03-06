package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;

public class Server {
  public static final int TCP_PORT = 9999;
  
  public static void main(String[] args){
    try{
      ServerSocket ss = new ServerSocket(TCP_PORT);
      ServerThreadManager stm = new ServerThreadManager();
      try{
        File file = new File("server/gotovi_proizvodi");
        file.mkdir();
        
      }catch(Exception e){
        e.printStackTrace();
      }
      while(true){
        Socket sock = ss.accept();
        ServerThread st = new ServerThread(sock, stm);
      }
    } catch(Exception e){
      e.printStackTrace();
    }
  }
  
}
