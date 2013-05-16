package libsvm;


import java.util.ArrayList;

import libsvm.Cache;
import libsvm.Kernel;
import libsvm.svm_node;
import libsvm.svm_parameter;

import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;

public class SVC_Q_GP extends Kernel {

	public EvolutionState state; 
	public Individual ind;
	public int threadnum;
	public Kernel_GP_problem problem;
	public GPData input;
	public ADFStack stack;
    
	private final byte[] y;
	private final Cache cache;
	private final double[] QD;

	SVC_Q_GP(svm_gp_problem prob, svm_parameter param, byte[] y_)
	{
		super(prob.l, prob.x, param);
		
		ind = prob.ind;
		state = null;
		stack = null;
		threadnum = 0;
		problem = null;
		input = prob.input;
		
		y = (byte[])y_.clone();
		cache = new Cache(prob.l,(long)(param.cache_size*(1<<20)));
		QD = new double[prob.l];
		for(int i=0;i<prob.l;i++)
			QD[i] = kernel_function(i,i);
	}

	float[] get_Q(int i, int len)
	{
		float[][] data = new float[1][];
		int start, j;
		//Exception when i == -1
		if((start = cache.get_data(i,data,len)) < len)
		{
			for(j=start;j<len;j++)
				data[0][j] = (float)(y[i]*y[j]*kernel_function(i,j));
		}
		return data[0];
	}

	double[] get_QD()
	{
		return QD;
	}

	void swap_index(int i, int j)
	{
		cache.swap_index(i,j);
		super.swap_index(i,j);
		do {byte _=y[i]; y[i]=y[j]; y[j]=_;} while(false);
		do {double _=QD[i]; QD[i]=QD[j]; QD[j]=_;} while(false);
	}
	
	
	public static double dot(svm_node[] x, svm_node[] y)
	{
		double sum = 0;
		int xlen = x.length;
		int ylen = y.length;
		int i = 0;
		int j = 0;
		while(i < xlen && j < ylen)
		{
			try{
				if(x[i].index == y[j].index)
					sum += x[i++].value * y[j++].value;
				else
				{
					if(x[i].index > y[j].index)
						++j;
					else
						++i;
				}
			}
			catch (Exception e){
				e.printStackTrace();
			}
		}
		return sum;
	}
	
	public static double powi(double base, int times)
	{
		double tmp = base, ret = 1.0;

		for(int t=times; t>0; t/=2)
		{
			if(t%2==1) ret*=tmp;
			tmp = tmp * tmp;
		}
		return ret;
	}
	
	/**
	 * Returns magnitude of given vector - ||X||
	 * @param x - vector of doubles
	 * @return Magnitude of the vector - ||X||
	 */
	public static double magnitude(svm_node[] x){		
		return Math.sqrt(dot(x, x));
	}
	
	
	public static svm_node[] vector_difference(svm_node[] x, svm_node[]y){
		int xlen = x.length;
		int ylen = y.length;
		int xi = 0;
		int yi = 0;
		int ri = 0;
		ArrayList<svm_node> result = new ArrayList<svm_node>();
		while(xi < xlen || yi < ylen)
		{
			if(xi < xlen && yi < ylen && x[xi].index == y[yi].index){
				result.add(ri, new svm_node());
				result.get(ri).index = x[xi].index;
				result.get(ri).value = x[xi++].value-y[yi++].value;
			}
			else
			{
				if (xi >= xlen){
					result.add(ri, new svm_node());
					result.get(ri).index = y[yi].index;
					result.get(ri).value = -y[yi++].value;
				}
				
				else if (yi >= ylen){
					result.add(ri, new svm_node());
					result.get(ri).index = x[xi].index;
					result.get(ri).value = x[xi++].value;
				}
				
				else if(x[xi].index > y[yi].index){
					result.add(ri, new svm_node());
					result.get(ri).index = y[yi].index;
					result.get(ri).value = -y[yi++].value;
				}
				else{
					result.add(ri, new svm_node());
					result.get(ri).index = x[xi].index;
					result.get(ri).value = x[xi++].value;
				}
			}
			ri++;
		}
		return result.toArray(new svm_node[result.size()]);
	}
	
	double kernel_function_gp(int i, int j)
	{
		((SVMData)input).X = x[i];
		((SVMData)input).Y = x[j];
		((SVMData)input).X2 = x_square[i];
		((SVMData)input).Y2 = x_square[j];
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
		SVMData data = (SVMData)input;
		
        return data.val;		
	}
	

	double kernel_function(int i, int j)
	{
		switch(getKernel_type())
		{
			case svm_parameter.LINEAR:
				return dot(x[i],x[j]);
			case svm_parameter.POLY:
				return powi(getGamma()*dot(x[i],x[j])+getCoef0(),getDegree());
			case svm_parameter.RBF:
				return Math.exp(-getGamma()*(x_square[i]+x_square[j]-2*dot(x[i],x[j])));
			case svm_parameter.SIGMOID:
				return Math.tanh(getGamma()*dot(x[i],x[j])+getCoef0());
			case svm_parameter.PRECOMPUTED:
				return x[i][(int)(x[j][0].value)].value;
			case svm_parameter.GPKERNEL:
				return kernel_function_gp(i, j);
			default:
				return 0;	// java
		}
	}
	
	
	static double k_function(svm_node[] x, svm_node[] y,
			svm_parameter param, svm_gp_problem prob)
	{					
		
		if (param.kernel_type != svm_parameter.GPKERNEL)
			return k_function(x, y, param);
		
		EvolutionState state = null;
		int threadnum = 0;
		Kernel_GP_problem problem = null;
		ADFStack stack = null;
		
		Individual ind = prob.ind;
		SVMData input = (SVMData) prob.input;
		input.X = x;
		input.Y = y;
		input.X2 = -1.0; //If there is RBF function it will compute squares on its own
		input.Y2 = -1.0;
		
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
		
		return input.val;
	}
	
	
	static double k_function(svm_node[] x, svm_node[] y,
			svm_parameter param)
	{
	switch(param.kernel_type)
		{
			case svm_parameter.LINEAR:
				return dot(x,y);
			case svm_parameter.POLY:
				return powi(param.gamma*dot(x,y)+param.coef0,param.degree);
			case svm_parameter.RBF:
			{
				double sum = 0;
				int xlen = x.length;
				int ylen = y.length;
				int i = 0;
				int j = 0;
				while(i < xlen && j < ylen)
				{
					if(x[i].index == y[j].index)
					{
						double d = x[i++].value - y[j++].value;
						sum += d*d;
					}
					else if(x[i].index > y[j].index)
					{
						sum += y[j].value * y[j].value;
						++j;
					}
					else
					{
						sum += x[i].value * x[i].value;
						++i;
					}
				}
		
				while(i < xlen)
				{
					sum += x[i].value * x[i].value;
					++i;
				}
		
				while(j < ylen)
				{
					sum += y[j].value * y[j].value;
					++j;
				}
		
				return Math.exp(-param.gamma*sum);
			}
			case svm_parameter.SIGMOID:
				return Math.tanh(param.gamma*dot(x,y)+param.coef0);
			case svm_parameter.PRECOMPUTED:
				return	x[(int)(y[0].value)].value;
			default:
				return 0;	// java
		}
	}
	
}



