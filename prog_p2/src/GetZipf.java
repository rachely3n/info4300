import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;


public class GetZipf {
	public static void main(String[] args){ //run errythang
		
	}
//get all documents

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
