import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;

public class InvertIndex {
	
	static StandardTokenizer tok;
	static String docsDirPath = "data/txt"; // directory containing documents
	static File docDir = new File(docsDirPath); 
	Map <String,Integer> freq ; //Map with doc frequency per word-[
	Map <String,HashSet<String>> inv_index ; //Map with list of docs per word
	Map <String,Map<String,Double>> doc_terms; //terms in each document
	Map<String, Double> tf_vec; 
	@SuppressWarnings("deprecation")
	
	public static void main(String[] args) throws IOException{ //run errythang
		InvertIndex InvIndex = new InvertIndex(new HashMap<String,Integer>(),new HashMap<String, HashSet<String>>(), 
				new HashMap <String,Map<String,Double>>());
		//Tokenize and create indices while reading each file
		
		
		
		for (File f : docDir.listFiles()) { //read each file from directory
			
			FileReader fread = new FileReader(f);
			//strip .txt
			int str_size = f.getName().length();
			String doc_string = f.getName().substring(0,str_size-4);
			Map<String,Double> count_map =  new HashMap<String,Double>();
			InvIndex.doc_terms.put(doc_string,count_map);
			
			//System.out.println(doc_string);
			//text tokenization per file
			tok = new StandardTokenizer(Version.LUCENE_40,fread);
			//set max token length? 
			TokenStream tokenStream = new LowerCaseFilter(Version.LUCENE_40,tok); //gave tokenStream tokenizer
			tokenStream = new StopFilter(Version.LUCENE_40,tokenStream,StandardAnalyzer.STOP_WORDS_SET);
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			tokenStream.reset();
			
			while(tokenStream.incrementToken()){//while token exists for current file
				
				String term = charTermAttribute.toString();
				//check if key exists
				Integer count = InvIndex.freq.get(term);
				if (count == null){
					count = 0;}
				if(count_map.get(term) == null){
					count_map.put(term, 1.0);
				}else{
					Double appear = count_map.get(term);
					count_map.put(term,appear+1.0);
				}//end freq count if 
				InvIndex.freq.put(term, count+1); 
				
				
				if(InvIndex.inv_index.containsKey(term)){
					InvIndex.inv_index.get(term).add(f.getName()); //add to LinkedList
				}else{ //no key
					//add to map
					InvIndex.inv_index.put(term, new HashSet<String>());
					InvIndex.inv_index.get(term).add(f.getName());
				}//end inv_index if
		
				//update doc_terms
			}//end while tokenStream, per term
			InvIndex.doc_terms.put(doc_string,count_map);
			
			tokenStream.close();
			fread.close();
		
		}//end for
	
		//finish building inverted_index
		
		// calculate tf.idf for each term for each document
	
		// loop through each doc
		//loop throuch each word, go through inverted index
			for(Entry<String, Map<String, Double>> entry : InvIndex.doc_terms.entrySet()){// docs: word and tf_idf
				String doc_string = entry.getKey();
				//search the word in inverted index for ni
				Map <String,Double>word_idf = entry.getValue();
				for(Entry<String,Double> w_entry : word_idf.entrySet()){ //word and tf_idf
				Double ni = (double) InvIndex.inv_index.get(w_entry.getKey()).size();
				Double tf_idf = w_entry.getValue();
				Double N_ni = 3204/ni;
				tf_idf = tf_idf*Math.log(N_ni);
				w_entry.setValue(tf_idf);
		//change the tf.idf for the word
				}
			}//end for doc_terms
		
	
		System.out.println(InvIndex.doc_terms);
	}
	
	
	 private Map<String,Integer> getFrequency(Map<String,LinkedList<String>> index){
		 //iterate through each term in map and find # of times it appeared
		 Map<String ,Integer> Mappy = new HashMap <String,Integer>();
		 for(Entry<String, LinkedList<String>> entry: index.entrySet() ){
			 Mappy.put(entry.getKey(),entry.getValue().size());
		 }
		return Mappy;
	}
	 
	private Map<String,Integer> sortByAscComparator(Map<String,Integer> unsortMap) {
		 
			List list = new LinkedList(unsortMap.entrySet()); //entrySet() uses iterators
	 
			// sort list based on comparator
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				public int compare(Entry<String,Integer> o1, Entry<String,Integer> o2) {
					if (o1.getValue().equals(o2.getValue())) {
						return o2.getKey().compareTo(o1.getKey());
					}
					
					return o1.getValue().compareTo(o2.getValue());
				}
			});
	 
			// put sorted list into map again
	                //LinkedHashMap make sure order in which keys were inserted
			Map<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				sortedMap.put((String)entry.getKey(), (Integer)entry.getValue());
			}
			return sortedMap;
		}
		
		private static Map<String,Integer> sortByDescComparator(Map<String,Integer> unsortMap) {
			 
			List list = new LinkedList(unsortMap.entrySet()); //entrySet() uses iterators
	 
			// sort list based on comparator
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				public int compare(Entry<String,Integer> o1, Entry<String,Integer> o2) {
					if (o1.getValue().equals(o2.getValue())) {
						return o1.getKey().compareTo(o2.getKey());
					}
					
					return o2.getValue().compareTo(o1.getValue());
				}
			});
	 
			// put sorted list into map again
	                //LinkedHashMap make sure order in which keys were inserted
			Map<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
			for (Iterator it = list.iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				sortedMap.put((String)entry.getKey(), (Integer)entry.getValue());
			}
			return sortedMap;
		}
		
		//tokenize method?
//constructor lol
		public InvertIndex(Map<String, Integer> freq,
				Map<String, HashSet<String>> inv_index, Map<String, Map<String,Double>> doc_terms) {
		
			this.freq = freq;
			this.inv_index = inv_index;
			this.doc_terms = doc_terms;
	
		}
	 
	 
	 
	
	
}
