package libsvm;

import static libsvm.Kernel.dot;
import libsvm.SVC_Q;
import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.SVMNodeData;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.app.kernel_gp.DoubleData;

public class SVC_Q_GP extends SVC_Q {

	static EvolutionState state; 
	static Individual ind;
	static int subpopulation;
	static int threadnum;
	static Kernel_GP_problem problem;
	static GPData input;
	static ADFStack stack;
    
	private svm_node[][] x;
	//private double[] x_square;
	
	/**
	 * @param prob
	 * @param param
	 * @param y_
	 */
	public SVC_Q_GP(svm_problem prob, svm_parameter param, byte[] y_) {
		super(prob, param, y_);

				
/*		x_square = new double[l];
		for(int i=0;i<l;i++)
			x_square[i] = dot(x[i],x[i]);
*/					
	}

	
	private static double powi(double base, int times)
	{
		double tmp = base;
		double ret = 1.0;

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
		return SVC_Q_GP.k_function(x[i], x[j], new svm_parameter());
	}
		

	static double k_function(svm_node[] x, svm_node[] y,
			svm_parameter param)
	{		
		
		SVC_Q_GP.problem.currentX = x;
		SVC_Q_GP.problem.currentY = y;
		
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
        DoubleData data = (DoubleData)input;
		
        return data.val;
		
	}

}



