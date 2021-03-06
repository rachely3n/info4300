//import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.core.LowerCaseFilter;

public class GetZipf {
	//public static final Version LUCENE_40;
	static FileReader fread;
	static StandardTokenizer tok;
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{ //run errythang
		//get all documents
		String docsDirPath = "data/txt"; // directory containing documents
		//filereader extends inputstreamreader
		
		Map <String,Integer> freq = new HashMap<String,Integer>();
		File docDir = new File(docsDirPath);
		for (File f : docDir.listFiles()) { //read each file from directory
			fread = new FileReader(f);
			//text tokenization per file
			tok = new StandardTokenizer(Version.LUCENE_40,fread);
			//set max token length? 
			TokenStream tokenStream = new LowerCaseFilter(Version.LUCENE_40,tok); //gave tokenStream tokenizer
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			
			tokenStream.reset();
			while(tokenStream.incrementToken()){//while token exists for current file
				String term = charTermAttribute.toString();
				//System.out.println(term);
				//use the hashmap
				Integer count = freq.get(term);
				if(count == null) count = 0;
				freq.put(term,count+1);
				
			}//end while tokenStream
			tokenStream.close();
			fread.close();
		
		}//end for
		//need to sort freq.values();
		Map<String, Integer> sortedAscMap = sortByAscComparator(freq); //ascending order(alphabetically)
		Map<String, Integer> sortedDescMap = sortByDescComparator(freq); //descending order (alphabetically and rank)
		System.out.println(sortedDescMap);
		int i = 1; int rank = freq.size();
		//rank bottom 5
		for(Entry <String,Integer> entry : sortedDescMap.entrySet()){
			if (i==6) break;
			System.out.println(entry.getKey()+" : "+entry.getValue()+ " rank is: "+i);
			i = i+1;
			//rank = rank-1;
		}
		rank = freq.size(); int j =0;
		for(Entry <String,Integer> entry : sortedAscMap.entrySet()){
			if (j==5) break;
			System.out.println(entry.getKey()+" : "+entry.getValue()+ " rank is: "+rank);
			j = j+1;
			rank = rank-1;
		}
		
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
