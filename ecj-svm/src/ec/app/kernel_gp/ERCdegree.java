package ec.app.kernel_gp;


public class ERCdegree extends ERCParam {

	public ERCdegree(){
		min = 1.0;
		max = 10.0;
		step = 0.5;
		name = "degree";
	}

	@Override
	protected double getDefValue() {
		return Kernel_GP_problem.svm_params.degree;
	}
	
}
