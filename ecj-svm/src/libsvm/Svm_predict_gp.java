package libsvm;
//import libsvm.*;
import java.io.*;
import java.util.*;

public class Svm_predict_gp {
	private static double atof(String s)
	{
		return Double.valueOf(s).doubleValue();
	}

	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}

	public static double predict(BufferedReader input, DataOutputStream output, svm_model model, int predict_probability) throws IOException
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
			v = svm_predict(model,x);
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
	
	public static double svm_predict(svm_model model, svm_node[] x)
	{
		int nr_class = model.nr_class;
		double[] dec_values;
		dec_values = new double[nr_class*(nr_class-1)/2];
		double pred_result = svm_predict_values(model, x, dec_values);
		return pred_result;
	}

	public static double svm_predict_values(svm_model model, svm_node[] x, double[] dec_values)
	{
		int i;

		int nr_class = model.nr_class;
		int l = model.l;
	
		double[] kvalue = new double[l];
		for(i=0;i<l;i++)
			kvalue[i] = SVC_Q_GP.k_function(x,model.SV[i],model.param);

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