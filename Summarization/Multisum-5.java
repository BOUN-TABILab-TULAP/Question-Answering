import java.util.*;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.math.*;


import org.tartarus.snowball.ext.turkishStemmer;

import java.lang.Math;

public class Multisum {
    public static	BufferedReader in;
    public static ArrayList<String> sent = new ArrayList<String>();
    public static String soru;
    public  static  ArrayList<String> qrootList = new ArrayList<String>();
    public  static  ArrayList<String> rootListall = new ArrayList<String>();
    public  static  ArrayList<String> allroot = new ArrayList<String>();
    
    public static void main(String[] args)  throws FileNotFoundException, IOException,NullPointerException,ArrayIndexOutOfBoundsException { 

        if(args.length < 3) {
	    throw new IOException("USAGE : <relatedFileName> <topDocs> <question> \nGiven: " + args.toString());
	}
	System.out.println("Given Related File Name : " + args[0]);
	System.out.println("# of Docs to be Summarized : " + args[1]);
	System.out.println("Given Quesion : " + args[2]);
	
	int size = Integer.parseInt(args[1]);
	
	String filename= args[0]; // "paratest.txt";
	Wrapper contentsTitles = readFile(filename); // return contents & titles


	String[] bodies= new String[size];
	String[] titless= new String[size];
	ArrayList<String[]> keywords = new ArrayList<String[]>();
	
	for(int k=0;k<size;k++) {
	    bodies[k]= getBody(k, contentsTitles.contents); // input -> contents
	    titless[k]= getTitle(k, contentsTitles.titles); // input -> titles
	    bodies[k].replaceAll("","");
	    bodies[k].replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	    keywords.add(bodies[k].toLowerCase().split("[ ]+"));
	}
	
	ArrayList<ArrayList<String>> keywordLists = createWordList(keywords, size);
	ArrayList<ArrayList<String>> rootLists = createRootList(keywordLists);

	//createSentences(bodies[0],bodies[1],bodies[2]);
	for (String x:bodies) { if(x=="") {System.out.println("HHHHHHH");}}
	Wrapper2 sentenceListNoList = createSentences(bodies);
	
	String question = args[2];
	ArrayList<ArrayList<Double>> initialScores = getInputfromUser(question, sentenceListNoList.sentenceLists);
   
	ArrayList<TreeMap<String, Integer>> freqMaps = word_frequency(rootLists, keywordLists);
	ArrayList<ArrayList<String>> kelimeFrekansLists = createFreq(freqMaps);
        if(kelimeFrekansLists.isEmpty()==false){
	ArrayList<ArrayList<Double>> finalScores = score_sentences(initialScores, kelimeFrekansLists, sentenceListNoList.sentenceLists, sentenceListNoList.noList);

	System.out.println("******");
	System.out.println("******");

	sortAllSentences(finalScores, sentenceListNoList.noList, sentenceListNoList.sentenceLists);
}
    }

