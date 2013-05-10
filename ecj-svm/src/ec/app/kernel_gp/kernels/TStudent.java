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
/**
 * The Generalized T-Student Kernel has been proven to be a Mercel Kernel, 
 * thus having a positive semi-definite Kernel matrix (Boughorbel, 2004).
 *  It is given by: 1 / ( 1 + ||x - y||^d
 * @author tomek
 *
 */
public class TStudent extends GPNode {

	@Override
	public String toString() {
		return "TStudent";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
		
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
		children[0].eval(state,thread,data,stack,individual,problem);
		double d = data.val;		
		
		data.val = 1 / (1 + Math.pow(SVC_Q_GP.magnitude(SVC_Q_GP.vector_difference(x, y)), d));

	}

}
