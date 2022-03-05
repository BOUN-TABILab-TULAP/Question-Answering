
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.tartarus.snowball.ext.turkishStemmer;

class Counter{
    int nums[]=new int[200];
    private String filename;
    private static ArrayList<String> keywordsList =new ArrayList<String>();



    public static void main(String[] args){


	readWords();
	System.out.println(keywordsList);
	countWords();
	//showResult();
	//}
    }

    public static void readWords(){
	Pattern pattern=Pattern.compile("\\W+"); 
	try {

	    FileReader fr=new FileReader("iklim.txt");
	    BufferedReader br = new BufferedReader(fr);
	    String strLine;
	    while((strLine=br.readLine())!=null){
		//split a line by spaces so we get words
		String[] words=strLine.split("[ ]+");
		for(String word:words){
		    //remove all symbols except underscore
		    Matcher mat=pattern.matcher(word);
		    word=mat.replaceAll("");
		    //add words to the list
		    keywordsList.add(word.toLowerCase());
		}
	    }

	    br.close();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }


    public static void countWords(){
	int count=1;
	String word="";

	String word2="abc";
	for(int i=0;i<keywordsList.size();i++){
	    turkishStemmer stemmer = new turkishStemmer();
	    stemmer.setCurrent(keywordsList.get(i));
	    word2=keywordsList.get(i);
	
	    if (stemmer.stem()){
		word=stemmer.getCurrent();
		System.out.println(word);
	    }

	
	    for(int j=i+1;j<keywordsList.size();j++){
	
	
		if(word2.contains(word)){
		
		    count++; //increase the number of duplicate words 
		    //}

		}
		//add the word and its frequency to the TreeMap

		//reset the count variable
		count=1;


	    }
	}
    }
}





/*public void showResult(){
  ArrayList<String> words = new ArrayList<String>();







  words.add(word);

  if(words.get(i).contains(word))
  nums[words.indexOf(words.get(i))]=nums[words.indexOf(words.get(i))]+1;



  nums[i]=count;
  System.out.println(nums[i]);
  System.out.println(words.get(i));
  if(words.get(i).equals("ve")||words.get(i).equals("ya")|| words.get(i).equals("bir") )
  largest = 0;
  else{
  if(nums[i] > largest){
            	
  largest = nums[i];
  index= i;
  }
  }
  //	System.out.format("%-20s%-5d%-2s\n", word,count,100*count/numWord+"%");
  }
  //System.out.format("%-20s%-5d%-2s\n", words.get(i),nums[i],100*20/numWord+"%");
  System.out.println("largest is:  " + largest);
  System.out.println("index is:  " + index);
  System.out.println(nums[105]);
  System.out.println(words.get(105));
  }

  }
*/





