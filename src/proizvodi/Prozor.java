package src.proizvodi;

import java.util.ArrayList;

public class Prozor extends Proizvod {
 private boolean roletne;
 private boolean resetke;
 public Prozor() {
  super();
 }

 public Prozor(String naziv, int serijskiBroj, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi, boolean roletne, boolean resetke) {
  super(naziv, serijskiBroj, vrstaMaterijala, visina, sirina, poluproizvodi);
  this.roletne = roletne;
  this.resetke = resetke;
 }
 public Prozor(String naziv, int serijskiBroj, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi) {
  super(naziv, serijskiBroj, vrstaMaterijala, visina, sirina, poluproizvodi);
  
 }
 public String toSTring(){
   String dodaci = "";
   if(roletne){
   dodaci += ", sa roletnama";
   }
   if(resetke){
   dodaci += ",sa resetkama.";
   }
  return super.toString() + dodaci;
 }

}
