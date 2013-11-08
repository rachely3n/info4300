public class mySimilarity extends TFIDFSimilarity {
  public abstract float tf(float freq) {
    float val = log(freq + 1);
    return val;
  }
  
  public abstract float idf(long docFreq, long numDocs) {
    float val = log(numDocs / docFreq);
    return val;
  }
}
