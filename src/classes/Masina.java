package src.classes;

public abstract class Masina extends Thread {
  private String oznaka;
  private int brzinaIzradeProizvoda;
  private String vrstaMaterijala;
  private String svrha;
  protected int id;
  
  
  public Masina(){}
  
  public Masina(String svrha, String vrstaMaterijala, String oznaka, int id){
    this.svrha = svrha;
    this.vrstaMaterijala = vrstaMaterijala;
    this.oznaka = oznaka;
    this.id = id;
    if(vrstaMaterijala.equalsIgnoreCase("drvo")){
      this.brzinaIzradeProizvoda = 3000;
    }else if(vrstaMaterijala.equalsIgnoreCase("PVC")){
      this.brzinaIzradeProizvoda = 4000;
    }else if (vrstaMaterijala.equalsIgnoreCase("staklo")){
      this.brzinaIzradeProizvoda = 5000;
    }
  }
  
  
  @Override
  public String toString(){
    return "Masina za izradu" + this.svrha + "Oznaka: " + this.oznaka + ", vrsta materijala: " + this.vrstaMaterijala;
  }
  
  public void run(){
  }
  
  public String getOznaka() {
    return oznaka;
  }
  
  public void setOznaka(String oznaka) {
    this.oznaka = oznaka;
  }
  
  public int getBrzinaIzradeProizvoda() {
    return brzinaIzradeProizvoda;
  }
  
  public void setBrzinaIzradeProizvoda(int brzinaIzradeProizvoda) {
    this.brzinaIzradeProizvoda = brzinaIzradeProizvoda;
  }
  
  public String getVrstaMaterijala() {
    return vrstaMaterijala;
  }
  
  public void setVrstaMaterijala(String vrstaMaterijala) {
    this.vrstaMaterijala = vrstaMaterijala;
  }
  
  public String getSvrha() {
    return svrha;
  }
  
  public void setSvrha(String svrha) {
    this.svrha = svrha;
  }
  
}
