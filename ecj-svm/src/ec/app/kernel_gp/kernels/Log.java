package ec.app.kernel_gp.kernels;

import libsvm.SVC_Q_GP;
import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Log extends GPNode {

	@Override
	public String toString() {
		return "Log";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
		
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
		children[0].eval(state,thread,data,stack,individual,problem);
		double d = data.val;		
		
		data.val = - Math.log(Math.pow(SVC_Q_GP.magnitude(SVC_Q_GP.vector_difference(x, y)), d) +1);

	}

}
