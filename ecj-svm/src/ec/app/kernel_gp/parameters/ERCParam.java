/**
 * 
 */
package ec.app.kernel_gp.parameters;

import ec.EvolutionState;
import ec.Problem;
import ec.app.kernel_gp.SVMData;
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
public abstract class ERCParam extends ERC {
	
	double min;
	double max;
	double step;
	public double val;
	String name;
	/* (non-Javadoc)
	 * @see ec.gp.ERC#resetNode(ec.EvolutionState, int)
	 */
	
	@Override
	public String toStringForHumans() {
		return String.format("%s=%f",name, val);
	}
	
	@Override
	public void resetNode(EvolutionState state, int thread) {
		val = getDefValue();
		mutateERC(state, thread);
	}
	
	abstract protected double getDefValue();

	/* (non-Javadoc)
	 * @see ec.gp.ERC#nodeEquals(ec.gp.GPNode)
	 */
	@Override
	public boolean nodeEquals(GPNode node) {
		return (node.getClass() == this.getClass() 
				&& ((ERCParam)node).val == this.val);
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#encode()
	 */
	@Override
	public String encode() {
		return Code.encode(val);
	}

	/* (non-Javadoc)
	 * @see ec.gp.GPNode#eval(ec.EvolutionState, int, ec.gp.GPData, ec.gp.ADFStack, ec.gp.GPIndividual, ec.Problem)
	 */
	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
        data.val = val;
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#mutateERC(ec.EvolutionState, int)
	 */
	
	public void mutateERC(EvolutionState state, int thread)
	{
		double v;
		do
			v = val + state.random[thread].nextGaussian() * step;
		while( v < min || v >= max);
		val = v;
	}
	

}
