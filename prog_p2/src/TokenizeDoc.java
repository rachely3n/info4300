import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.core.LowerCaseFilter;
public class TokenizeDoc {
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
	}
}
