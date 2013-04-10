package ec.app.kernel_gp.operators;

import ec.EvolutionState;
import ec.Problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class Exp extends GPNode {

	@Override
	public void checkConstraints(EvolutionState state, int tree,
			GPIndividual typicalIndividual, Parameter individualBase) {
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        if (children.length!=1)
            state.output.error("Incorrect number of children for node " + 
                toStringForError() + " at " +
                individualBase);
	}

	@Override
	public String toString() {
		return "Exp";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
        
        SVMData rd = ((SVMData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
		rd.val = Math.exp(rd.val);

	}

}
