package ec.app.kernel_gp;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Sigmoid extends GPNode {

	@Override
	public String toString() {
		return "Sigmoid";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {
		SVMData data = (SVMData)input;
		
		children[0].eval(state,thread,input,stack,individual,problem);
        svm_node[] x = data.svm_val;
        
		children[1].eval(state,thread,input,stack,individual,problem);
		svm_node[] y = data.svm_val;		   
		
//		SVMData erc = (SVMData)input;
//		
//		children[2].eval(state,thread,input,stack,individual,problem);
//		double gamma = erc.val;
		
		double gamma = ((Kernel_GP_problem)problem).svm_param.gamma;
		double coef0 = ((Kernel_GP_problem)problem).svm_param.coef0;
		
		SVMData SVMData = (SVMData)input;
				
		SVMData.val = Math.tanh(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0);
	}

}
