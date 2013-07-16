package ec.app.kernel_gp.parameters;

import ec.EvolutionState;
import ec.app.kernel_gp.Kernel_GP_problem;

public class ERCgamma extends ERCParam {

	public ERCgamma(){
		min = 0.1;
		max = 2;
		step = 0.05;
		name = "gamma";
	}
	
	@Override
	protected double getDefValue() {
		return Kernel_GP_problem.svm_params.gamma;
	}
	
}
