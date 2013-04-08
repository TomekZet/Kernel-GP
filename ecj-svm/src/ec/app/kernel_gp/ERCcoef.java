package ec.app.kernel_gp;


public class ERCcoef extends ERCParam {
	
	public ERCcoef(){
		min = 0.1;
		max = 1.0;
		step = 0.1;
		name = "coef0";
	}
	
	@Override
	protected double getDefValue() {
		return Kernel_GP_problem.svm_params.coef0;
	}
	
	
}
