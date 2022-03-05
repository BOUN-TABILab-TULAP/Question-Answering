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

//kelime zinciri 2-3 yarat,scores 2-3 yarat ,frekans 2-3 yarat, scores[] u global tanimla veya o score bilgisini 
//siraladiktan sonra gonder. (doc adi ve de hangi dok oldugu bilinsin.), quicksortu uygula
// 3 dokumanin ozeti olussun
// sonraki step unique rootlist ile cumlelerdeki kelimeleri kiyaslamak
public class multisum {
    public static	BufferedReader in;
    public static List<String> contents = new ArrayList<String>();
    public static List<String> titles = new ArrayList<String>();
    public  static String[]  bodies= new String[4];
    public  static String[]  titless= new String[4];
    public static ArrayList<String[]> keywords = new ArrayList<String[]>();
    public static ArrayList<String> keywordsList = new ArrayList<String>();
    public static ArrayList<String> keywordsList2 = new ArrayList<String>();
    public static ArrayList<String> keywordsList3 = new ArrayList<String>();
    public static ArrayList<String> ukeywordsList = new ArrayList<String>();
    public static ArrayList<String> ukeywordsList2 = new ArrayList<String>();
    public static ArrayList<String> ukeywordsList3 = new ArrayList<String>();
    private static TreeMap<String, Integer> freqMap=new TreeMap<String,Integer>();
    private static TreeMap<String, Integer> freqMap2=new TreeMap<String,Integer>();
    private static TreeMap<String, Integer> freqMap3=new TreeMap<String,Integer>();
    public  static  ArrayList<String> kelimefrekans = new ArrayList<String>(); 
    public  static  ArrayList<String> kelimefrekans2 = new ArrayList<String>();
    public  static  ArrayList<String> kelimefrekans3 = new ArrayList<String>();
    public  static  ArrayList<String> kelimezinciri = new ArrayList<String>();
    public  static  ArrayList<String> kelimezinciri2 = new ArrayList<String>();
    public  static  ArrayList<String> kelimezinciri3 = new ArrayList<String>();
    public static ArrayList<String> list = new ArrayList<String>();
    public static ArrayList<Double> score = new ArrayList<Double>();
    public static ArrayList<Integer> index = new ArrayList<Integer>();
    public static ArrayList<String> list2 = new ArrayList<String>();
    public static ArrayList<Double> score2 = new ArrayList<Double>();
    public static ArrayList<String> list3 = new ArrayList<String>();
    public static ArrayList<Double> score3 = new ArrayList<Double>();
    public static ArrayList<Integer> index2 = new ArrayList<Integer>();
    public static ArrayList<Integer> index3 = new ArrayList<Integer>();


    public static ArrayList<String> candidateSentences1 = new ArrayList<String>();
    public static ArrayList<String> candidateSentences2 = new ArrayList<String>();
    public static ArrayList<String> candidateSentences3 = new ArrayList<String>();

    public static ArrayList<Integer> indice = new ArrayList<Integer>();
    public static ArrayList<Integer> indice2 = new ArrayList<Integer>();
    public static ArrayList<Integer> indice3 = new ArrayList<Integer>();
    public static ArrayList<String> sent = new ArrayList<String>();
    public static String soru;
    public static int no;
    public static int no2;
    public static int no3;
    public static double[] scores=new double[500];	
    public static double[] scores2=new double[500];
    public static double[] scores3=new double[500];
    public  static  ArrayList<String> rootList = new ArrayList<String>();
    public  static  ArrayList<String> qrootList = new ArrayList<String>();
    public  static  ArrayList<String> rootList2 = new ArrayList<String>();
    public  static  ArrayList<String> rootList3 = new ArrayList<String>();
    public  static  ArrayList<String> rootListall = new ArrayList<String>();
    public  static  ArrayList<String> allroot = new ArrayList<String>();
    public  static String[]  sentences;
    public  static String[] sentences2;
    public  static String[] sentences3;
    public  static String[]  titles1;
    public  static String[] titles2;
    public  static String[] titles3;
    public static ArrayList<String[]> SentenceList = new ArrayList<String[]>();
    public static ArrayList<String[]> SentenceList2 = new ArrayList<String[]>();
    public static ArrayList<String[]> SentenceList3 = new ArrayList<String[]>();

    public static ArrayList<String> TitleRoots = new ArrayList<String>();
    public static ArrayList<String> Title2Roots = new ArrayList<String>();
    public static ArrayList<String> Title3Roots = new ArrayList<String>();

