package src.proizvodi;

import java.util.Random;
import src.interfaces.TestiranjeMehanizmaZaOtvranje;

public class MehanizamZaOtvaranje extends Poluproizvod implements TestiranjeMehanizmaZaOtvranje{
  
  public MehanizamZaOtvaranje() {
  }
  
  public MehanizamZaOtvaranje(String naziv, int serijskiBroj) {
    super(naziv, serijskiBroj);
  }
  
  @Override
  public String toString(){
    return super.toString();
  }
  
  @Override
  public boolean testiranjeMehanizmaZaOtvaranje() {
    Random rand = new Random();
    boolean rez = false;
    int br = rand.nextInt(100);
    if (br < 95 ){
      rez = true;
    }
    return rez;
  }
}
