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
	static FileReader fread;
	static StandardTokenizer tok;
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{ //run errythang
		//get all documents
		String docsDirPath = "data/txt"; // directory containing documents
		//filereader extends inputstreamreader
		//create inverted index
		Map <String,LinkedList<String>> freq_index = new HashMap<String,LinkedList<String>>();
		Map <String,HashSet<String>> inv_index = new HashMap<String,HashSet<String>>();
		File docDir = new File(docsDirPath);
		for (File f : docDir.listFiles()) { //read each file from directory
			fread = new FileReader(f);
			//text tokenization per file
			tok = new StandardTokenizer(Version.LUCENE_40,fread);
			//set max token length? 
			TokenStream tokenStream = new LowerCaseFilter(Version.LUCENE_40,tok); //gave tokenStream tokenizer
			tokenStream = new StopFilter(Version.LUCENE_40,tokenStream,StandardAnalyzer.STOP_WORDS_SET);
			
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			
			tokenStream.reset();
			while(tokenStream.incrementToken()){//while token exists for current file
				String term = charTermAttribute.toString();
				//System.out.println(term);
				//use the hashmap
				
				//check if key exists
				if(inv_index.containsKey(term)){
					inv_index.get(term).add(f.getName());
					freq_index.get(term).add(f.getName());
				}else{ //no key
					//add to map
				
					inv_index.put(term, new HashSet<String>());
					inv_index.get(term).add(f.getName());
					freq_index.put(term, new LinkedList<String>());
					freq_index.get(term).add(f.getName());
				}
			}//end while tokenStream
			tokenStream.close();
			fread.close();
		
		}//end for
		//System.out.println(inv_index);
		//Map<String,Integer> freq_Map = getFrequency(freq_index);
		Map <String, Integer> freq_Map = sortByDescComparator(getFrequency(freq_index));
		System.out.println(inv_index+"\n"+freq_Map);
	}
	
	 private static Map<String,Integer> getFrequency(Map<String,LinkedList<String>> index){
		 //iterate through each term in map and find # of times it appeared
		 Map<String ,Integer> Mappy = new HashMap <String,Integer>();
		 for(Entry<String, LinkedList<String>> entry: index.entrySet() ){
			 Mappy.put(entry.getKey(),entry.getValue().size());
		 }
		return Mappy;
	}
	 
	private static Map<String,Integer> sortByAscComparator(Map<String,Integer> unsortMap) {
		 
			List list = new LinkedList(unsortMap.entrySet()); //entrySet() uses iterators
	 
			// sort list based on comparator
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				@Override
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
	 
	 
	 
	
	
}
