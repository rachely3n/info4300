import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class EvaluateQueries {
	public static void main(String[] args) {
		String docsDir = ""; // directory containing documents
		String indexDir = ""; // the directory where index is written into

		String queryFile = "";    // query file
		String answerFile = "";   // relevance judgements file

		int numResults = 5;

		CharArraySet stopwords = new CharArraySet(Version.LUCENE_44,0,false);
		System.out.println(evaluate(indexDir, docsDir, queryFile,
				answerFile, numResults, stopwords));

	}

	private static Map<Integer, String> loadQueries(String filename) {
		HashMap<Integer, String> queryIdMap = new HashMap<Integer, String>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(
					new File(filename)));
		} catch (FileNotFoundException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		}

		String line;
		try {
			while ((line = in.readLine()) != null) {
				int pos = line.indexOf(',');
				queryIdMap.put(Integer.parseInt(line.substring(0, pos)), line
						.substring(pos + 1));
			}
		} catch(IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch(IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		}
		return queryIdMap;
	}

	private static Map<Integer, HashSet<String>> loadAnswers(String filename) {
		HashMap<Integer, HashSet<String>> queryAnswerMap = new HashMap<Integer, HashSet<String>>();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(
					new File(filename)));

			String line;
			while ((line = in.readLine()) != null) {
				String[] parts = line.split(" ");
				HashSet<String> answers = new HashSet<String>();
				for (int i = 1; i < parts.length; i++) {
					answers.add(parts[i]);
				}
				queryAnswerMap.put(Integer.parseInt(parts[0]), answers);
			}
		} catch(IOException e) {
			System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
		} finally {
			try {
				in.close();
			} catch(IOException e) {
				System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
			}
		}
		return queryAnswerMap;
	}

	private static double precision(HashSet<String> answers,
			List<String> results) {
		double matches = 0;
		for (String result : results) {
			if (answers.contains(result))
				matches++;
		}

		return matches / results.size();
	}

	private static double evaluate(String indexDir, String docsDir,
			String queryFile, String answerFile, int numResults,
			CharArraySet stopwords) {

		// Build Index
		IndexFiles.buildIndex(indexDir, docsDir, stopwords);

		// load queries and answer
		Map<Integer, String> queries = loadQueries(queryFile);
		Map<Integer, HashSet<String>> queryAnswers = loadAnswers(answerFile);

		// Search and evaluate
		double sum = 0;
		for (Integer i : queries.keySet()) {
			List<String> results = SearchFiles.searchQuery(indexDir, queries
					.get(i), numResults, stopwords);
			sum += precision(queryAnswers.get(i), results);
		}

		return sum / queries.size();
	}
}
