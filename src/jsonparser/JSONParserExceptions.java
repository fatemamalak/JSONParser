package jsonparser;

public class JSONParserExceptions extends Exception {
	String error;
	public JSONParserExceptions(String error){
	      this.error = error;
	  }
	  public String toString(){
	  return error;
	  }
}
