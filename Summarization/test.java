import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) throws IOException {
	// Open File
	File fileDir = new File("biyosfer2.txt");

	BufferedReader in = new BufferedReader(new InputStreamReader(
								     new FileInputStream(fileDir), "UTF8"));

	// Keep All Files in docs list
	List<String> docs = new ArrayList<String>();
	String doc = "";

	// Read file line by line
	String line;
	line = in.readLine();// Skip Question line
	line = in.readLine();// Skip Answer line
	while ((line = in.readLine()) != null) {
	    if (line.startsWith("Doc")) {
		if (doc.equals("") == false)
		    docs.add(doc);
		doc = "<DOC>";
	    } else
		doc += "\n" + line;
	}

	// Close the file
	in.close();

	// Parse the each document
	for (int i = 0; i < docs.size(); i++) {
	    VikipediParser parser = new VikipediParser();
	    String[] parsedItems = parser.parse(docs.get(i).toString());

	    System.out.println("Id: " + parsedItems[0]);
	    System.out.println("Title: " + parsedItems[1]);
	    System.out.println("Content: " + parsedItems[2]);
			
			
	}
    }

}