    public static ArrayList<String> title = new ArrayList<String>();
    public static ArrayList<String> sub = new ArrayList<String>();
    public static ArrayList<String> sub2 = new ArrayList<String>();
    public static ArrayList<String> sub3 = new ArrayList<String>();

    public static int size=3;
    public static void main(String[] args)  throws FileNotFoundException, IOException,NullPointerException { 
	
	setSize(3);

	
	if(args.length < 2) {
	    throw new IOException("USAGE : <relatedFileName> <question> \nGiven: " + args.toString());
	}

	System.out.println("Given Related File Name : " + args[0]);
	System.out.println("Given Quesion : " + args[1]);
	//String filename="paratest.txt";
	String filename = args[0];
	
	readFile(filename);
    
	for(int k=0;k<size;k++) {
	    bodies[k]= getBody(filename,k);
	    titless[k]= getTitle(filename,k);
	    bodies[k].replaceAll("","");
	    bodies[k].replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
	    keywords.add(bodies[k].toLowerCase().split("[ ]+"));
	}
	titles1= titless[0].split("\\s+");
	titles2= titless[1].split("\\s+");
	titles3= titless[2].split("\\s+");
	rootTitles(titles1, titles2, titles3);
    
	CreateWordList();
	CreateRootList();
	createSentences(bodies[0],bodies[1],bodies[2]);

	
	String question = args[1]; 
	getInputfromUser(question);
   
	word_frequency();
	createFreq();// createFreq'i word_frequency den  cagirinca hatali sonuc veriyordu.
    
	score_sentences();
	
	System.out.println("******");
	sortSentences();    
	System.out.println("******");
	sortSentences2();
	System.out.println("******");
	sortSentences3();  
	// form_summary();
    }

    public static void readFile(String filename) throws IOException {
	//	 List<String> contents = new ArrayList<String>();
	//	 List<String> titles = new ArrayList<String>();
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
    }
    public static void setSize(int n){
	size=n;
    }

