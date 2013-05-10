package libsvm;
//import libsvm.*;
import libsvm.Results;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;

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
	

	// Platt's binary SVM Probablistic Output: an improvement from Lin et al.
	public static void sigmoid_train(int l, double[] dec_values, double[] labels, 
				  double[] probAB)
	{
		double A, B;
		double prior1=0, prior0 = 0;
		int i;

		for (i=0;i<l;i++)
			if (labels[i] > 0) prior1+=1;
			else prior0+=1;
	
		int max_iter=100;	// Maximal number of iterations
		double min_step=1e-10;	// Minimal step taken in line search
		double sigma=1e-12;	// For numerically strict PD of Hessian
		double eps=1e-5;
		double hiTarget=(prior1+1.0)/(prior1+2.0);
		double loTarget=1/(prior0+2.0);
		double[] t= new double[l];
		double fApB,p,q,h11,h22,h21,g1,g2,det,dA,dB,gd,stepsize;
		double newA,newB,newf,d1,d2;
		int iter; 
	
		// Initial Point and Initial Fun Value
		A=0.0; B=Math.log((prior0+1.0)/(prior1+1.0));
		double fval = 0.0;

		for (i=0;i<l;i++)
		{
			if (labels[i]>0) t[i]=hiTarget;
			else t[i]=loTarget;
			fApB = dec_values[i]*A+B;
			if (fApB>=0)
				fval += t[i]*fApB + Math.log(1+Math.exp(-fApB));
			else
				fval += (t[i] - 1)*fApB +Math.log(1+Math.exp(fApB));
		}
		for (iter=0;iter<max_iter;iter++)
		{
			// Update Gradient and Hessian (use H' = H + sigma I)
			h11=sigma; // numerically ensures strict PD
			h22=sigma;
			h21=0.0;g1=0.0;g2=0.0;
			for (i=0;i<l;i++)
			{
				fApB = dec_values[i]*A+B;
				if (fApB >= 0)
				{
					p=Math.exp(-fApB)/(1.0+Math.exp(-fApB));
					q=1.0/(1.0+Math.exp(-fApB));
				}
				else
				{
					p=1.0/(1.0+Math.exp(fApB));
					q=Math.exp(fApB)/(1.0+Math.exp(fApB));
				}
				d2=p*q;
				h11+=dec_values[i]*dec_values[i]*d2;
				h22+=d2;
				h21+=dec_values[i]*d2;
				d1=t[i]-p;
				g1+=dec_values[i]*d1;
				g2+=d1;
			}

			// Stopping Criteria
			if (Math.abs(g1)<eps && Math.abs(g2)<eps)
				break;
			
			// Finding Newton direction: -inv(H') * g
			det=h11*h22-h21*h21;
			dA=-(h22*g1 - h21 * g2) / det;
			dB=-(-h21*g1+ h11 * g2) / det;
			gd=g1*dA+g2*dB;


			stepsize = 1;		// Line Search
			while (stepsize >= min_step)
			{
				newA = A + stepsize * dA;
				newB = B + stepsize * dB;

				// New function value
				newf = 0.0;
				for (i=0;i<l;i++)
				{
					fApB = dec_values[i]*newA+newB;
					if (fApB >= 0)
						newf += t[i]*fApB + Math.log(1+Math.exp(-fApB));
					else
						newf += (t[i] - 1)*fApB +Math.log(1+Math.exp(fApB));
				}
				// Check sufficient decrease
				if (newf<fval+0.0001*stepsize*gd)
				{
					A=newA;B=newB;fval=newf;
					break;
				}
				else
					stepsize = stepsize / 2.0;
			}
			
			if (stepsize < min_step)
			{
				svm.info("Line search fails in two-class probability estimates\n");
				break;
			}
		}
		
		if (iter>=max_iter)
			svm.info("Reaching maximal iterations in two-class probability estimates\n");
		probAB[0]=A;probAB[1]=B;
	}
	
	
	private static double sigmoid_predict(double decision_value, double A, double B)
	{
		double fApB = decision_value*A+B;
		if (fApB >= 0)
			return Math.exp(-fApB)/(1.0+Math.exp(-fApB));
		else
			return 1.0/(1+Math.exp(fApB)) ;
	}
	
	
	public static double svm_predict_probability(svm_model model, svm_node[] x, double[] prob_estimates, svm_gp_problem problem)
	{
		if ((model.param.svm_type == svm_parameter.C_SVC || model.param.svm_type == svm_parameter.NU_SVC) &&
		    model.probA!=null && model.probB!=null)
		{
			int i;
			int nr_class = model.nr_class;
			double[] dec_values = new double[nr_class*(nr_class-1)/2];
			svm_predict_values(model, x, dec_values, problem);

			double min_prob=1e-7;
			double[][] pairwise_prob=new double[nr_class][nr_class];
			
			int k=0;
			for(i=0;i<nr_class;i++)
				for(int j=i+1;j<nr_class;j++)
				{
					pairwise_prob[i][j]=Math.min(Math.max(sigmoid_predict(dec_values[k],model.probA[k],model.probB[k]),min_prob),1-min_prob);
					pairwise_prob[j][i]=1-pairwise_prob[i][j];
					k++;
				}
			multiclass_probability(nr_class,pairwise_prob,prob_estimates);

			int prob_max_idx = 0;
			for(i=1;i<nr_class;i++)
				if(prob_estimates[i] > prob_estimates[prob_max_idx])
					prob_max_idx = i;
			return model.label[prob_max_idx];
		}
		else 
			return svm_predict(model, x, problem);
	}
	

	
	// Method 2 from the multiclass_prob paper by Wu, Lin, and Weng
	private static void multiclass_probability(int k, double[][] r, double[] p)
	{
		int t,j;
		int iter = 0, max_iter=Math.max(100,k);
		double[][] Q=new double[k][k];
		double[] Qp=new double[k];
		double pQp, eps=0.005/k;
	
		for (t=0;t<k;t++)
		{
			p[t]=1.0/k;  // Valid if k = 1
			Q[t][t]=0;
			for (j=0;j<t;j++)
			{
				Q[t][t]+=r[j][t]*r[j][t];
				Q[t][j]=Q[j][t];
			}
			for (j=t+1;j<k;j++)
			{
				Q[t][t]+=r[j][t]*r[j][t];
				Q[t][j]=-r[j][t]*r[t][j];
			}
		}
		for (iter=0;iter<max_iter;iter++)
		{
			// stopping condition, recalculate QP,pQP for numerical accuracy
			pQp=0;
			for (t=0;t<k;t++)
			{
				Qp[t]=0;
				for (j=0;j<k;j++)
					Qp[t]+=Q[t][j]*p[j];
				pQp+=p[t]*Qp[t];
			}
			double max_error=0;
			for (t=0;t<k;t++)
			{
				double error=Math.abs(Qp[t]-pQp);
				if (error>max_error)
					max_error=error;
			}
			if (max_error<eps) break;
		
			for (t=0;t<k;t++)
			{
				double diff=(-Qp[t]+pQp)/Q[t][t];
				p[t]+=diff;
				pQp=(pQp+diff*(diff*Q[t][t]+2*Qp[t]))/(1+diff)/(1+diff);
				for (j=0;j<k;j++)
				{
					Qp[j]=(Qp[j]+diff*Q[t][j])/(1+diff);
					p[j]/=(1+diff);
				}
			}
		}
		if (iter>=max_iter)
			svm.info("Exceeds max_iter in multiclass_prob\n");
	}

	
	/**
	 * Predicts classes for all examples in problem and computes accuracy measures	 
	 * @param problem
	 * @param model
	 * @return Results object containing various accuracy measures
	 */
	public static Results predict_problem(svm_gp_problem problem, svm_model model)
	{
		int correct = 0;
		int predicted;
		int actual;
		int actualClassNr;
		
		double probabilitySum = 0.0;

		Results results = new Results(model.nr_class);

		for(int i=0;i<problem.y.length; i++)
		{
			actual = (int)(problem.y[i]);			
			initMapIfNeeded(results.counts, new Integer(actual));
		}
		
		HashMap<Integer, Integer> classes = new HashMap<Integer, Integer>();
		for(int i=0; i<model.nr_class; i++){
			classes.put(new Integer(model.label[i]), new Integer(i));
		}
		
		for(int i=0;i<problem.x.length; i++)
		{  				
			
			double [] prob_estimates = new double[model.nr_class];
			actual = (int)(problem.y[i]);
			Integer actualInt = new Integer(actual).intValue();
			actualClassNr = classes.get(actualInt);
						
			if(model.param.probability==1){
				predicted = (int) svm_predict_probability(model, problem.x[i], prob_estimates, problem);
				probabilitySum += prob_estimates[actualClassNr];
			}
			else{
				predicted = (int) svm_predict(model, problem.x[i], problem);
			}
						
			String result;
			
			if (actual == predicted)
				correct++;
			
			for (Integer klass : results.counts.keySet()){
				if (klass.intValue() == actual && klass.intValue() == predicted)
					result = "TP";
				else if (klass.intValue() == actual)
					result = "FN";				
				else if (klass.intValue() == predicted)
					result = "FP";
				else
					result = "TN";
				float _count = results.counts.get(klass).get(result).floatValue() + 1.0f;
				Float count =  new Float(_count);
				results.counts.get(klass).put(result, count);				
			}
		}

		results.accuracy = (float)correct / problem.x.length;
		results.meanProbability = probabilitySum / problem.x.length;
		results.meanf1 = 0.0f;
		results.meanMCC = 0.0f;
		float f1;
		float MCC;
		
		for (Integer klass : results.counts.keySet()){				
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
	private static void initMapIfNeeded(HashMap<Integer, HashMap<String, Float>> counts, Integer key){
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
	
	
	/**
	 * Predicts class for example given in x
	 * @param model
	 * @param x - example which class will be predicted
	 * @param problem - svm_gp problem needed to compute kernel generated by GP
	 * @return
	 */
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