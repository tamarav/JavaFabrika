package src.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import src.proizvodi.*;
import src.server.ServerThreadManager;
import src.interfaces.*;


public class MasinaZaIzraduMehanizmaZaOtvaranje extends Masina {
  private static MehanizamZaOtvaranje noviMehanizamZaOtvaranje;
  public MasinaZaIzraduMehanizmaZaOtvaranje() {
    super();
  }
  
  public MasinaZaIzraduMehanizmaZaOtvaranje(String svrha, String vrstaMaterijala, String oznaka, int id) {
    super(svrha, vrstaMaterijala, oznaka, id);
  }
  
  @Override
  public String toString(){
    return super.toString();
  }
  
  public MehanizamZaOtvaranje getMehanizamZaOtvranje(){
    
    return noviMehanizamZaOtvaranje;
  }
  
  @Override
  public void run(){
    synchronized(this){
      try {
        boolean ispravanKomad = false;
        while(! ispravanKomad){
          sleep(this.getBrzinaIzradeProizvoda());
          System.out.println("Naravljen je mehanizam za otvaranje");
          this.noviMehanizamZaOtvaranje = new MehanizamZaOtvaranje(this.getSvrha(),this.id);
          ispravanKomad = noviMehanizamZaOtvaranje.testiranjeMehanizmaZaOtvaranje();
          System.out.println("Testiranje mehanizma za otvaranje (id = " + id + ")");
          if(!ispravanKomad){
            System.out.println("mehanizam za otvaranje id: " + this.id + "nije ispravan");
            id++;
          }
        }
        System.out.println("Mehanizam za otvaranje (id = " + this.id + ") je isprvan");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    
  }
  
}
