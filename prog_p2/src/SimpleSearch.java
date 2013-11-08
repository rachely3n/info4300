import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SimpleSearch {
	public static void main(String[] args) throws IOException {
		String docsDir = "Z:\\Misc\\General Programs\\Eclipse\\Eclipse Workspace\\CS 4300 Project II\\data\\txt"; // directory containing documents
		String indexDir = "Z:\\Misc\\General Programs\\Eclipse\\Eclipse Workspace\\CS 4300 Project II\\data\\index"; // the directory where index is written into
		String queryFile = "Z:\\Misc\\General Programs\\Eclipse\\Eclipse Workspace\\CS 4300 Project II\\data\\cacm_processed.query";    // query file
		String answerFile = "Z:\\Misc\\General Programs\\Eclipse\\Eclipse Workspace\\CS 4300 Project II\\data\\cacm_processed.rel";   // relevance judgements file

		int numResults = 5;
		CharArraySet luceneStopwords = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
		Engine engine = new Engine();
		
		engine.checkZipf(docsDir, indexDir);
		engine.evaluate(docsDir, indexDir, queryFile, answerFile, numResults, luceneStopwords);
		System.out.println("Done!");
	}
}