package ec.app.kernel_gp.parameters;


public class ERCa extends ERCParam {

	public ERCa(){
		min = -10.0;
		max = 10.0;
		step = 0.1;
		name = "a";
	}
		
	@Override
	protected double getDefValue() {
		return 1.0;
	}
}
