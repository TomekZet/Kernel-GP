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

		DoubleData erc = (DoubleData)input;
		
		children[2].eval(state,thread,input,stack,individual,problem);
		double gamma = erc.val;
		
		DoubleData doubleData = (DoubleData)input;
				
		//return Math.exp(-gamma*(x_square[i]+x_square[j]-2*dot(x[i],x[j])));
		doubleData.val =	Math.exp(-gamma*(libsvm.SVC_Q_GP.dot(x,x) + libsvm.SVC_Q_GP.dot(y,y) -2*libsvm.SVC_Q_GP.dot(x,y)));

	}
	


}
