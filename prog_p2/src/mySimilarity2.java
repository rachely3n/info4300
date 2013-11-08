//Secondary version used so we can have two different tf.idf scoring measures as the project asks for.
public class mySimilarity extends TFIDFSimilarity {
  public mySimilarity() {
    super();
  }
  
  public abstract float tf(float freq) {
    float val = log(freq + 1);
    return val;
  }
  
  public abstract float idf(long docFreq, long numDocs) {
    float val = log(numDocs / (docFreq + 1)) + 1;
    return val;
  }
}
