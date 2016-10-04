package src.server;

import java.io.*;
import java.lang.System.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.text.spi.BreakIteratorProvider;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import src.classes.*;
import src.proizvodi.*;

public class ServerThread extends Thread {
  
  private Socket sock;
  private PrintWriter out;
  private BufferedReader in;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private ArrayList<String> korisnici = new ArrayList<>();
  private static int id = 15489;
  private MasinaZaIzraduDrveneKonstrukcije mdk;
  private MasinaZaIzraduPVCKonstrukcije mpk;
  private MasinaZaIzraduStakla mis;
  private MasinaZaIzraduMehanizmaZaOtvaranje mmo;
  private boolean postojiStaklo = false;
  private ArrayList<Poluproizvod> poluproizvodi = new ArrayList<>();
  private static ServerThreadManager stm;
  private Vrata novaVrata;
  private SigurnosnaVrata novaSigurnosnaVrata;
  private Prozor noviProzor;
  private static ArrayDeque<String> listaNarudzbi = new ArrayDeque<>();
  private int idList;
  private KrovniProzor krovniProzor;
  private static ArrayList<Proizvod> gotoviProizvodi = new ArrayList<>();
  
  public ServerThread(Socket sock, ServerThreadManager stm) {
    this.sock = sock;
    this.stm = stm;
    
    korisnici.add("tamara-1234");
    korisnici.add("tamara1-1234");
    korisnici.add("tamara2-1234");
    
    try {
      in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
      out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    start();
  }
  
  public void run() {
    try {
      String filijala = "";
      
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
      String pocetak = "";
      
      boolean prijavljen = false;
      while (!prijavljen) {
        filijala = in.readLine();
        if (korisnici.contains(filijala)) {
          out.println("1");
          prijavljen = true;
        } else {
          out.println("0");
        }
      }

      boolean krajNarucivanja = false;
      while (!krajNarucivanja) {
        // sta se narucuje
        String narudzba = in.readLine();
        String podaciONarudzbi = in.readLine();
        boolean naCekanjuPrazna = false;
        
        if (!narudzba.equals("6")) {
          listaNarudzbi.offer(narudzba + "#" + podaciONarudzbi);
          idList++;
          pocetak = sdf.format(date);
          stm.staviNaListuCekanja(narudzba + "#" + podaciONarudzbi + "vrijeme narudzbe:" + pocetak  + ":" +  filijala + "\n");
        }else{
          System.out.println("Nema vise narudzbi");
          listaNarudzbi.offer("6#0");
        }
        
        String artikl = listaNarudzbi.poll(); 
        String[] podaci = artikl.split("#");

        if(!podaci[0].equals("6")){
          stm.aktivniProcesi(filijala + ":" + artikl + "(pocetak procesa: " + sdf.format(date) + ")");
        }else{
          stm.aktivniProcesi("");
        }
        
        switch (podaci[0]) {
          case "1": // vrata
            novaVrata = stm.napraviVrata(podaci[1], this.sock, this.in, this.out);
            gotoviProizvodi.add(novaVrata);
            id++;
            stm.zavrsenProces(artikl + "(kraj procesa: " + sdf.format(date) + ")"  + ":" + filijala );
            stm.dodajGotovProizvod(novaVrata, id +  "sugurnosna vrata");
            break;
          case "2": // sigurnosna vrata
            novaSigurnosnaVrata = stm.napraviSigurnosnaVrata(podaci[1], sock, in, out);
            gotoviProizvodi.add(novaSigurnosnaVrata);
            stm.zavrsenProces(artikl + "(kraj procesa: " + sdf.format(date) + ")"  + ":" + filijala );
            stm.dodajGotovProizvod(novaSigurnosnaVrata, id +  " sugurnosna vrata");
            id++;
            break;
          case "3":
            noviProzor = stm.napraviProzor(podaci[1], sock, in, out);
            id++;
            stm.zavrsenProces(artikl + "(kraj procesa: " + sdf.format(date) + ")"  + ":" + filijala );
            stm.dodajGotovProizvod(noviProzor, id +  "prozor");
            gotoviProizvodi.add(noviProzor);
            break;
          case "4":
            System.out.println(podaci[1]);
            krovniProzor =  stm.napraviKrovniProzor(podaci[1], sock, in, out);
            gotoviProizvodi.add(krovniProzor);
            System.out.println(krovniProzor);
            stm.dodajGotovProizvod(krovniProzor, id +  "krovni_prozor");
            stm.zavrsenProces(artikl + "(kraj procesa: " + sdf.format(date) + ")"  + ":" + filijala );
            id++;
            break;
          case "5":
            SajbaZaIzlog sajba = stm.napraviSajbuZaIzlog(podaci[1], sock, in, out);
            gotoviProizvodi.add(sajba);
            stm.zavrsenProces(artikl + "(kraj procesa: " + sdf.format(date) + ")"  + ":" + filijala );
            stm.dodajGotovProizvod(sajba, id +  "sajba za izlog");
            id++;
            break;
          case "6":
            krajNarucivanja = true;
            id++;
            break;
          case "7":
            System.out.println("  k r a j ");
            
            break;
          default:
            break;
            
        }
        System.out.println("Svi proizvodi su napravljeni");
        System.out.println("Slanje gotovih proizvoda filijali. . . ");
        if(listaNarudzbi.isEmpty()){
          String[] brPr = new String[0];
          try{
            File f = new File ("server/gotovi_proizvodi");
            brPr = f.list();
            stm.posaljiZipFajl(sock);
          }catch(Exception e){
            e.printStackTrace();
          } 
        }
      }

      System.out.println("Proizvodi su poslani . . .");
      stm.unzip(sock, in);
      
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
}
