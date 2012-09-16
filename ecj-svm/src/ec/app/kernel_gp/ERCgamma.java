package ec.app.kernel_gp;

import ec.EvolutionState;

public class ERCgamma extends ERCParam {

	public ERCgamma(){
		min = 0.0;
		max = 2.0;
		step = 0.1;
		name = "gamma";
	}
	
	@Override
	protected double getDefValue() {
		return Kernel_GP_problem.svm_params.gamma;
	}
	
}
