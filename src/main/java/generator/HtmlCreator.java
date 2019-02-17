package generator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HtmlCreator {
	public void writeToHtml(Dungeon root){
		File htmlFile = new File ("generator.Dungeon.html");
		
		try{
			PrintWriter write = new PrintWriter(htmlFile);
			Scanner read = new Scanner(new FileReader("RoomDescription.txt"));
			
			write.println("<html>");
			write.println("<head>");
			write.println("<title>generator.Dungeon Generator</title>");
			write.println("</head>");
			
			write.println("<body>");
			write.println("<h1><center>generator.Dungeon Generator</Center></h1>");
			
			write.println("<p><center><img src = \"dungeon.png\" border = \"50\" align = \"center\" /> </center> </p>");
			
			write.println("<p>");
			while(read.hasNextLine()){
				write.println(read.nextLine() + "<br />");
			}
			write.println("</p>");
			write.println("</body>");
			write.println("</html>");
			
			
			write.close();
			read.close();
		} catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
}
