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
public class ERC_RBF_gamma extends ERC {

	
	public double value;
	/* (non-Javadoc)
	 * @see ec.gp.ERC#resetNode(ec.EvolutionState, int)
	 */
	@Override
	public void resetNode(EvolutionState state, int thread) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#nodeEquals(ec.gp.GPNode)
	 */
	@Override
	public boolean nodeEquals(GPNode node) {
		// TODO Auto-generated method stub
		return (node.getClass() == this.getClass() && ((ERC_RBF_gamma)node).value == this.value);
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#encode()
	 */
	@Override
	public String encode() {
		// TODO Auto-generated method stub
		return Code.encode(value);
	}

	/* (non-Javadoc)
	 * @see ec.gp.GPNode#eval(ec.EvolutionState, int, ec.gp.GPData, ec.gp.ADFStack, ec.gp.GPIndividual, ec.Problem)
	 */
	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		DoubleData data = (DoubleData)input;
        data.val = value;
	}

	/* (non-Javadoc)
	 * @see ec.gp.ERC#mutateERC(ec.EvolutionState, int)
	 */
	@Override
	public void mutateERC(EvolutionState state, int thread) {
		double v;
		do
			v = value + state.random[thread].nextGaussian() * 0.1;
		while( v < 0.0 || v >= 2.0);
		value = v;
	}
	
	

}
