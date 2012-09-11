/**
 * 
 */
package ec.app.kernel_gp;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.ERC;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Code;

/**
 * @author Tomasz Ziętkiewicz
 *
 */
public class ERCParam extends ERC {
	
	public double gamma;	//libsvm parameter
	public double coef0;	//libsvm parameter
	public int degree;		//libsvm parameter
	public double a;		//a used in aMul function
	/* (non-Javadoc)
	 * @see ec.gp.ERC#resetNode(ec.EvolutionState, int)
	 */
	
	@Override
	public String toStringForHumans() {
		return String.format("gamma:%f; coef0:%f; degree:%d; a:%f", gamma, coef0, degree, a);
	}
	
	@Override
	public void resetNode(EvolutionState state, int thread) {
		gamma = Kernel_GP_problem.svm_param.gamma;
		coef0 = Kernel_GP_problem.svm_param.coef0;
		degree = Kernel_GP_problem.svm_param.degree;
		a = 1.0;
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#nodeEquals(ec.gp.GPNode)
	 */
	@Override
	public boolean nodeEquals(GPNode node) {
		// TODO Auto-generated method stub
		return (node.getClass() == this.getClass() 
				&& ((ERCParam)node).gamma == this.gamma
				&& ((ERCParam)node).coef0 == this.coef0
				&& ((ERCParam)node).degree == this.degree
				&& ((ERCParam)node).a == this.a);
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#encode()
	 */
	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return Code.encode(gamma);
	}

	/* (non-Javadoc)
	 * @see ec.gp.GPNode#eval(ec.EvolutionState, int, ec.gp.GPData, ec.gp.ADFStack, ec.gp.GPIndividual, ec.Problem)
	 */
	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		ERCData data = (ERCData)input;
        data.gamma = gamma;
        data.coef0 = coef0;
        data.degree = degree;
        data.a = a;
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#mutateERC(ec.EvolutionState, int)
	 */
	
	protected double mutate(EvolutionState state, int thread,
			double value, double min, double max, double step)
	{
		double v;
		do
			v = value + state.random[thread].nextGaussian() * step;
		while( v < 0.0 || v >= 2.0);
		return v;
	}
	
	@Override
	public void mutateERC(EvolutionState state, int thread) {
			gamma = mutate(state, thread, gamma, 0.0, 2.0, 0.01);
			coef0 = mutate(state, thread, coef0, 0.0, 2.0, 0.01);
			a = mutate(state, thread, a, -10.0, 10.0, 0.01);
			degree = (int)(mutate(state, thread, (double)degree, 1.0, 5.0, 1.0));		
	}
}
