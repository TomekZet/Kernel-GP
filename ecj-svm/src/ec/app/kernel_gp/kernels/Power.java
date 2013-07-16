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
 * Power kernel is also known as the (unrectified) triangular kernel.
 * It is an example of scale-invariant kernel (Sahbi and Fleuret, 2004) 
 * and is also only conditionally positive definite.
 * http://hal.archives-ouvertes.fr/docs/00/07/19/84/PDF/RR-4601.pdf
 * @author tomek
 *
 */
public class Power extends GPNode {

	@Override
	public String toString() {
		return "Power";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
		
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
		children[2].eval(state,thread,data,stack,individual,problem);
		double d = data.val;		
		
		data.val = - Math.pow(SVC_Q_GP.magnitude(SVC_Q_GP.vector_difference(x, y)), d);

	}

}
