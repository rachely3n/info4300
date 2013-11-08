//NOT ACTUALLY USED ONLY KEPT FOR REFERENCE

public class myBM25{

   public static float getAvgLen(IndexReader reader,String field) throws IOException{
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
   }//end getAvgLen method

   public static void main (String args[]) throws IOException, ParseException{
      String index = "";
      IndexReader reader = null; 
      try {
         reader=IndexReader.open(index);
      }
      catch (CorruptIndexException e1) {e1.printStackTrace();}
      catch (IOException e1) {e1.printStackTrace();}
      String field="Search_Field";
      Searcher searcher = new IndexSearcher("index");
      Analyzer analyzer = new StandardAnalyzer();
      //the second parameter calls the getAvgLength method and automatically calculates the   Average Length value
      BM25Parameters.setAverageLength(field,getAvgLen(reader,field));
      BM25Parameters.setB(0.75f);
      BM25Parameters.setK1(2f);
      BM25BooleanQuery query = new BM25BooleanQuery( "Cystic hydroma" ,field,analyzer);
      //System.out.println ("Searching for: " + query.toString(field));
      TopDocs top=searcher.search(query, 10);
      ScoreDoc[] docs = top.scoreDocs;
      for (int i = 0; i < 10; i++){
         //System.out.println("the document with id= " + docs[i].doc + " has score ="+docs[i].score);
      } 
   }//end of main
}//end of class
