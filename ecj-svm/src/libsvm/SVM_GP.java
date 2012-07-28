/**
 * 
 */
package libsvm;

import ec.EvolutionState;
import ec.Individual;
import ec.app.kernel_gp.Kernel_GP_problem;
import ec.gp.ADFStack;
import ec.gp.GPData;


/**
 * @author Tomek
 *
 */

public class SVM_GP extends svm {
	
	static EvolutionState state; 
    static Individual ind;
    static int subpopulation;
    static int threadnum;
    static Kernel_GP_problem problem;
    static GPData input;
    static ADFStack stack;
	
	private static void solve_c_svc(svm_problem prob, svm_parameter param,
			double[] alpha, Solver.SolutionInfo si,
			double Cp, double Cn)
	{
		int l = prob.l;
		double[] minus_ones = new double[l];
		byte[] y = new byte[l];
		
		int i;
		
		for(i=0;i<l;i++)
		{
			alpha[i] = 0;
			minus_ones[i] = -1;
			if(prob.y[i] > 0) y[i] = +1; else y[i] = -1;
		}
		
		Solver s = new Solver();
		s.Solve(l, 
				new SVC_Q_GP(prob,param,y, state, 
		         ind,  subpopulation,  threadnum,
		         problem,  input,  stack),
		        minus_ones, y,
			alpha, Cp, Cn, param.eps, si, param.shrinking);
		
		double sum_alpha=0;
		for(i=0;i<l;i++)
			sum_alpha += alpha[i];
		
		if (Cp==Cn)
			svm.info("nu = "+sum_alpha/(Cp*prob.l)+"\n");
		
		for(i=0;i<l;i++)
			alpha[i] *= y[i];
	}
	
	public static EvolutionState getState() {
		return state;
	}

	public static void setState(EvolutionState state) {
		SVM_GP.state = state;
	}

	public static Individual getInd() {
		return ind;
	}

	public static void setInd(Individual ind) {
		SVM_GP.ind = ind;
	}

	public static int getSubpopulation() {
		return subpopulation;
	}

	public static void setSubpopulation(int subpopulation) {
		SVM_GP.subpopulation = subpopulation;
	}

	public static int getThreadnum() {
		return threadnum;
	}

	public static void setThreadnum(int threadnum) {
		SVM_GP.threadnum = threadnum;
	}

	public static Kernel_GP_problem getProblem() {
		return problem;
	}

	public static void setProblem(Kernel_GP_problem problem) {
		SVM_GP.problem = problem;
	}

	public static GPData getInput() {
		return input;
	}

	public static void setInput(GPData input) {
		SVM_GP.input = input;
	}

	public static ADFStack getStack() {
		return stack;
	}

	public static void setStack(ADFStack stack) {
		SVM_GP.stack = stack;
	}
	
}
