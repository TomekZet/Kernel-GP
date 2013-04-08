package libsvm;


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
		((SVMData)input).X = x[i];
		((SVMData)input).Y = x[j];
		((SVMData)input).X2 = x_square[i];
		((SVMData)input).Y2 = x_square[j];
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
		SVMData data = (SVMData)input;
		
        return data.val;
	}

	static double k_function(svm_node[] x, svm_node[] y,
			svm_parameter param, svm_gp_problem prob)
	{					
//		EvolutionState state = ((svm_gp_problem)prob).state;
//		int threadnum = ((svm_gp_problem)prob).threadnum;
//		Kernel_GP_problem problem = ((svm_gp_problem)prob).problem;
//		SVMData input = (SVMData) ((svm_gp_problem)prob).input;
//		ADFStack stack = ((svm_gp_problem)prob).stack;
		EvolutionState state = null;
		int threadnum = 0;
		Kernel_GP_problem problem = null;
		ADFStack stack = null;
		
		Individual ind = prob.ind;
		SVMData input = (SVMData) prob.input;
		input.X = x;
		input.Y = y;
		input.X2 = -1.0; //If there is RBF function it will compute squaes on its own
		input.Y2 = -1.0;
				
		((GPIndividual)ind).trees[0].child.eval(state,threadnum,input,stack,((GPIndividual)ind),problem);
		
        return input.val;
	}
}



