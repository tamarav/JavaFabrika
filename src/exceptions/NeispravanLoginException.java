package src.exceptions;

public class NeispravanLoginException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public String toString(){
    return "Ne nalazite se u nasoj bazi.\n Zelite li da pokusate ponovo? (DA/NE)";
  }
  
}
