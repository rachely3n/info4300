import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFilesMySimilarity {

	private SearchFilesMySimilarity() {}

	/** This function is only for test search. */
	public static List<String> searchQuery(String indexDir, String queryString, int numResults, CharArraySet stopwords) {
		String field = "contents";
		List<String> hitPaths = new ArrayList<String>();

		try {
			IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
			IndexSearcher searcher = new IndexSearcher(reader);
	
			//Used with mySimilarity.java for part E
			mySimilarity sim = new mySimilarity();
			searcher.setSimilarity(sim);
			
			//Used in part F
			//String field="Search_Field";
      			//the second parameter calls the getAvgLength method and automatically calculates the   Average Length value
      			//BM25Parameters.setAverageLength(field,getAvgLen(reader,field));
      			//BM25Parameters.setB(0.75f);
      			//BM25Parameters.setK1(2f);
      			//BM25BooleanQuery query = new BM25BooleanQuery( "Cystic hydroma" ,field,analyzer);
      			//System.out.println ("Searching for: " + query.toString(field));
      			//TopDocs top=searcher.search(query, 10);
      			//ScoreDoc[] docs = top.scoreDocs;
      			//for (int i = 0; i < 10; i++){
         			//System.out.println("the document with id= " + docs[i].doc + " has score ="+docs[i].score);
      			//}
			
			Analyzer analyzer = new MyAnalyzer(Version.LUCENE_44, stopwords);

			QueryParser parser = new QueryParser(Version.LUCENE_44, field, analyzer);
			Query query;
			query = parser.parse(QueryParser.escape(queryString));

			TopDocs results = searcher.search(query, null, numResults);
			for (ScoreDoc hit : results.scoreDocs) {
				String path = searcher.doc(hit.doc).get("path");
				hitPaths.add(path.substring(0, path.length()-4)); // chop off the file extension (".txt")
			}
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} catch (ParseException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

		return hitPaths;
	}
	
	/**public static float getAvgLen(IndexReader reader,String field) throws IOException{
      		int sum = 0;
      		for (int i = 0; i < reader.numDocs(); i++){
         		TermFreqVector tfv = reader.getTermFreqVector(i, field);
         		if(tfv != null) {
            			int[] tfs = tfv.getTermFrequencies();
        			for(int j = 0; j < tfv.size(); j++){
               				sum = sum + tfs[j];
            			}
        	 	}
      		}
      		float avg = (float) sum / reader.numDocs();
      		//System.out.println("average length = " + avg);
      		return avg;
   	} //end getAvgLen method
   	*/
}
