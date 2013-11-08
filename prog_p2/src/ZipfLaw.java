import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
 
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
 
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
 
public class ZipfLaw {
	public static void main(String[] args) throws IOException {
		Multiset multiset = HashMultiset.create();
		Token token = new Token();
		String[] STOP_WORDS = {};
		
		StandardAnalyzer analyzer = new StandardAnalyzer(STOP_WORDS);
 		TokenStream stream = analyzer.tokenStream("content", new FileReader(new File("data/txt/CACM-*.txt")));
		
		while ((token = stream.next(token)) != null){
			multiset.add(token.term());
		}
 
		ArrayList l = new ArrayList(multiset.entrySet());
		Comparator occurence_comparator = new Comparator() {
			public int compare(Multiset.Entry e1, Multiset.Entry e2) {
				return e2.getCount() - e1.getCount() ;
			}
		};
		Collections.sort(l,occurence_comparator);
 
		int rank = 1;
		for(Multiset.Entry e : l ){
			System.out.println(rank+"t"+e.getCount());
			rank++;
		}
	}
}