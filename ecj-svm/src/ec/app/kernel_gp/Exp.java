package ec.app.kernel_gp;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class Exp extends GPNode {

	@Override
	public void checkConstraints(EvolutionState state, int tree,
			GPIndividual typicalIndividual, Parameter individualBase) {
		// TODO Auto-generated method stub
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length!=1)
            state.output.error("Incorrect number of children for node " + 
                toStringForError() + " at " +
                individualBase);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
        
		children[0].eval(state,thread,input,stack,individual,problem);
        DoubleData data = (DoubleData)input;
		data.val = Math.exp(data.val);

	}

}
