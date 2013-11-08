//import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.util.*;
import java.io.*;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.core.LowerCaseFilter;

public class GetZipf {
	//public static final Version LUCENE_40;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException{ //run errythang
		//get all documents
		String docsDirPath = "data/txt"; // directory containing documents
		//filereader extends inputstreamreader
		FileReader fread;
		StandardTokenizer tok;
		File docDir = new File(docsDirPath);
		for (File f : docDir.listFiles()) { //read each file from directory
			fread = new FileReader(f);
			//text tokenization
			//what is Your_versioN? @.@ lol
			tok = new StandardTokenizer(Version.LUCENE_40,fread);
			//set max token length? 
			TokenStream tokenStream = new LowerCaseFilter(Version.LUCENE_40,tok);
			CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
			
			tokenStream.reset();
			while(tokenStream.incrementToken()){//while token exists
				String term = charTermAttribute.toString();
			}
			fread.close();
			
		}
	}


//tokenize all documents
	 
	/**
	 *
	 *  
StandardTokenizer src = new StandardTokenizer(YOUR_VERSION, YOUR_FILE_READER);
src.setMaxTokenLength(YOUR_MAX_LENGTH);
TokenStream tokenStream = new StandardFilter(Your_VERSION, src);
CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

tokenStream.reset();
while (tokenStream.incrementToken()) {
   String term = charTermAttribute.toString(); // here you get the terms
}
	 */

	
	//don't filter stop words
	//retrieve frequencies for each word
	
	
}