    public static String getBody(String fname, int index){
	String s;
	s= contents.get(index);
	return s;
    }
    public static String getTitle(String fname, int index){
	String s;
	s= titles.get(index);
	return s;
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

    private static void CreateWordList() {
	for(String word1:keywords.get(0)){
			
	    word1 = removeStopWords(processWord(word1));

	    keywordsList.add(word1);
	}
	for(String word2:keywords.get(1)){
			
	    word2 = removeStopWords(processWord(word2));

	    keywordsList2.add(word2);
	}
	    
	for(String word3:keywords.get(2)){
			
	    word3 = removeStopWords(processWord(word3));
	    keywordsList3.add(word3);
	}
	
	HashSet hs = new HashSet();
	hs.addAll(keywordsList);
	hs.addAll(keywordsList2);
	hs.addAll(keywordsList3);
	ukeywordsList.addAll(hs);
	
    }
    private static void CreateRootList() {
	turkishStemmer stemmer = new turkishStemmer();
	turkishStemmer stemmer2 = new turkishStemmer();
	turkishStemmer stemmer3 = new turkishStemmer();
	String word="";
	for(int j=0; j<keywordsList.size();j++){	
	    stemmer.setCurrent(keywordsList.get(j));
	    if (stemmer.stem()){
		rootList.add(processWord(stemmer.getCurrent()));
	    }
	}
    
	for(int m=0; m<keywordsList2.size();m++){	
	    stemmer2.setCurrent(keywordsList2.get(m));
	    if (stemmer2.stem()){
		rootList2.add(processWord(stemmer2.getCurrent()));
	    }
	}
	for(int t=0; t<keywordsList3.size();t++){	
    	    stemmer3.setCurrent(keywordsList3.get(t));
	    if (stemmer3.stem()){
		rootList3.add(processWord(stemmer3.getCurrent()));
	    } 
	}
	uniqueRootList();
    }
    public static void uniqueRootList(){
	String alllist;

	HashSet hs4 = new HashSet();
	hs4.addAll(rootList);
	hs4.addAll(rootList2);
	hs4.addAll(rootList3);
	rootListall.addAll(hs4);
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
    private static void getInputfromUser(String question) {
	
	//Scanner userinput = new Scanner(System.in);
	//System.out.println("Soruyu giriniz: ");
	//soru = userinput.nextLine();
	soru = question;
	
	soru.toLowerCase();
	soru=removeQWords(soru);
	String[] wordsq=soru.trim().split(" ");
	turkishStemmer stemmer1 = new turkishStemmer();
    
	for(int j=0; j<wordsq.length;j++){
  	
	    stemmer1.setCurrent(wordsq[j]);
  
	    if (stemmer1.stem()){
		qrootList.add(stemmer1.getCurrent());
	    }
	}
	for(int i=0;i<sentences.length;i++){
	    for(int k=0;k<wordsq.length;k++){
		if(sentences[i].contains(wordsq[k]))
		    scores[i]=scores[i]+1; }
	}
	for(int i=0;i<sentences2.length;i++){
	    for(int k=0;k<wordsq.length;k++){
	   	if(sentences2[i].contains(wordsq[k]))
		    scores2[i]=scores2[i]+1; }
	}
	for(int i=0;i<sentences3.length;i++){
	    for(int k=0;k<wordsq.length;k++){
	   	if(sentences3[i].contains(wordsq[k]))
		    scores3[i]=scores3[i]+1; }
	}
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
    //calculate_score(String, int index)
    private static void createSentences(String s, String s2, String s3){
	sentences= s.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
	sentences2= s2.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
	sentences3= s3.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
	no=sentences.length;
	no2=sentences2.length;
	no3=sentences3.length;

	int k;
	for (k=0; k<no;k++){
	    scores[k]=0;
	    SentenceList.add(sentences[k].trim().split(" "));

	    /*for(int l=0;k<TitleRoots.size();l++){
	      if(sentences[k].contains(TitleRoots.get(l)))
	      scores[k]=scores[k]+ (TitleRoots.size()/ (TitleRoots.size()+SentenceList.get(k).length));
	      }
	    */
	}
	for (k=0; k<no2;k++){
	    scores2[k]=0;
	    SentenceList2.add(sentences2[k].trim().split(" "));
	}

	for (k=0; k<no3;k++){
	    scores3[k]=0;
	    SentenceList3.add(sentences3[k].trim().split(" "));
	}

    }


    // override the method if done for single summary
    private static void createSentences(String s){
	sentences= s.split("(?<=[.?!])\\s+(?=[a-zA-Z])");
    }

    private static void rootTitles(String[] t, String[] t2, String[] t3){
	
	turkishStemmer stemmer1 = new turkishStemmer();
	turkishStemmer stemmer2 = new turkishStemmer();
	turkishStemmer stemmer3 = new turkishStemmer();

	for(int j=0; j<t.length;j++){
	  	
	    stemmer1.setCurrent(t[j]);
	  
	    if (stemmer1.stem()){
		TitleRoots.add(stemmer1.getCurrent());
	    }
	}
	for(int j=0; j<t2.length;j++){
	  	
	    stemmer2.setCurrent(t2[j]);
	  
	    if (stemmer2.stem()){
		Title2Roots.add(stemmer2.getCurrent());
	    }
	}
	for(int j=0; j<t3.length;j++){
  	
	    stemmer3.setCurrent(t3[j]);
  
	    if (stemmer3.stem()){
		Title3Roots.add(stemmer2.getCurrent());
	    }
	}

    }
    public static void score_sentences(){
	lexical_chain();
	for(int i=0; i<no;i++){

	    for(int j=0;j<kelimefrekans.size(); j++ )
		if(sentences[i].contains(kelimefrekans.get(j)))
		    scores[i]=scores[i]+0.2;
    	    if(kelimezinciri.isEmpty()==false)
		{
		    if(sentences[i].contains(kelimezinciri.get(0)))
			scores[i]=scores[i]+0.2;
		    if(sentences[i].contains(kelimezinciri.get(1)))
    	    		scores[i]=scores[i]+0.2;
		    if(sentences[i].contains(kelimezinciri.get(2)))
    	    		scores[i]=scores[i]+0.2;
		} 			  
	}
	
	for(int i=0; i<no2;i++){
	
		

	    for(int j=0;j<kelimefrekans2.size(); j++ )
		if(sentences2[i].contains(kelimefrekans2.get(j)))
		    scores2[i]=scores2[i]+0.2;
	    if(kelimezinciri2.isEmpty()==false)
		{
		    if(sentences2[i].contains(kelimezinciri2.get(0)))
			scores2[i]=scores2[i]+0.2;
		    if(sentences2[i].contains(kelimezinciri2.get(1)))
			scores2[i]=scores2[i]+0.2;
		    if(sentences2[i].contains(kelimezinciri2.get(2)))
			scores2[i]=scores2[i]+0.2;
		} 			  
	}

	for(int i=0; i<no3;i++){
	
	    for(int j=0;j<kelimefrekans3.size(); j++ )
		if(sentences3[i].contains(kelimefrekans3.get(j)))
		    scores3[i]=scores3[i]+0.2;
	    if(kelimezinciri3.isEmpty()==false)
		{
		    if(sentences3[i].contains(kelimezinciri3.get(0)))
			scores3[i]=scores3[i]+0.2;
		    if(sentences3[i].contains(kelimezinciri3.get(1)))
			scores3[i]=scores3[i]+0.2;
		    if(sentences3[i].contains(kelimezinciri3.get(2)))
			scores3[i]=scores3[i]+0.2;
		} 			  
	}
	
    }
    private static void word_frequency(){
	int size1= rootList.size();
	int size2= rootList2.size();
	int size3= rootList3.size();
	int count=1;
	int count2=1;
	int count3=1;
	for(int j=0; j<size1;j++){
    			  
	    for(int i=0; i<keywordsList.size();i++){
		if(keywordsList.get(i).contains(rootList.get(j)))
		    count++;
	    }
	    freqMap.put(rootList.get(j),count);
	    sortByFreq(rootList.get(j),count);
	    count=1;
	}
	for(int j=0; j<size2;j++){
    			  
	    for(int i=0; i<keywordsList2.size();i++){
		if(keywordsList2.get(i).contains(rootList2.get(j)))
		    count++;
	    }
	    freqMap2.put(rootList2.get(j),count);
	    sortByFreq(rootList2.get(j),count);
	    count=1;
	}		
	for(int j=0; j<size3;j++){
	  
	    for(int i=0; i<keywordsList3.size();i++){
		if(keywordsList3.get(i).contains(rootList3.get(j)))
		    count++;
	    }
	    freqMap3.put(rootList3.get(j),count);
	    sortByFreq(rootList3.get(j),count);
	    count=1;
	}		  
 
    }
    public static boolean isNumeric( String input )  
    {  
	try  
	    {  
		Double.parseDouble( input );  
		return true;  
	    }  
	catch( Exception e )  
	    {  
		return false;  
	    }  
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
   
	//System.out.println(map);
   
	List list = new LinkedList(map.entrySet());
   
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

    private static void createFreq(){
	Set<String> keys=freqMap.keySet();
	int numWord=keys.size();
	Iterator<String> iterator=keys.iterator();
	while(iterator.hasNext()){
	    String word1=iterator.next();
	    int count=freqMap.get(word1); 
	    if(count>15 && count<120 && word1.length()>2){
		kelimefrekans.add(word1);
	    }
	}
	Set<String> keys2=freqMap2.keySet();
	int numWord2=keys2.size();
	Iterator<String> iterator2=keys2.iterator();
	while(iterator2.hasNext()){
	    String word2=iterator2.next();
	    int count2=freqMap2.get(word2); 
	    if(count2>15 && count2<120 && word2.length()>2){
		kelimefrekans2.add(word2);
	    }
	}	 
	Set<String> keys3=freqMap3.keySet();
	int numWord3=keys3.size();
	Iterator<String> iterator3=keys3.iterator();
	while(iterator3.hasNext()){
	    String word3=iterator3.next();
	    int count3=freqMap3.get(word3); 
	    if(count3>15 && count3<120 && word3.length()>2){
		kelimefrekans3.add(word3);
	    }
	}	  
	 
	 
    }

    private static void lexical_chain()
    {
	ReadXMLFile.ReadXML();
	title=ReadXMLFile.titles2;
	sub=ReadXMLFile.sub;
	sub2=ReadXMLFile.sub2;
	sub3=ReadXMLFile.sub3;
			
	for(int i=0; i<kelimefrekans.size();i++ ) 
	    for(int j=0; j<65;j++ ) 
		{
		    if(kelimefrekans.get(i)==title.get(i) || kelimefrekans.get(i)==sub.get(i)|| kelimefrekans.get(i)==sub2.get(i) || kelimefrekans.get(i)==sub3.get(i)  )
			{
			    kelimezinciri.add(title.get(i));
			    kelimezinciri.add(sub.get(i));
			    kelimezinciri.add(sub2.get(i));
			    kelimezinciri.add(sub3.get(i));
			}
		    else
	    		kelimezinciri.clear();
		}
	  
	for(int i=0; i<kelimefrekans2.size();i++ ) 
	    for(int j=0; j<65;j++ ) 
		{
		    if(kelimefrekans2.get(i)==title.get(i) || kelimefrekans2.get(i)==sub.get(i)|| kelimefrekans2.get(i)==sub2.get(i) || kelimefrekans2.get(i)==sub3.get(i)  )
			{
			    kelimezinciri2.add(title.get(i));
			    kelimezinciri2.add(sub.get(i));
			    kelimezinciri2.add(sub2.get(i));
			    kelimezinciri2.add(sub3.get(i));
			}
		    else
	    		kelimezinciri2.clear();
		}   
	for(int i=0; i<kelimefrekans3.size();i++ ) 
	    for(int j=0; j<65;j++ ) 
		{
		    if(kelimefrekans3.get(i)==title.get(i) || kelimefrekans3.get(i)==sub.get(i)|| kelimefrekans3.get(i)==sub2.get(i) || kelimefrekans3.get(i)==sub3.get(i)  )
			{
			    kelimezinciri3.add(title.get(i));
			    kelimezinciri3.add(sub.get(i));
			    kelimezinciri3.add(sub2.get(i));
			    kelimezinciri3.add(sub3.get(i));
			}
		    else
	    		kelimezinciri3.clear();
		}   
	    
    }
    public static int countWords(String s){

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

    private static void sortSentences(){

	MyQuickSort sorter = new MyQuickSort();
	double[] input = scores.clone();
	sorter.sort(input);
	String[] candidates;
    
    
	final double[] unqiue = new double[input.length];
	double prev = input[0];
	unqiue[0] = prev;
	int count1 = 1;
	for (int i = 1; i < input.length; ++i) {
	    if (input[i] != prev) {
		unqiue[count1++] = input[i];
	    }
	    prev = input[i];
	}
	final double[] compressed = new double[count1];
	System.arraycopy(unqiue, 0, compressed, 0, count1);
	int m=compressed.length;
	int size;
	if(compressed.length>20)
	    size = compressed.length/10;
	if(compressed.length>10 && compressed.length<21 )
	    size = compressed.length/5;
	else
	    size=2;

	for(int i=0; i<no-1;i++)	{
	    for(int x=m-1;x>=m-size;x--){
		if(scores[i]==compressed[x] && scores[i]!=0) {
		    System.out.println(sentences[i]);
		    list.add(sentences[i]); // to keep the order of sentences and scores
		    score.add(scores[i]);
		    index.add(i);
		}
	    }
	    ArrayList<Double> copy = (ArrayList<Double>) score.clone(); 
	    Collections.sort(score);
	    Double[] max= new Double[score.size()];
	    int[] indices = new int[score.size()];
	    for(int j=score.size() - 1; j>=0 ;j--){
		max[j]= score.get(j); //gets the last item, largest for an ascending sort
		indices[j]=copy.indexOf(max[j]); //index of highest scores
		candidateSentences1.add(sentences[j]);
		indice.add(j);
	    }
	}
     
    }  
          
       
    

    private static void sortSentences2(){
	MyQuickSort sorter2 = new MyQuickSort();
	double[] input2 = scores2.clone();
	sorter2.sort(input2);
	final double[] unqiue2 = new double[input2.length];
	double prev2 = input2[0];
	unqiue2[0] = prev2;
	int count2 = 1;
	for (int i = 1; i < input2.length; ++i) {
	    if (input2[i] != prev2) {
		unqiue2[count2++] = input2[i];
	    }
	    prev2 = input2[i];
	}
	final double[] compressed2 = new double[count2];
	System.arraycopy(unqiue2, 0, compressed2, 0, count2);
	int m2=compressed2.length;
	int size2;
	if(compressed2.length>20)
	    size2 = compressed2.length/10;
	if(compressed2.length>10 && compressed2.length<21 )
	    size2 = compressed2.length/5;
	else
	    size2=2;
	for(int i=0; i<no2-1;i++)	{
	    for(int x=m2-1;x>=m2-size2;x--){
	        	
		if(scores2[i]==compressed2[x] && scores2[i]!=0) {
		    System.out.println( sentences2[i]);
		    list2.add(sentences2[i]); // to keep the order of sentences and scores
		    score2.add(scores[i]);
		    index2.add(i);
		}
	    }
	    ArrayList<Double> copy2 = (ArrayList<Double>) score2.clone(); 
	    Collections.sort(score2);
		        
	    Double[] max= new Double[score2.size()];
	    int[] indices = new int[score2.size()];
	    for(int j=score2.size() - 1; j>=0 ;j--){
		max[j]= score2.get(j); //gets the last item, largest for an ascending sort
		indices[j]=copy2.indexOf(max[j]); //index of highest scores
		candidateSentences2.add(sentences2[j]);
		indice2.add(j);
	    }
	}
    }

    private static void sortSentences3(){
	MyQuickSort sorter3 = new MyQuickSort();
	double[] input3 = scores3.clone();
	sorter3.sort(input3);
	final double[] unqiue3 = new double[input3.length];
	double prev3 = input3[0];
	unqiue3[0] = prev3;
	int count3 = 1;
	for (int i = 1; i < input3.length; ++i) {
	    if (input3[i] != prev3) {
		unqiue3[count3++] = input3[i];
	    }
	    prev3 = input3[i];
	}
	final double[] compressed3 = new double[count3];
	System.arraycopy(unqiue3, 0, compressed3, 0, count3);
	int m3=compressed3.length;
	int size3;
	if(compressed3.length>20)
	    size3 = compressed3.length/10;
	if(compressed3.length>10 && compressed3.length<21  )
	    size3 = compressed3.length/5;
	else
	    size3=2;
	    
	for(int i=0; i<no3-1;i++)	{
	    for(int x=m3-1;x>=m3-size3;x--){
		if(scores3[i]==compressed3[x] && scores3[i]!=0) {
		    System.out.println( sentences3[i]);
		    list3.add(sentences3[i]); // to keep the order of sentences and scores
		    score3.add(scores[i]);
		    index3.add(i);
		}
	    }
	}
	ArrayList<Double> copy3 = (ArrayList<Double>) score3.clone(); 
	Collections.sort(score3);
	Double[] max= new Double[score3.size()];
	int[] indices = new int[score3.size()];
	for(int j=score3.size() - 1; j>=0 ;j--){
	    max[j]= score3.get(j); //gets the last item, largest for an ascending sort
	    indices[j]=copy3.indexOf(max[j]); //index of highest scores
	    candidateSentences3.add(sentences3[j]);
	    indice3.add(j);
	}
    }

    private static void form_summary(){
	int k=rootListall.size();
	int m= candidateSentences3.size();
	String [] wordsOfRoot = new String[k];
	for(int i=0;i>k;i++)
	    {
		wordsOfRoot[i]=rootListall.get(i);
	    }
	String words3[][]= new String[k][candidateSentences3.size()];
	int arr3[][]=new int[k][m] ;
	for(int i=0;i>k;i++){
	    for(int j=0;j<candidateSentences3.size(); j++){
		words3[i][j]=candidateSentences3.get(j).trim().split(" ").toString();

		if(words3[i][j].contains(rootListall.get(i).toString()))	
		    arr3[i][j]=1;
		else
		    arr3[i][j]=0;
	    }

	}
	String words2[][]= new String[k][candidateSentences2.size()];
	int arr2[][]=new int[k][m];
	for(int i=0;i>k;i++){
	    for(int j=0;j<candidateSentences2.size(); j++){
		words2[i][j]=candidateSentences2.get(j).trim().split(" ").toString();

		if(words2[i][j].contains(rootListall.get(i).toString()))	
		    arr2[i][j]=1;
		else
		    arr2[i][j]=0;

	    }
	}

	int arr[][]=new int[k][m];
	String words[][]= new String[k][candidateSentences1.size()];

	for(int i=0;i>k;i++){
	    for(int j=0;j<candidateSentences1.size(); j++){
		words[i][j]=candidateSentences1.get(j).trim().split(" ").toString(); //multidimensional array yap

		if(words[i][j].contains(rootListall.get(i).toString()))	
		    arr[i][j]=1;
		else
		    arr[i][j]=0;
	    }
	}

	double result=0;
	double norm1=0;
	double norm2=0;
	double similarity;

	for(int i=0;i>k;i++){
	    //	for(int j=0;j<5; j++){ //norm arrayi  falan olacak ve rakam yerine j olacak sagda
	    result=arr[i][1]*arr2[i][1];
	    norm1=Math.pow(arr[i][1],2);
	    norm2=Math.pow(arr2[i][1],2);
	    //}
	    System.out.println(arr[i][1]);
	}

	similarity= (result/(Math.sqrt(norm1)+Math.sqrt(norm2)));
	System.out.println(similarity);
    }
}

