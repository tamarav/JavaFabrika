package src.proizvodi;


import java.util.Random;
import src.exceptions.GreskaPriIzradiStaklaException;
import src.interfaces.TestiranjeStakla;

public class Staklo extends Poluproizvod implements TestiranjeStakla{
 public boolean ispravno;

 public Staklo() {
  super();
 }

 public Staklo(String naziv, int serijskiBroj) {
  super(naziv, serijskiBroj);
 }
 
 @Override
 public String toString(){
  return super.toString();
 }
 
 public int getId(){
  return this.getId();
 }
 
 @Override
 public String testirajStaklo() throws GreskaPriIzradiStaklaException{
  Random rand = new Random();
  int br = rand.nextInt(100);  //96% je uspjesnost pravljenja stakla 
   if(br<95){
    System.out.println("Napravljeno staklo je ispravno");
    return "ispravno";
   }
   return "neispravno";
 }
}
