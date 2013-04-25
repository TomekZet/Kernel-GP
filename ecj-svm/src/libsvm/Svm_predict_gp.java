package libsvm;
//import libsvm.*;
import libsvm.Results;
import java.io.*;
import java.util.*;
import java.lang.ArithmeticException;

public class Svm_predict_gp {
	private static double atof(String s)
	{
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}

	public static double predict(BufferedReader input, DataOutputStream output, svm_model model, int predict_probability, svm_gp_problem problem) throws IOException
	{
		int correct = 0;
		int total = 0;
		double error = 0;

		while(true)
		{
			String line = input.readLine();
			if(line == null) break;

			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

			double target = atof(st.nextToken());
			int m = st.countTokens()/2;
			svm_node[] x = new svm_node[m];
			for(int j=0;j<m;j++)
			{
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}

			double v;
			v = svm_predict(model,x, problem);
			//output.writeBytes(v+"; ");

			if(v == target)
				++correct;
			error += (v-target)*(v-target);
			++total;
		}
		double accuracy = (double)correct/total;
		/*output.writeBytes("\nAccuracy = "+accuracy*100+
			 "% ("+correct+"/"+total+") (classification)\n");*/
		return accuracy;
	}
	
	
	
	public static double predict_problem_accuracy(svm_gp_problem problem, svm_model model)
	{
		int correct = 0;
		int total = 0;

		for(int i=0;i<problem.x.length; i++)
		{
			double target = problem.y[i];
			double v;
			v = svm_predict(model, problem.x[i], problem);

			if(v == target)
				++correct;
			++total;
		}
		double accuracy = (double)correct/total;
		return accuracy;
	}
	
	
	public static Results predict_problem(
			svm_gp_problem problem,
			svm_model model)
	{
		int correct = 0;
		float predicted;
		float actual;
		Float actualD;
		Float predictedD;

		Results results = new Results(model.nr_class);

		for(int i=0;i<problem.y.length; i++)
		{
			actual = (float) problem.y[i];			
			initMapIfNeeded(results.counts, new Float(actual));
		}
		
		for(int i=0;i<problem.x.length; i++)
		{
			predicted = (float) svm_predict(model, problem.x[i], problem);
			actual = (float) problem.y[i];
			actualD = new Float(actual);
			predictedD = new Float(predicted);
						
			String result;
			
			if (actualD.equals(predictedD))
				correct++;
			
			for (Float klass : results.counts.keySet()){
				if (klass.equals(actualD) && klass.equals(predictedD))
					result = "TP";
				else if (klass.equals(actualD))
					result = "FN";				
				else if (klass.equals(predictedD))
					result = "FP";
				else
					result = "TN";
				float _count = results.counts.get(klass).get(result).floatValue() + 1.0f;
				Float count =  new Float(_count);
				results.counts.get(klass).put(result, count);				
			}
		}

		results.accuracy = (float)correct / problem.x.length;				
		results.meanf1 = 0.0f;
		results.meanMCC = 0.0f;
		float f1;
		float MCC;
		
		for (Float klass : results.counts.keySet()){				
			f1 = F1(results.counts.get(klass));
			MCC = MCC(results.counts.get(klass));
			results.counts.get(klass).put("F1", f1);
			results.counts.get(klass).put("MCC", MCC);			
			results.meanf1+=f1;
			results.meanMCC+=MCC;
		}
		results.meanMCC /= results.counts.size();
		results.meanf1 /= results.counts.size();		
		
		return results;
	}
	
	/***
	 * If Map "counts" doesn't conatain entry with key "key"
	 *  this key is initialized with 0's and added to the map
	 * @param counts
	 * @param key
	 */
	private static void initMapIfNeeded(HashMap<Float, HashMap<String, Float>> counts, Float key){
		if (!counts.containsKey(key)){
			HashMap rates = new HashMap<String, Float>(4);
			rates.put("TP", 0.0f);
			rates.put("TN", 0.0f);
			rates.put("FP", 0.0f);
			rates.put("FN", 0.0f);				
			counts.put(key, rates);
		}
	}
	
	/***
	 * Returns F1 given Map with TP, FP and FN values
	 * @param rates
	 * @return
	 */
	private static float F1(Map<String, Float> rates){
		return F1(rates.get("TP").intValue(), rates.get("FP").intValue(), rates.get("FN").intValue());
	}
	
