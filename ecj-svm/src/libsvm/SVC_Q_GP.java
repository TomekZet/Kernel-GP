package libsvm;


import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;

public class SVC_Q_GP extends SVC_Q {

	public static EvolutionState state; 
	public static Individual ind;
	public 	static int subpopulation;
	public static int threadnum;
	public static Kernel_GP_problem problem;
	public static GPData input;
	public static ADFStack stack;
    
	//private svm_node[][] x;
	//private double[] x_square;
	
	/**
	 * @param prob
	 * @param param
	 * @param y_
	 */
	public SVC_Q_GP(svm_problem prob, svm_parameter param, byte[] y_) {
		super(prob, param, y_);
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
		//We have to assign 
		problem.currentX = x;
		problem.currentY = y;
		
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
		SVMData data = (SVMData)input;
		
        return data.val;
	}
}



