package src.proizvodi;

import java.util.ArrayList;

import src.interfaces.MahanizamZaZakljucavanje;

public class SigurnosnaVrata extends Vrata implements MahanizamZaZakljucavanje{
  
  public SigurnosnaVrata(int serijskiBroj, String naziv, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi){
    super(serijskiBroj, naziv, vrstaMaterijala, visina, sirina, poluproizvodi);  
  }
  
  @Override
  public String toString(){
    return super.toString() + ", imaju dodatni mehanizam za zakljucavanje"; 
  }
}
