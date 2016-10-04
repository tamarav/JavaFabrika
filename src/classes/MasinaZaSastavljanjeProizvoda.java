package src.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import src.proizvodi.*;

public class MasinaZaSastavljanjeProizvoda{
  private Vrata vrata;
  private SigurnosnaVrata sigurnosnaVrata;
  private Prozor prozor;
  private String pocetakProizvodnje;
  private String krajProizvodnje;
  public static ArrayList<Poluproizvod> listaPoluproizvoda = new ArrayList<>();
  private MasinaZaIzraduDrveneKonstrukcije mdk;
  private MasinaZaIzraduPVCKonstrukcije mpk;
  private MasinaZaIzraduStakla mis;
  private MasinaZaIzraduMehanizmaZaOtvaranje mmo;
  
  public MasinaZaSastavljanjeProizvoda(){}
  
  public Vrata sastaviVrata(int serijskiBroj, String naziv, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi){
    vrata = new Vrata(serijskiBroj, naziv, vrstaMaterijala, visina, sirina, poluproizvodi);
    Date datum = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm");
    this.krajProizvodnje = sdf.format(datum);
    return vrata;
  }
  
  public synchronized void upisiProizvod(String proizvod, String pocetakProzivodnje, String krajProizvodnje){
    try{
      File file = new File ("prozivodi.txt");
      BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
      bw.write(proizvod + "* pocetak proizvodnje " + pocetakProzivodnje + "- kraj prozivodnje " + krajProizvodnje);
      bw.newLine();
      bw.flush();
      bw.close();   
    }catch(IOException e){
      e.printStackTrace();
    }
  }
  
  
  public SigurnosnaVrata sastaviSigurnosnaVrata(int id, int visina, int sirina, Konstrukcija kon, MehanizamZaOtvaranje mehOtvaranje){
    ArrayList<Poluproizvod> pp = new ArrayList<>();
    pp.add(kon);
    pp.add(mehOtvaranje);
    SigurnosnaVrata sv  = new SigurnosnaVrata(id, "Sigurnosna vrata ", "PVC", visina, sirina, pp);
    return sv;
  }
  
  public SajbaZaIzlog sastaviSajbu(int id,String vrstaMaterijala, int duzina, int sirina,ArrayList<Poluproizvod> pp){
    SajbaZaIzlog sajba = new SajbaZaIzlog("sajba za izlog", id, vrstaMaterijala, duzina, sirina, pp);
    return sajba;
  }
  
  public KrovniProzor sastaviKrovniProzor(String naziv, int id,String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi){
    KrovniProzor prozor = new KrovniProzor(naziv, id, vrstaMaterijala, visina, sirina, poluproizvodi);
    return prozor;
    
  }
  
  public Prozor sastaviProzor(int id, String vrstaMaterijala,int duzina, int sirina, int roletne, int resetke, ArrayList<Poluproizvod> poluproizvodi){
    boolean rol = false;
    boolean res = false;
    if(roletne == 1){
      rol = true;
    }
    if(resetke == 1){
      res = true;
    }
    Prozor prozor = new Prozor("Prozor", id, vrstaMaterijala, duzina, sirina, poluproizvodi, rol, res);
    return prozor;
  }
  
  
}
