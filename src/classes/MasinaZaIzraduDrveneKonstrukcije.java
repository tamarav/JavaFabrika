package src.classes;

import java.util.HashMap;

import src.proizvodi.Konstrukcija;
import src.server.ServerThreadManager;

public class MasinaZaIzraduDrveneKonstrukcije extends Masina {
  private Konstrukcija novaKonstrukcija;
  protected boolean zavrsenaKonstrukcija;
  private static  HashMap<Integer, Integer > listaZavrsenihKonstrukcija  = new HashMap<>();
  public MasinaZaIzraduDrveneKonstrukcije() {
    super();
  }
  
  public MasinaZaIzraduDrveneKonstrukcije(String svrha, String vrstaMaterijala, String oznaka, int id) {
    super(svrha, vrstaMaterijala, oznaka, id);
  }
  
  public Konstrukcija getKonstrukcija(){
    return novaKonstrukcija;
  }
  public boolean ZavrsenaKonstrukcija(int id){
    boolean rez = false;
    for(int i =0 ; i< listaZavrsenihKonstrukcija.size(); i++){
      if(listaZavrsenihKonstrukcija.get(i) == id)
        rez = true;
    }
    
    return rez;
  }
  @Override
  public void run(){
    synchronized(this){
      try {
        sleep(this.getBrzinaIzradeProizvoda());
        this.novaKonstrukcija = new Konstrukcija("Drvena konstrukcija", this.id);
        System.out.println(novaKonstrukcija);
        ServerThreadManager.poluproizvodi.add(novaKonstrukcija);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Drvena konstrukcija je napravljena");
    }
  }
  
}