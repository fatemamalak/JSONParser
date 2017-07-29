package jsonparser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyParser{
	String jsonStr;
	int position;
	char lookAhead;
	public MyParser(String jStr)
	{
		 position = 0;
		  this.jsonStr=jStr.replaceAll("\\s+","");
		 lookAhead = this.jsonStr.charAt(position);
	}
	public boolean validate()throws JSONParserExceptions
	{
		if(lookAhead == '{')
		{
			typeObject();
		}
		else
		{
			typeArray();
		}
	  return true;	
	}
	void typeObject() throws JSONParserExceptions{
		if(lookAhead == '{')
		{
			match('{');
			if(lookAhead == '}')
				match('}');
			else if(lookAhead == '\"')
			{
				
				typePair();	
				while(lookAhead == ',')
				{
					match(',');
					typePair();
				}
				match('}');
				
			}
		}
		else if(lookAhead == ';')
			match(';');
		
		
	}
	void typePair() throws JSONParserExceptions{
		typeString();
		match(':');
		typeValue();
		
	}
	void typeArray() throws JSONParserExceptions{
		if(lookAhead == '[')
		{
			match('[');
			if(lookAhead == ']')
				match(']');
			else 
			{
				typeValue();	
				while(lookAhead == ',')
				{
					match(',');
					typeValue();
				}
				match(']');
				
			}
		}
		else if(lookAhead == ';')
			match(';');
		
	}
	void typeValue()throws JSONParserExceptions
	{
		switch(lookAhead)
		{
		  case '\"':
		   {
			 typeString();
			 break;
		     }
		  case '{':
		    {
			 typeObject();
			 break;
		     }
		  case '[':
		    {
			 typeArray();
			 break;
		      }
		   case 't':
		   {
			   position += 4;
			   lookAhead = jsonStr.charAt(position);
			   break;
		     }
		   case 'f':
		   {
			   position += 5;
			   lookAhead = jsonStr.charAt(position);
			   break;
		   }
		   case 'n':
		     {
		    	 position += 4;
		    	 lookAhead = jsonStr.charAt(position);
		    	 break;
		     }
		   default :
		   {
			   typeNumber();
		   }
		}
		
	}
	void typeNumber() throws JSONParserExceptions
	{
		int length = 0;
		String str = "";
		while(lookAhead=='-' || lookAhead =='+' || (lookAhead >= '0' && lookAhead <='9'))
		{
			str = str + jsonStr.charAt(position);
			position++;
			lookAhead = jsonStr.charAt(position);
			
		}
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		Matcher matcher = pattern.matcher(str);
		if(!matcher.matches()) {
			throw new JSONParserExceptions("Not a valid number");
		}
	}
	void typeString()throws JSONParserExceptions
	{
		match('\"');
		String tempStr = jsonStr.substring(position).split("\"")[0];
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*[.+@+'\t'+' ']*[a-zA-Z0-9]*$");
		Matcher matcher = pattern.matcher(tempStr);
		if(matcher.matches()) {
		    position +=tempStr.length();
		    lookAhead = jsonStr.charAt(position);
		}
		match('\"');
	}
	void match(char t) throws JSONParserExceptions
	{
		if(t == lookAhead)
		{
			lookAhead = getChar();
		}
		else
			throw new JSONParserExceptions("Expected "+t +" instead of "+lookAhead);
	}
	char getChar() throws JSONParserExceptions{
		position++;
		/*while(jsonStr.charAt(position) == '\n' || jsonStr.charAt(position)== '\t' )
		{
			position++;
		}*/
		if(position == jsonStr.length())
			return '\t';
		else
		return jsonStr.charAt(position);
	}
}
public class TestParser {
	public static void main(String args[])
	{
		String fileName = "F:/input.txt";
		BufferedReader br;
	    try {
	    	br  = new BufferedReader(new FileReader(fileName));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        MyParser mp = new MyParser(sb.toString());
	        if(mp.validate())
	        	System.out.println("The file is valid JSON File");
	        
	    } 
	   
	    catch(Exception e)
	    {
	    	System.out.println("The file is not a valid JSON file\n"+e);
	    }
	}
}

