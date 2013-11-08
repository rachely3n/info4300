import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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

public class Engine {
	static Analyzer analyzer;
	static HashMap<String, HashSet<Integer>> reverseIndex;
	int n;
	
	public Engine() {
		reverseIndex = new HashMap<String, HashSet<Integer>>();
		n = 1;
	}
	
	public void checkZipf(String docsPath, String indexPath) throws IOException {
		// Check whether docsPath is valid
		if (docsPath == null || docsPath.isEmpty()) {
			System.err.println("Document directory cannot be null");
			System.exit(1);
		}

		// Check whether the directory is readable
		final File docDir = new File(docsPath);
		if (!docDir.exists() || !docDir.canRead()) {
			System.out.println("Document directory '" +docDir.getAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}
		
		CharArraySet stopwords = new CharArraySet(Version.LUCENE_44,0,false);
		analyzer = new MyAnalyzer(Version.LUCENE_44, stopwords);
		zipfHelper(docDir);
		
		String[] strings = new String[10];
		int[] integers = new int[10];
		HashSet<String> visited = new HashSet<String>();
		int sumOccurrences = 0;
		for(int i = 0; i < 5; i++) {
			int max = Integer.MIN_VALUE;
			String maxString = "";
			
			for(String u : reverseIndex.keySet()) {
				if(visited.contains(u)) {
					//Ignore
				} else {
					if(reverseIndex.get(u).size() > max) {
						max = reverseIndex.get(u).size();
						maxString = u;
					}
				}
				sumOccurrences = sumOccurrences + reverseIndex.get(u).size();
			}
			
			strings[i] = maxString;
			integers[i] = max;
			visited.add(maxString);
		}
		
		int k = 5;
		for(int j = reverseIndex.keySet().size(); j > reverseIndex.keySet().size() - 5; j--) {
			int min = Integer.MAX_VALUE;
			String minString = "";
			
			for(String u : reverseIndex.keySet()) {
				if(visited.contains(u)) {
					//Ignore
				} else {
					if(reverseIndex.get(u).size() < min) {
						min = reverseIndex.get(u).size();
						minString = u;
					}
				}
			}
			
			strings[k] = minString;
			integers[k] = min;
			visited.add(minString); k++;
		}
		
		System.out.println("The TOP Ranked Tokens:");
		for(int z = 0; z < 5; z++) {
			double c = (z+1) * ((double) integers[z] / (double) sumOccurrences);
			System.out.println((z+1) + ". (" + strings[z] + ", " + integers[z] + ") -> Product: " + c);
		}
		int y = 0;
		System.out.println("The BOTTOM Ranked Tokens:");
		for(int z = 5; z < 10; z++) {
			double c = (reverseIndex.keySet().size() - y) * ((double) integers[z] / (double) sumOccurrences);
			System.out.println((reverseIndex.keySet().size() - y) + ". (" + strings[z] + ", " + integers[z] + ") -> Product: " + c);
			y++;
		}
		System.out.println(sumOccurrences);
	}
	
	public void evaluate(String docsDir, String indexDir, String queryFile, String answerFile, int numResults, CharArraySet stopwords) {
		
	}
	
	private void zipfHelper(File file) throws IOException {
		if (file.canRead()) {
			if (file.isDirectory()) {
				String[] files = file.list();
				if (files != null) {
					for (int i = 0; i < files.length; i++) {
						zipfHelper(new File(file, files[i]));
						n++;
					}
				}
			} else {
				FileInputStream fis = null;
				
				try {
					fis = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
				}
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
				try {
					while(reader.ready()) {
						String line = reader.readLine();
						//TokenStream stream  = analyzer.tokenStream("content", new StringReader(line));

			            //while(stream.incrementToken()) {
			            	//String token = stream.getAttribute(CharTermAttribute.class).toString();
			            	//if(reverseIndex.containsKey(token)) {
			            	//	reverseIndex.get(token).add(n);
			            	//}
			            //}
						StringTokenizer tokenizer = new StringTokenizer(line);
						while(tokenizer.hasMoreElements()) {
							String token = tokenizer.nextToken();
							if(reverseIndex.containsKey(token)) {
								reverseIndex.get(token).add(n);
							} else {
								reverseIndex.put(token, new HashSet<Integer>());
								reverseIndex.get(token).add(n);
							}
						}
					}
				} catch(IOException e) {
	        		//
	        	}
			}
		}
	}
}
