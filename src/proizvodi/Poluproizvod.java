package src.proizvodi;

import java.io.Serializable;

public class Poluproizvod implements Serializable{
  private String naziv;
  private int serijskiBroj;
  
  public Poluproizvod() {
  }
  
  public Poluproizvod(String naziv, int serijskiBroj){
    this.naziv = naziv;
    this.serijskiBroj = serijskiBroj;
  }
  
  public int getId(){
    return this.serijskiBroj;
  }
  
  @Override
  public String toString(){
    return "\u2022Poluproizvod( serijski broj: " + this.serijskiBroj +", naziv: " + naziv + ")";
  }
  
}
