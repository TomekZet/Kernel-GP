package ec.app.kernel_gp;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class RBF extends GPNode {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		// TODO Auto-generated method stub

		SVMData data = (SVMData)input;
		
		children[0].eval(state,thread,input,stack,individual,problem);
        svm_node[] x = data.val;
        
		children[1].eval(state,thread,input,stack,individual,problem);
		svm_node[] y = data.val;		   

		DoubleData doubleData = (DoubleData)input;
		
		doubleData.val =	Math.exp(-gamma*(x*x + y*y -2*libsvm.SVC_Q_GP.dot(x,y)));

	}
	


}
