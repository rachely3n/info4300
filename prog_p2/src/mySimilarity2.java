import org.apache.lucene.search.similarities.TFIDFSimilarity;

//Secondary version used so we can have two different tf.idf scoring measures as the project asks for.
public abstract class mySimilarity2 extends TFIDFSimilarity {
  public mySimilarity2() {
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
    float val = log(numDocs / (docFreq + 1)) + 1;
    return val;
  }
}
