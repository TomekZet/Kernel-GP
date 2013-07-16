package libsvm;

import libsvm.svm_problem;
import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.gp.ADFStack;
import ec.gp.GPData;

public class svm_gp_problem extends svm_problem implements Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public svm_gp_problem()
	{
		super();
	}
	
	public Individual ind;
	public EvolutionState state; 
	public 	int subpopulation;
	public int threadnum;
	public Kernel_GP_problem problem;
	public GPData input;
	public ADFStack stack;
	
	public svm_gp_problem semi_clone(){
		svm_gp_problem result = new svm_gp_problem();
		result.ind = (Individual) this.ind;
		result.input = (GPData) this.input.clone();
		result.problem = this.problem;
		result.threadnum = this.threadnum;
		result.subpopulation = this.subpopulation;
		result.state = this.state;
		
		return result;				
	}
}
