package src.proizvodi;

import java.util.ArrayList;

public class Vrata extends Proizvod{
  
  
  public Vrata(int serijskiBroj, String naziv, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi){
    super(naziv, serijskiBroj, vrstaMaterijala, visina, sirina, poluproizvodi); 
  }
  
  @Override
  public String toString(){
    return super.toString();
  }
  
}
