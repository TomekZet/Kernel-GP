package libsvm;

import static libsvm.Kernel.dot;
import libsvm.SVC_Q;
import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.DoubleData;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;

public class SVC_Q_GP extends SVC_Q {

	EvolutionState state; 
    Individual ind;
    int subpopulation;
    int threadnum;
    Kernel_GP_problem problem;
    GPData input;
    ADFStack stack;
    
	private svm_node[][] x;
	//private double[] x_square;
	
	/**
	 * @param prob
	 * @param param
	 * @param y_
	 */
	public SVC_Q_GP(svm_problem prob, svm_parameter param, byte[] y_,
			EvolutionState state, 
	        Individual ind, 
	        int subpopulation,
	        int threadnum,
	        Kernel_GP_problem problem,
	        GPData input,
	        ADFStack stack) {
		super(prob, param, y_);

		this.state = state;
		this.ind = ind;
		this.subpopulation = subpopulation;
		this.threadnum = threadnum;
		this.problem = problem;
		this.input = input;
		this.stack = stack;
				
/*		x_square = new double[l];
		for(int i=0;i<l;i++)
			x_square[i] = dot(x[i],x[i]);
*/			
		
	}

	
	private static double powi(double base, int times)
	{
		double tmp = base, ret = 1.0;

		for(int t=times; t>0; t/=2)
		{
			if(t%2==1) ret*=tmp;
			tmp = tmp * tmp;
		}
		return ret;
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
		return sum;
	}
	
	double kernel_function(int i, int j)
	{
	
        //TODO: continue here
		
		this.problem.currentX = x[i];
		this.problem.currentY = x[j];
		
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
        DoubleData data = (DoubleData)input;
		return data.val;
		
/*			switch(kernel_type)
		{
			case svm_parameter.LINEAR:
				return dot(x[i],x[j]);
			case svm_parameter.POLY:
				return powi(gamma*dot(x[i],x[j])+coef0,degree);
			case svm_parameter.RBF:
				return Math.exp(-gamma*(x_square[i]+x_square[j]-2*dot(x[i],x[j])));
			case svm_parameter.SIGMOID:
				return Math.tanh(gamma*dot(x[i],x[j])+coef0);
			case svm_parameter.PRECOMPUTED:
				return x[i][(int)(x[j][0].value)].value;
			default:
				return 0;	// java
		}*/

	}
		

	static double k_function(svm_node[] x, svm_node[] y,
			svm_parameter param)
	{		
		return kernel_function(x, y);
		
		/*switch(param.kernel_type)
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
		}*/
	}

}



