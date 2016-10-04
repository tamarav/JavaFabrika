package src.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import src.classes.*;
import src.proizvodi.*;

public class ServerThreadManager {
  private Socket sock;
  private int value;
  private PrintWriter out;
  private BufferedReader in;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private static int id = 492;
  private MasinaZaIzraduDrveneKonstrukcije mdk;
  private MasinaZaIzraduPVCKonstrukcije mpk;
  private MasinaZaIzraduStakla mis;
  private MasinaZaIzraduMehanizmaZaOtvaranje mmo;
  private MasinaZaSastavljanjeProizvoda masinaSastavi = new MasinaZaSastavljanjeProizvoda();
  private boolean postojiStaklo = false;
  public static ArrayList<Poluproizvod> poluproizvodi = new ArrayList<>();
  private Vrata vrata;
  private SigurnosnaVrata sigVrata;
  
  public ServerThreadManager() {
  }
  
  public synchronized Vrata napraviVrata(String podaciONarudzbi, Socket sock, BufferedReader in, PrintWriter out) {
    
    this.sock = sock;
    this.in = in;
    this.out = out;
    String pocetakProizvodnje;
    Date datum = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm");
    pocetakProizvodnje = sdf.format(datum);
    System.out.println("\u2022Podaci o narudzbi:" + podaciONarudzbi);
    String podaci[] = podaciONarudzbi.split("-");
    String oznaka = "";
    boolean prvaFaza = false;
    if (podaci[0].equals("drvo")) {
      oznaka = "DV";
      mdk = new MasinaZaIzraduDrveneKonstrukcije("vrata", "drvo", oznaka, id);
      mdk.start();  
    } else {
      oznaka = "PV";
      mpk = new MasinaZaIzraduPVCKonstrukcije("vrata", "PVC", oznaka, id);
      mpk.start();
    }
    
    if (podaci[2].equals("staklo")) {
      postojiStaklo = true;
      boolean testiranjeStakla = false;
      while (!testiranjeStakla) {
        mis = new MasinaZaIzraduStakla("staklo za vrata", "staklo", oznaka, id);
        mis.start();
        testiranjeStakla = true;
      }
    }
    
    mmo = new MasinaZaIzraduMehanizmaZaOtvaranje("mehanizam za otvaranje vrata", podaci[0], oznaka, id);
    mmo.start();
    
    id++;
    Konstrukcija konstrukcija;
    ArrayList<Poluproizvod> pp = new ArrayList<>();
    try {
      if (mdk != null) {
        mdk.join();
        konstrukcija = mdk.getKonstrukcija();
        pp.add(konstrukcija);
      } else if (mpk != null) {
        mpk.join();
        konstrukcija = mpk.getKonstrukcija();
        pp.add(konstrukcija);
      }
      if (mis != null) {
        mis.join();
        Staklo dioStaklo = mis.getStaklo();
        pp.add(dioStaklo);
      }
      mmo.join();
      pp.add(mmo.getMehanizamZaOtvranje());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // sastavljanje prozivoda (vrata)
    System.out.println("sastavljanje proizvoda");
    Vrata nv = masinaSastavi.sastaviVrata(id, "Vrata", podaci[0], Integer.parseInt(podaci[3]),Integer.parseInt(podaci[4]), pp);
    id++;
    return nv;
  }
  
  public synchronized SigurnosnaVrata napraviSigurnosnaVrata(String podaciONarudzbi, Socket sock, BufferedReader in,PrintWriter out) {
    
    this.sock = sock;
    this.in = in;
    this.out = out;
    String vrstaMaterijala = "";
    String podaci[] = podaciONarudzbi.split("-");
    try {
      System.out.println("\u2022Podaci o narudzbi:" + podaciONarudzbi);
      String oznaka = "";
      vrstaMaterijala = podaci[0];
      oznaka = "PVC";
      mpk = new MasinaZaIzraduPVCKonstrukcije("vrata", "PVC", oznaka, id);
      mpk.start();
      mmo = new MasinaZaIzraduMehanizmaZaOtvaranje("mehanizam za otvaranje vrata", podaci[0], oznaka, id);
      mmo.start();
      if (mpk != null) {
        mpk.join();
      }
      if (mmo != null) {
        mmo.join();
      }
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    // sastavljanje prozivoda (vrata)
    Konstrukcija kon = mpk.getKonstrukcija();
    MehanizamZaOtvaranje mehOtvaranje = mmo.getMehanizamZaOtvranje();
    SigurnosnaVrata sv = masinaSastavi.sastaviSigurnosnaVrata(id, Integer.parseInt(podaci[1]), Integer.parseInt(podaci[2]), kon, mehOtvaranje);
    id++;
    return sv; 
  }
  
  public KrovniProzor napraviKrovniProzor(String podaciONarudzbi, Socket sock, BufferedReader in, PrintWriter out) {
    this.sock = sock;
    this.in = in;
    this.out = out;
    String pocetakProizvodnje;
    Date datum = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm");
    pocetakProizvodnje = sdf.format(datum);
    
    KrovniProzor kprozor = null;
    ArrayList<Poluproizvod> dijeloviZaProzor = new ArrayList<>();
    
    String materijal = "";
    System.out.println("\u2022Podaci o narudzbi:" + podaciONarudzbi);
    String podaci[] = podaciONarudzbi.split("-");
    String oznaka = "";
    if (podaci[0].equals("drvo")) {
      oznaka = "DV";
      mdk = new MasinaZaIzraduDrveneKonstrukcije("vrata", "drvo", oznaka, id);
      mdk.start();
      materijal = "drvo";
    } else {
      oznaka = "PV";
      mpk = new MasinaZaIzraduPVCKonstrukcije("vrata", "PVC", oznaka, id);
      mpk.start();
      materijal = "PVC";
    }
    mis = new MasinaZaIzraduStakla("staklo za krovni prozor", podaci[0], "SKP", id);
    id++;
    ArrayList<Poluproizvod> pp = new ArrayList<>();
    try {
      if (mdk != null) {
        mdk.join();
      } else if (mpk != null) {
        mpk.join();
      }
      if (mis != null) {
        mis.join();
      }
      
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Konstrukcija kon = null;
    if (materijal.equalsIgnoreCase("drvo")) {
      kon = mdk.getKonstrukcija();
    } else {
      kon = mpk.getKonstrukcija();
    }
    pp.add(kon);
    Staklo staklo = mis.getStaklo();
    pp.add(staklo);
    KrovniProzor kProzor = masinaSastavi.sastaviKrovniProzor("krozvni prozor", id, materijal, Integer.parseInt(podaci[1]), Integer.parseInt(podaci[2]), pp);
    id++;
    return kProzor; 
  }
  
  public synchronized Prozor napraviProzor(String podaciONarudzbi, Socket sock, BufferedReader in, PrintWriter out) {
    
    this.sock = sock;
    this.in = in;
    this.out = out;
    String podaci[] = podaciONarudzbi.split("-");
    int brKrila = Integer.parseInt(podaci[1]);
    int brKon = brKrila;
    while (brKon != 0) {
      if (podaci[0].equals("drvo")) {
        mdk = new MasinaZaIzraduDrveneKonstrukcije("sajba za izlog", "drvo", "DV", id);
        mdk.start();
        brKon--;
        id++;
      } else if (podaci[0].equals("PVC")) {
        mpk = new MasinaZaIzraduPVCKonstrukcije("sajba za izlog", "PVC", "PV", id);
        mpk.start();
        brKon--;
        id++;
      }
    }
    while (brKrila != 0) {
      mis = new MasinaZaIzraduStakla("staklo za prozor", "staklo", "prozor", id);
      mis.start();
      id++;
      System.out.println(" ber  jeee " + brKrila);
      brKrila--;
    }
    mmo = new MasinaZaIzraduMehanizmaZaOtvaranje("mehanizam za otvaranje prozora", podaci[0], "MOP", id);
    mmo.start();
    try {
      if (mdk != null) {
        mdk.join();
      } else if (mpk != null) {
        mpk.join();
      }
      if (mis != null) {
        mis.join();
      }
      mmo.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    id++;
    Konstrukcija kon = null;
    Staklo staklo = null;
    ArrayList<Poluproizvod> pp = new ArrayList<>();
    int br = Integer.parseInt(podaci[1]);
    for (int i = 0; i < br; i++) {
      if (podaci[0].equals("drvo")) {
        kon = mdk.getKonstrukcija();
      } else {
        kon = mpk.getKonstrukcija();
      }
      pp.add(kon);
      staklo = mis.getStaklo();
      pp.add(staklo);
    }
    Prozor noviProzor = masinaSastavi.sastaviProzor(id, podaci[0], Integer.parseInt(podaci[4]), Integer.parseInt(podaci[5]), Integer.parseInt(podaci[2]), Integer.parseInt(podaci[6]), pp);
    id++;
    return noviProzor;
  }
  
  public synchronized SajbaZaIzlog napraviSajbuZaIzlog(String podaciONarudzbi, Socket sock, BufferedReader in, PrintWriter out) {
    this.sock = sock;
    this.in = in;
    this.out = out;
    String podaci[] = podaciONarudzbi.split("-");
    if (podaci[0].equals("drvo")) {
      mdk = new MasinaZaIzraduDrveneKonstrukcije("sajba za izlog", "drvo", "DV", id);
      mdk.start();
    } else {
      mpk = new MasinaZaIzraduPVCKonstrukcije("sajba za izlog", "PVC", "PV", id);
      mpk.start();
    }
    int br = Integer.parseInt(podaci[1]);
    System.out.println("broj krila je " + br);
    boolean testiranjeStakla = false;
    while (br != 0) {
      mis = new MasinaZaIzraduStakla("sajba za izlog", "staklo", "sajba", id);
      mis.start();
      id++;
      System.out.println(" ber  jeee " + br);
      br--;
    }
    try {
      if (mpk != null) {
        mpk.join();
      }
      if (mdk != null) {
        mdk.join();
      }
      if (mis != null) {
        mis.join();
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //
    Konstrukcija kon = null;
    String materijal = "";
    if (podaci[0].equalsIgnoreCase("drvo")) {
      kon = mdk.getKonstrukcija();
      materijal = "drvo";
    } else {
      kon = mpk.getKonstrukcija();
      materijal = "PVC";
    }
    System.out.println("valjda je vracena " + kon);
    System.out.println("sastavljanje proizvoda");
    ArrayList<Poluproizvod> pp = new ArrayList<>();
    pp.add(kon);
    int brKr = Integer.parseInt(podaci[1]);
    while (brKr != 0) {
      Staklo s = mis.getStaklo();
      pp.add(s);
      brKr--;
    }
    SajbaZaIzlog sajba = masinaSastavi.sastaviSajbu(id, materijal, Integer.parseInt(podaci[2]), Integer.parseInt(podaci[3]), pp);
    id++;
    return sajba;
  }
  
  public synchronized void staviNaListuCekanja(String podaci) {
    try {
      File file = new File("server/na_cekanju.txt");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
      bw.write("\u2022" + podaci + "\n");
      bw.newLine();
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized void aktivniProcesi(String podaci) {
    ukloniSaListeCekanja(podaci);
    try {
      File file = new File("server/aktivni.txt");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      bw.write(podaci);
      bw.newLine();
      bw.flush();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  public synchronized void ukloniSaListeCekanja(String podaci) {
    try {
      File file = new File("server/na_cekanju.txt");
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = "";
      String tmp = "";
      line = br.readLine();
      while ((line = br.readLine()) != null) {
        tmp += line;
        tmp += "\n";
      }
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      bw.write(tmp);
      bw.newLine();
      bw.flush();
      bw.close();
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  
  public synchronized void zavrsenProces(String artikl) {
    try {
      File file = new File("server/zavrseni.txt");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
      bw.write(artikl);
      bw.flush();
      bw.newLine();
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized void dodajGotovProizvod(Proizvod p, String naziv) {
    try {
      String name = "server/gotovi_proizvodi/" + naziv + ".ser";
      File file = new File(name);
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      System.out.println("salje se " + p);
      oos.writeObject(p);
      oos.flush();
      oos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public synchronized void preuzmiProizvod(Object o, String naziv){
    try {
      String name = "client/proizvodi/" + naziv;
      File file = new File(name);
      FileOutputStream fos = new FileOutputStream(file);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(o);
      oos.flush();
      oos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
  }
  public synchronized void posaljiZipFajl(Socket sock) {
    this.sock = sock;
    try {
      FileOutputStream fileOutputStream = new FileOutputStream("server/gotovi_proizvodi.zip");
      ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
      ZipEntry zipEntry = new ZipEntry("zavrseni.txt");
      zipOutputStream.putNextEntry(zipEntry);
      FileInputStream fileInputStream = new FileInputStream(new File("server/zavrseni.txt"));
      byte[] buf = new byte[1024];
      int bytesRead;
      
      while ((bytesRead = fileInputStream.read(buf)) > 0) {
        zipOutputStream.write(buf, 0, bytesRead);
      }
      
      zipOutputStream.closeEntry();
      zipOutputStream.close();
      fileOutputStream.close();
      fileInputStream.close();
      // zipovan je, sada slanje
      File file = new File("server/gotovi_proizvodi.zip");
      long duzina = file.length();
      DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
      dos.writeLong(duzina);
      dos.flush();
      byte[] buffer = new byte[1024];
      FileInputStream fajl = new FileInputStream(file);
      int length = 0;
      while ((length = fajl.read(buffer)) > 0) {
        dos.write(buffer, 0, length);
        dos.flush();
      }
      fajl.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    posaljiProizvode(sock);
  }
  
  public synchronized void posaljiProizvode(Socket sock) {
    try { 
      ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream()); 
      File folder = new  File("server/gotovi_proizvodi/"); 
      File files[] = folder.listFiles();
      String[] names = folder.list();
      int i=0;
      Object p;
      System.out.println(files.length);
      FileInputStream fis;
      ObjectInputStream ois;
      for(int j = 0;j< files.length; j++){
        out.println(names[j]);
        fis = new FileInputStream(files[j]);
        ois = new ObjectInputStream(fis);
        p = (Object) ois.readObject();
        System.out.println(p);
        preuzmiProizvod(p, names[j]);
        oos.writeObject(p); 
          ois.close();
      } 
    
    }catch(IOException | ClassNotFoundException e){ 
      e.printStackTrace();
    }
  }
  
  public synchronized void unzip(Socket sock, BufferedReader in) {
    
    byte[] buffer = new byte[1024];
    try {
      ZipInputStream zis = new ZipInputStream(new FileInputStream("client/gotovi_proizvodi.zip"));
      ZipEntry ze = zis.getNextEntry();
      while (ze != null) {
        File fajl = new File("client/gotovi_proizvodi_koji_se_preuzimaju.txt");
        FileOutputStream fos = new FileOutputStream(fajl);
        int len;
        while ((len = zis.read(buffer)) > 0) {
          fos.write(buffer, 0, len);
        }
        fos.close();
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      zis.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
   public void preuzmiZip(Socket sock, BufferedReader in) throws Exception {
    
    DataInputStream is = new DataInputStream(sock.getInputStream());
    long duzina = is.readLong();
    int kontrolnaDuzina = 0, flag = 0;
    byte[] buffer = new byte[1024];
    FileOutputStream fajl = new FileOutputStream(new File("client/gotovi_proizvodi.zip"));
    while ((kontrolnaDuzina = is.read(buffer)) > 0) {
      fajl.write(buffer, 0, kontrolnaDuzina);
      flag += kontrolnaDuzina;
      if (duzina == flag)
        break; 
    }
    fajl.close();
    System.out.println("Preuzimanje zip fajla je  zavrseno...");
  }
}
