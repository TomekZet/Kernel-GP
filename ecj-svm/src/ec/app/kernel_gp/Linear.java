package ec.app.kernel_gp;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Linear extends GPNode {

	@Override
	public String toString() {
		return "Linear";
	}

	@Override
	public void eval(EvolutionState state,
			int thread,
			GPData input,
			ADFStack stack,
			GPIndividual individual,
			Problem problem) 
	{
		SVMData data = (SVMData)input;
		
		children[0].eval(state,thread,input,stack,individual,problem);
        svm_node[] x = data.svm_val;
        
		children[1].eval(state,thread,input,stack,individual,problem);
		svm_node[] y = data.svm_val;		   
					
		data.val = libsvm.SVC_Q_GP.dot(x,y);
	}
	
}
