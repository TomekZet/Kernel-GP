package ec.app.kernel_gp.kernels;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Sigmoid extends GPNode {

	@Override
	public String toString() {
		return "Sigm";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
		
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
		children[0].eval(state,thread,data,stack,individual,problem);
		double gamma = data.val;
		
		children[1].eval(state,thread,data,stack,individual,problem);
		double coef0 = data.val;
		
		data.val = Math.tanh(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0);
	}

}
