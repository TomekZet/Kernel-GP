package libsvm;

import libsvm.svm_problem;
import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.gp.ADFStack;
import ec.gp.GPData;

public class svm_gp_problem extends svm_problem
{
	public svm_gp_problem()
	{
		super();
	}
	
	public Individual ind;
//	public EvolutionState state; 
//	public 	int subpopulation;
//	public int threadnum;
//	public Kernel_GP_problem problem;
	public GPData input;
//	public ADFStack stack;
}
