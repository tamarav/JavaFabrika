package src.proizvodi;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class Proizvod implements Serializable{
 
 public String naziv;
 private String vrstaMaterijala;
 private int visina;
 private int sirina;
 public int serijskiBroj;
 private String datumIVrijemeKreiranja;
 private ArrayList<Poluproizvod> poluproizvodi = new ArrayList<>();
 
 public Proizvod() {
 }
 
 public Proizvod(String naziv, int serijskiBroj, String vrstaMaterijala, int visina, int sirina, ArrayList<Poluproizvod> poluproizvodi){
  this.naziv = naziv;
  this.serijskiBroj = serijskiBroj;
  this.vrstaMaterijala = vrstaMaterijala;
  this.visina = visina;
  this.sirina = sirina;
  this.poluproizvodi = poluproizvodi;
  Date datum = new Date();
  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY_HH:mm");
  this.datumIVrijemeKreiranja = sdf.format(datum);
 }
 
 public String toString(){
  return "Naziv: " + naziv + "(" + serijskiBroj + ",visina: " + visina + ",sirina: " + sirina + ", vrsta materijala:" + vrstaMaterijala+ " datum kreiranja: " + datumIVrijemeKreiranja+ ") \n";
  
 }
 public String getName(){
  return naziv;
 }
 
 public int getId(){
  return serijskiBroj;
 }
 
}
