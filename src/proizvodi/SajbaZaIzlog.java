package src.proizvodi;

import java.util.ArrayList;

public class SajbaZaIzlog extends Prozor {

 public SajbaZaIzlog() {
 }

 public SajbaZaIzlog(String naziv, int serijskiBroj, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi) {
  super(naziv, serijskiBroj, vrstaMaterijala, visina, sirina, poluproizvodi);
 }
 
 @Override
 public String toString(){
  return super.toString();
 }

}
