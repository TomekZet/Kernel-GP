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
 * The exponential kernel is closely related to the Gaussian kernel,
 *  with only the square of the norm left out. 
 * It is also a radial basis function kernel.
 *  It is important to note that the observations made about the sigma 
 *  parameter for the Gaussian kernel also apply to the Expotential kernel.
 * @author tomek
 *
 */
public class Expotential extends GPNode {

	@Override
	public String toString() {
		return "Expotential";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {

		SVMData data = (SVMData)input;
		
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
      	children[0].eval(state,thread,data,stack,individual,problem);
		double gamma = data.val;	
		
		data.val = Math.exp(-gamma * SVC_Q_GP.magnitude(SVC_Q_GP.vector_difference(x, y)));

	}
}
