package src.proizvodi;

import java.util.ArrayList;

import src.interfaces.DebljaStakla;
import src.interfaces.MehanizamZaOtvaranjeNaKrovnojKosini;

public class KrovniProzor extends Prozor implements DebljaStakla, MehanizamZaOtvaranjeNaKrovnojKosini{
  
  public KrovniProzor() {
  }
  
  public KrovniProzor(String naziv, int serijskiBroj, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi) {
    super(naziv, serijskiBroj, vrstaMaterijala, visina, sirina, poluproizvodi);
    
  }
  @Override
  public String debljaStakla(){
    return "posjeduju deblja stakla";
  }
  
  @Override
  public String mehanizamZaOtvaranjeNaKrovnojKosini(){
    return ", posjeduje mehanizam za otvaranje na krovnoj kosini.";
  }
  
  @Override
  public String toString(){
    return super.toSTring() + mehanizamZaOtvaranjeNaKrovnojKosini() + " i " + debljaStakla();
  }
  
}
