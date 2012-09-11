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
        svm_node[] x = data.X;
      	svm_node[] y = data.Y;		   
		
		ERCData erc = new ERCData();
		
		children[1].eval(state,thread,erc,stack,individual,problem);
		double gamma = erc.gamma;
		
		children[2].eval(state,thread,erc,stack,individual,problem);
		double coef0 = erc.coef0;
		
		data.val = Math.tanh(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0);
	}

}