	/***
	 * Returns F1 measure computed from true-positive(TP),
	 *  false-positive(FP) and false-negative(FN) rates
	 * @param TP true-positive rate
	 * @param FP false-positive rate
	 * @param FN false-positive rate
	 * @return F1 measure
	 */
	private static float F1(int TP, int FP, int FN){		
		return 2.0f*TP/(2*TP+FN+FP);
	}
	
	/***
	 * Computes F1-measure computed from precision and recall
	 * @param precision
	 * @param recall
	 * @return F1 measure
	 */
	private static float F1(float precision, float recall){
		return 2.0f*precision*recall/(precision+recall);
	}
	
	/**
	 * Returns classification precision given true positive (TP) and false positive (FP) rates  
	 * @param TP - number of true positive (TP) classified examples
	 * @param FP - number of false positive (FP) classified examples
	 * @return precision of classification
	 */
	private static float precision(int TP, int FP){
		return (float)TP/(TP+FP);
	}
	
	/***
	 * Returns classification precision given true positive (TP) and false negative (FN) rates  
	 * @param TP - number of true positive (TP) classified examples
	 * @param FP - number of false positive (FN) classified examples
	 * @return precision of classification
	 */
	private static float recall(int TP, int FN){
		return (float)TP/(TP+FN);
	}
	
	/***
	 * Returns Matthews correlation coefficient given a map with TP, TN, FP and FN rates
	 * @param rates
	 * @return
	 */
	private static float MCC(Map<String,  Float> rates){
		return MCC(rates.get("TP").intValue(), rates.get("TN").intValue(), 
				rates.get("FP").intValue(), rates.get("FN").intValue());
	}
	
	/***
	 * Returns Matthews correlation coefficient given TP, TN, FP and FN 
	 * @param TP - number of examples classified true positively
	 * @param TN - number of examples classified true negatively
	 * @param FP - number of examples classified false positively
	 * @param FN - number of examples classified false negatively
	 * @return Matthews correlation coefficient
	 */
	private static float MCC(int TP, int TN, int FP, int FN){
		float result = 0.0f;
		try{
			double denominator = Math.sqrt((TP+FP)*(TP+FN)*(TN+FP)*(TN+FN));
			if (denominator == 0)
				return 0.0f;
			result = (float) (((TP*TN)-(FP*FN))/denominator);
		}
		catch(ArithmeticException e){
			result = 0.0f;
		}
		return result;
	}
	
	
	
	public static double svm_predict(svm_model model, svm_node[] x, svm_gp_problem problem)
	{
		int nr_class = model.nr_class;
		double[] dec_values;
		dec_values = new double[nr_class*(nr_class-1)/2];
		double pred_result = svm_predict_values(model, x, dec_values, problem);
		return pred_result;
	}

	public static double svm_predict_values(svm_model model, svm_node[] x, double[] dec_values, svm_gp_problem problem)
	{
		int i;

		int nr_class = model.nr_class;
		int l = model.l;
	
		double[] kvalue = new double[l];
		for(i=0;i<l;i++)
			kvalue[i] = SVC_Q_GP.k_function(x,model.SV[i],model.param, problem);

		int[] start = new int[nr_class];
		start[0] = 0;
		for(i=1;i<nr_class;i++)
			start[i] = start[i-1]+model.nSV[i-1];

		int[] vote = new int[nr_class];
		for(i=0;i<nr_class;i++)
			vote[i] = 0;

		int p=0;
		for(i=0;i<nr_class;i++)
			for(int j=i+1;j<nr_class;j++)
			{
				double sum = 0;
				int si = start[i];
				int sj = start[j];
				int ci = model.nSV[i];
				int cj = model.nSV[j];
			
				int k;
				double[] coef1 = model.sv_coef[j-1];
				double[] coef2 = model.sv_coef[i];
				for(k=0;k<ci;k++)
					sum += coef1[si+k] * kvalue[si+k];
				for(k=0;k<cj;k++)
					sum += coef2[sj+k] * kvalue[sj+k];
				sum -= model.rho[p];
				dec_values[p] = sum;					

				if(dec_values[p] > 0)
					++vote[i];
				else
					++vote[j];
				p++;
			}

		int vote_max_idx = 0;
		for(i=1;i<nr_class;i++)
			if(vote[i] > vote[vote_max_idx])
				vote_max_idx = i;

		return model.label[vote_max_idx];
	}
}