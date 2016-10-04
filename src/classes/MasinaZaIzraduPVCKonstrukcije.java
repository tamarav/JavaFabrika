package src.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import src.proizvodi.*;
import src.server.ServerThreadManager;

public class MasinaZaIzraduPVCKonstrukcije extends Masina{
  private static Konstrukcija novaKonstrukcija;
  private HashMap<Integer, Boolean> listaZavrsenihKonstrukcija  = new HashMap<>();
  
  public MasinaZaIzraduPVCKonstrukcije() {
    super();
  }
  
  public MasinaZaIzraduPVCKonstrukcije(String svrha, String vrstaMaterijala, String oznaka, int id){
    super(svrha, vrstaMaterijala, oznaka, id);
  }
  public Konstrukcija getKonstrukcija(){
    return novaKonstrukcija;
  }
  
  @Override
  public void run(){
    synchronized(this){
      try {
        sleep(this.getBrzinaIzradeProizvoda());
        this.novaKonstrukcija = new Konstrukcija("PVC konstrukcija", this.id);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("PVC konstrukcija je napravljena");
    }
  }
  
  
}
