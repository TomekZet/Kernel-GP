/**
 * 
 */
package libsvm;

import java.util.HashMap;

/**
 * @author tomek
 *
 */
public class Results {
	public Float accuracy;
	public Float meanf1;
	public Float meanMCC;
	public Double meanProbability;
	public HashMap<Integer, HashMap<String, Float>> counts;
    
    public Results(int nr_classes){
    	accuracy = new Float(0.0f);
    	meanf1 = new Float(0.0f);
    	meanMCC = new Float(0.0f);
    	meanProbability = new Double(0.0);
    	counts = new HashMap<Integer, HashMap<String, Float>>(nr_classes);
    }
    
    public Results(){
    	this(0);
    }

	/**
	 * @return the accuracy
	 */
	public Float getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(Float accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * @return the meanProbability
	 */
	public Double getMeanProbability() {
		return meanProbability;
	}

	/**
	 * @param meanProbability the meanProbability to set
	 */
	public void setMeanProbability(Double meanProbability) {
		this.meanProbability = meanProbability;
	}

	/**
	 * @return the meanf1
	 */
	public Float getMeanf1() {
		return meanf1;
	}

	/**
	 * @param meanf1 the meanf1 to set
	 */
	public void setMeanf1(Float meanf1) {
		this.meanf1 = meanf1;
	}

	/**
	 * @return the meanMCC
	 */
	public Float getMeanMCC() {
		return meanMCC;
	}

	/**
	 * @param meanMCC the meanMCC to set
	 */
	public void setMeanMCC(Float meanMCC) {
		this.meanMCC = meanMCC;
	}

	/**
	 * @return the counts
	 */
	public HashMap<Integer, HashMap<String, Float>> getCounts() {
		return counts;
	}

	/**
	 * @param counts the counts to set
	 */
	public void setCounts(HashMap<Integer, HashMap<String, Float>> counts) {
		this.counts = counts;
	}
}
