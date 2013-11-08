import org.apache.lucene.search.similarities.TFIDFSimilarity;

public abstract class mySimilarity extends TFIDFSimilarity {
  public mySimilarity() {
    super();
  }
  
  public float tf(float freq) {
    float val = log(freq + 1);
    return val;
  }
  
  private float log(float f) {
	// TODO Auto-generated method stub
	return 0;
}

public float idf(long docFreq, long numDocs) {
    float val = log(numDocs / docFreq);
    return val;
  }
}
