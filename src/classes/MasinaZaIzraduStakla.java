package src.classes;

import src.exceptions.GreskaPriIzradiStaklaException;
import src.interfaces.TestiranjeStakla;
import src.proizvodi.*;
import src.server.ServerThreadManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Random;

public class MasinaZaIzraduStakla extends Masina {
  private Staklo staklo;
  private boolean ispravno = false;
  private ArrayList<Poluproizvod> listaNapravljenih = new ArrayList<>();
  public MasinaZaIzraduStakla(){
    super();
  }
  
  public MasinaZaIzraduStakla(String svrha, String vrstaMaterijala, String oznaka, int id){
    super(svrha, vrstaMaterijala, oznaka, id);
  }
  
  public boolean getIspravno(){
    return this.ispravno;
  }
  @Override
  public String toString(){
    return super.toString();
  }
  
  public Staklo getStaklo(){
    return staklo;
  }
  public Staklo getStaklo(int id){
    for(Poluproizvod s : listaNapravljenih){
      if(s.getId() == id)
        return (Staklo)s;
    }
    return null;
  }
  @Override
  public void run(){
    try {
      int id = this.id;
      
      synchronized(this){
        
        
        sleep(this.getBrzinaIzradeProizvoda());
        this.staklo = new Staklo(this.getSvrha(), id);
        
        System.out.println("Staklo " + id + "je napravljeno");
        System.out.println("Testiranje stakla...");
        listaNapravljenih.add(this.staklo);
      }
    }catch(InterruptedException  e){
      System.out.println(e);
      
    }
  }
  
}