    public static Wrapper readFile(String filename) throws IOException {
	List<String> contents = new ArrayList<String>();
	List<String> titles = new ArrayList<String>();
	File fileDir = new File(filename);
			
	try {
	    in = new BufferedReader(new InputStreamReader(
							  new FileInputStream(fileDir), "UTF8"));
	} catch (UnsupportedEncodingException | FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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
	for (int i = 0; i < docs.size(); i++) {
	    VikipediParser parser = new VikipediParser();
	    String[] parsedItems = parser.parse(docs.get(i).toString());				
	    contents.add(parsedItems[2]) ; 
	    titles.add(parsedItems[1]);	 
	}

	return new Wrapper(contents, titles);
    }

    public static String getBody(int index, List<String> contents){
	return contents.get(index);
    }
    public static String getTitle(int index, List<String> titles){
	return titles.get(index);
    }

    private static String processWord(String x) {
	String tmp;

	tmp = x.toLowerCase();
	tmp = tmp.replace(",", "");
	tmp = tmp.replace(".", "");
	tmp = tmp.replace(";", "");
	tmp = tmp.replace("!", "");
	tmp = tmp.replace("?", "");
	tmp = tmp.replace("(", "");
	tmp = tmp.replace(")", "");
	tmp = tmp.replace("{", "");
	tmp = tmp.replace("}", "");
	tmp = tmp.replace("[", "");
	tmp = tmp.replace("]", "");
	tmp = tmp.replace("<", "");
	tmp = tmp.replace(">", "");
	tmp = tmp.replace("%", "");
	tmp = tmp.replace("'", "");
	tmp = tmp.replace("-", " ");
	tmp = tmp.replace("","");
	return tmp;
    }

    private static ArrayList<ArrayList<String>> createWordList(ArrayList<String[]> keywords, int size) {
	ArrayList<ArrayList<String>> keywordLists = new ArrayList<ArrayList<String>>();

	for(int i=0; i<size;i++) {
	    ArrayList<String> tmp = new ArrayList<String>();
	    
	    for(String word: keywords.get(i)) {
		word = removeStopWords(processWord(word));
		tmp.add(word);
	    }
	    
	    keywordLists.add(tmp);
	}

	return keywordLists;
    }

    private static ArrayList<ArrayList<String>> createRootList(ArrayList<ArrayList<String>> keywordLists) {

	ArrayList<ArrayList<String>> rootLists = new ArrayList<ArrayList<String>>();

	for(int i = 0; i<keywordLists.size();i++) {
	    turkishStemmer stemmer = new turkishStemmer();
	    ArrayList<String> keywordList = keywordLists.get(i);
	    ArrayList<String> rootList = new ArrayList<String>();

	    for (int j = 0; j<keywordList.size(); j++) {
		stemmer.setCurrent(keywordList.get(j));
		if (stemmer.stem()) {
		    rootList.add(processWord(stemmer.getCurrent()));
		}
	    }

	    rootLists.add(rootList);
	}
	
	return rootLists;
    }
	
    private static String removeStopWords(String x) {
	String tmp;
	tmp = x.toLowerCase();
	tmp = tmp.replace("ve", "");
	tmp = tmp.replace("veya", "");
	tmp = tmp.replace("bu", "");
	tmp = tmp.replace("bir", "");
	tmp = tmp.replace("ile", "");
	tmp = tmp.replace("fakat", "");
	tmp = tmp.replace("hiç", "");
	tmp = tmp.replace("o", "");
	tmp = tmp.replace("birbir", "");
	tmp = tmp.replace("ancak", "");
	return tmp;
    }

    private static ArrayList<ArrayList<Double>> getInputfromUser(String question, ArrayList<String[]> sentenceList) {

	ArrayList<ArrayList<Double>> newScores = new ArrayList<ArrayList<Double>>();
	
	soru = question;
	soru.toLowerCase();
	soru = removeQWords(soru);
	
	String[] wordsq = soru.trim().split(" ");

	for (int u = 0; u<sentenceList.size(); u++) {
	    String [] sentences = sentenceList.get(u);

	    ArrayList<Double> scores = new ArrayList<Double>();
	    
	    for (int i = 0; i<sentences.length; i++) {
		// originally, createSentences sets all the scoresN arrays to 0
		double score = 0; 
		for (int k = 0; k<wordsq.length; k++) {
		    if(sentences[i].contains(wordsq[k]))
			score += 1;
		}
		scores.add(score);
	    }
	    newScores.add(scores);
	}

	return newScores;
    }

    private static String removeQWords(String x) {
	String  tmp = x.toLowerCase();
	tmp = tmp.replace("ne", "");
	tmp = tmp.replace("hangi", "");
	tmp = tmp.replace("kim", "");
	tmp = tmp.replace("nerede", "");
	tmp = tmp.replace("nasýl", "");
	tmp = tmp.replace("kaç", "");
	tmp = tmp.replace("ad", "");
	tmp = tmp.replace("verilir", "");
	tmp = tmp.replace("neye", "");
	tmp = tmp.replace("neyi", "");
	tmp = tmp.replace("kaçtýr", "");
	tmp = tmp.replace("nasýldýr", "");
	tmp = tmp.replace("nedir", "");
	tmp = tmp.replace("verilir", "");
	tmp = tmp.replace("nedendir", "");
	tmp = tmp.replace("nelerdir", "");
	tmp = tmp.replace("açýklanýr", "");
	tmp = tmp.replace("açýklanabilir", ""); 
	tmp = tmp.replace("neler", ""); 
	return tmp;
    }

    private static Wrapper2 createSentences(String[] bodies) {

	ArrayList<String[]> sentenceLists = new ArrayList<String[]>();
	ArrayList<Integer> noList = new ArrayList<Integer>();

	for (int i = 0; i<bodies.length; i++) {
	    //System.out.println(i);
	    String[] sentences = bodies[i].split("(?<=[.?!])\\s+(?=[a-zA-Z])");
	    
	    int no = sentences.length;
	    noList.add(no);

	    sentenceLists.add(sentences);
	}

	return new Wrapper2(sentenceLists, noList);
    }
    
    public static ArrayList<ArrayList<Double>> score_sentences(ArrayList<ArrayList<Double>> initScores, ArrayList<ArrayList<String>> kelimeFrekansList, ArrayList<String[]> sentenceLists, ArrayList<Integer> noList) {

	Double incrementConstant = 0.2;
	
	ArrayList<ArrayList<String>> kelimeZincirleri = lexical_chain(kelimeFrekansList);

	int docNum = sentenceLists.size();

	ArrayList<ArrayList<Double>> finalScores = new ArrayList<ArrayList<Double>>();
	
	for (int doc = 0; doc<docNum; doc++) {

	    String[] sentences = sentenceLists.get(doc);
	    int no = noList.get(doc);
	    ArrayList<String> kelimeFrekans = kelimeFrekansList.get(doc);
	    ArrayList<Double> initDocScores = initScores.get(doc);
	    ArrayList<String> kelimeZinciri = kelimeZincirleri.get(doc);
	    ArrayList<Double> finalDocScores = new ArrayList<Double>();
	    
         for (int i = 0; i<no; i++) {

		double score = initDocScores.get(i);
		
		for (int j = 0; j<kelimeFrekans.size(); j++) {
		    if (sentences[i].contains(kelimeFrekans.get(j))) {
// TUNGA ***			score += incrementConstant;
			score += 0;
		    }
		}

		if (!kelimeZinciri.isEmpty()) {
		    String sen = sentences[i];
		    
		    if(sen.contains(kelimeZinciri.get(0))) {
// TUNGA ***			score += incrementConstant;
			score += 0;
		    }
		    if(sen.contains(kelimeZinciri.get(1))) {
// TUNGA ***			score += incrementConstant;
			score += 0;
		    }
		    if(sen.contains(kelimeZinciri.get(2))) {
// TUNGA ***			score += incrementConstant;
			score += 0;
		    }
		}

		finalDocScores.add(score);
	    }

	    finalScores.add(finalDocScores);
	}

	return finalScores;
    }

    private static ArrayList<TreeMap<String, Integer>> word_frequency(ArrayList<ArrayList<String>> rootLists, ArrayList<ArrayList<String>> keywordLists) {

	ArrayList<TreeMap<String, Integer>> freqMaps = new ArrayList<TreeMap<String, Integer>>();

	for (int i = 0; i<rootLists.size(); i++) {
	    ArrayList<String> rootList = rootLists.get(i);

	    int size = rootList.size();
	    int count = 1;

	    ArrayList<String> currentKeywordList = keywordLists.get(i);
	    TreeMap<String, Integer> freqMapTmp = new TreeMap<String, Integer>();
	    
	    for (int j = 0; j<size; j++) {
		
		for (int k = 0; k<currentKeywordList.size(); k++) {
		   
		    if (currentKeywordList.get(k).contains(rootList.get(j)))
			count++;
		}
		freqMapTmp.put(rootList.get(j), count);
		sortByFreq(rootList.get(j), count);
		count = 1;
	    }
	    freqMaps.add(freqMapTmp);
	}

	return freqMaps;
    }

    static void sortByFreq(String a, int c){
	Map<String, Integer> map = new TreeMap<> ();
    
	/* Logic to place the elements to Map */
   
	if(map.get(a) == null){
	    map.put(a, c);
        }
	else{
	    int frequency = map.get(a);
	    map.put(a, frequency+1);
	}
   
	List list = new LinkedList<>(map.entrySet());
 //  List<Entry> list = new List<Entry>();
//   list.add(map.entrySet());
try{
	/* Sort the list elements based on frequency */
	Collections.sort(list, new Comparator() {
		@Override
		public int compare(Object obj1, Object obj2) {
		    return ((Comparable) ((Map.Entry) (obj1)).getValue())
			.compareTo(((Map.Entry) (obj2)).getValue());
		}
	    });
   
	int count=0;
   
	/* Place the elements in to the array based on frequency */
	for (Iterator it = list.iterator(); it.hasNext();) {
	    Map.Entry entry = (Map.Entry) it.next();
        
	    String key = (String)entry.getKey();
	    int val = (int)entry.getValue();
        
	    for(int i=0; i < val; i++){
		a = key;
		count++;
	    }            
	} 
}
catch (ArrayIndexOutOfBoundsException exception){
System.out.println("error");
}

    }

    private static ArrayList<ArrayList<String>> createFreq(ArrayList<TreeMap<String, Integer>> freqMaps) {

	ArrayList<ArrayList<String>> kelimeFrekansLists = new ArrayList<ArrayList<String>>();
	
	for (int i = 0; i<freqMaps.size(); i++) {
	    TreeMap<String, Integer> freqMap = freqMaps.get(i);
	    
	    Set<String> keys = freqMap.keySet();
	    int numWord = keys.size();
	    Iterator<String> iterator = keys.iterator();

	    ArrayList<String> kelimeFrekans = new ArrayList<String>();
	    
	    while(iterator.hasNext()) {
		String word = iterator.next();
		int count = freqMap.get(word);
		if (count>15 && count<120 && word.length()>2) {
		    kelimeFrekans.add(word);
		}
	    }
	    kelimeFrekansLists.add(kelimeFrekans);
	}

	return kelimeFrekansLists;
    }

    private static ArrayList<ArrayList<String>> lexical_chain(ArrayList<ArrayList<String>> kelimeFrekansLists) {
	ReadXMLFile.ReadXML();
	ArrayList<String> title = ReadXMLFile.titles2;
	ArrayList<String> sub = ReadXMLFile.sub;
	ArrayList<String> sub2 = ReadXMLFile.sub2;
	ArrayList<String> sub3 = ReadXMLFile.sub3;

	ArrayList<ArrayList<String>> kelimeZincirleri = new ArrayList<ArrayList<String>>();

	for (int u = 0; u<kelimeFrekansLists.size(); u++) {
	    ArrayList<String> kelimeFrekans = kelimeFrekansLists.get(u);
	    int someConstant = 146;
	    ArrayList<String> kelimeZinciri = new ArrayList<String>();
	    
	    for (int i = 0; i<kelimeFrekans.size(); i++) {
		for (int j = 0; j<someConstant; j++) {
		    String kelime = kelimeFrekans.get(i);
		    if (kelime == title.get(i) || kelime == sub.get(i) || kelime == sub2.get(i) || kelime == sub3.get(i)) {
			    kelimeZinciri.add(title.get(i));
			    kelimeZinciri.add(sub.get(i));
			    kelimeZinciri.add(sub2.get(i));
			    kelimeZinciri.add(sub3.get(i));
		    } else {
			kelimeZinciri.clear();
		    }
		}
	    }
	    kelimeZincirleri.add(kelimeZinciri);
	}

	return kelimeZincirleri;
    }

    public static int countWords(String s) {

	int wordCount = 0;

	boolean word = false;
	int endOfLine = s.length() - 1;

	for (int i = 0; i < s.length(); i++) {
	    // if the char is a letter, word = true.
	    if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
		word = true;
		// if char isn't a letter and there have been letters before,
		// counter goes up.
	    } else if (!Character.isLetter(s.charAt(i)) && word) {
		wordCount++;
		word = false;
		// last word of String; if it doesn't end with a non letter, it
		// wouldn't count without this.
	    } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
		wordCount++;
	    }
	}
	return wordCount;
    }

    private static void sortAllSentences(ArrayList<ArrayList<Double>> allDocScores, ArrayList<Integer> noList, ArrayList<String[]> sentencesList) {

	MyQuickSort sorter = new MyQuickSort();
	
	for (int doc = 0; doc<allDocScores.size(); doc++) {
	    ArrayList<Double> docScores = allDocScores.get(doc);
	    int no = noList.get(doc);
	    ArrayList<String> list = new ArrayList<String>();
	    ArrayList<Double> score = new ArrayList<Double>();
	    ArrayList<Integer> index = new ArrayList<Integer>();

	    String[] sentences = sentencesList.get(doc);
	    
	    double[] scores = new double[docScores.size()];
	    for (int qq = 0; qq<scores.length; qq++) {
		scores[qq] = docScores.get(qq);
	    }

	    double[] input = scores.clone(); // why exactly?
	    sorter.sort(input);
	    String[] candidates;

	    // BEWARE: named unique, but this doesn't make a unique array
	    // it only looks one step behind, e.g. [2,3,2,4] will pass
	    final double[] unique = new double[input.length];
	    double prev = input[0];
	    unique[0] = prev;
	    int count1 = 0;
	    for (int i = 1; i<input.length; ++i) {
		if (input[i] != prev) {
		    unique[count1++] = input[i];
		}
		prev = input[i];
	    }
	    final double[] compressed = new double[count1];
	    System.arraycopy(unique, 0, compressed, 0, count1);
	    int m = compressed.length;
	    /*
	    int size;
	    if (m > 20) {
		size = m/10;
	    } else if (m > 10 && m < 21) {
		size = m/5;
	    } else {
		// what if m == 1 ??
		if (m <= 1) {
		    size = 1;
		} else {
		    size = 2;
		}
	    }
	    */
	    //System.out.println(m);

	    if (m>2 && no!=0) {
		for(int i=0; i<no-1;i++)    {
		    for (int x = m-1; x>=m-3;x--) {
			//System.out.println(compressed[x]);
			if(scores[i] == compressed[x]) {
			    System.out.println(sentences[i]);
			    list.add(sentences[i]);
			    score.add(scores[i]);
			    index.add(i);
			}
		    }
		}
	    } 
          if(m==2 && no!=0) {
                if(no>2){
		for(int i=0; i<no-1;i++){  
		   if(scores[i]==compressed[m-1] ) {
			System.out.println(sentences[i]);
			list.add(sentences[i]); // to keep the order of sentences and scores
			score.add(scores[i]);
			index.add(i);
		    }
		}
	    }
           if(no==2){
            for(int i=0; i<no;i++){  
		   if(scores[i]==compressed[m] ) {
			System.out.println(sentences[i]);
			list.add(sentences[i]); // to keep the order of sentences and scores
			score.add(scores[i]);
			index.add(i);
		    }
		}

            }
            if(no==1)
             System.out.println(sentences[0]);

     }
        if(m==1 && no!=0) {
           for(int i=0; i<no-1;i++){  
		   if(scores[i]==compressed[m-1] )
         System.out.println(sentences[i]);
			list.add(sentences[i]); // to keep the order of sentences and scores
			score.add(scores[i]);
			index.add(i);
    }
}
          if(m==0){

          System.out.println(sentences[0]);
           }

          if(no==0)
           System.out.println("");

	    ArrayList<Double> copy = (ArrayList<Double>) score.clone();
	    Collections.sort(score);
	    Double[] max = new Double[score.size()];
	    int[] indices = new int[score.size()];
	    for (int j = score.size() - 1; j>=0; j--) {
		max[j] = score.get(j);
		indices[j] = copy.indexOf(max[j]);
		// indice.add(j) <-- no one uses it, hence comment out
	    }
	    
	    System.out.println("******");
	}
    }

    // END OF CLASS Multisum
}


