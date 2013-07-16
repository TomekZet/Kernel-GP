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

public class Poly extends GPNode {

	@Override
	public String toString() {
		return "Poly";
	}

	@Override
	public void eval(EvolutionState state,
			int thread,
			GPData input,
			ADFStack stack,
			GPIndividual individual,
			Problem problem) 
	{

/*        double result1;
        double result2;
        SVMData data = ((SVMData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
        result1 = data.val;

        children[1].eval(state,thread,input,stack,individual,problem);
        result2 = data.val;
        
        data.val = powi(gamma*libsvm.SVC_Q_GP.dot(result1,result2)+coef0,degree);
        */
        
        
		SVMData data = (SVMData)input;
		//Eval selection node
		children[0].eval(state,thread,data,stack,individual,problem);
		
        svm_node[] x = data.X;
		svm_node[] y = data.Y;		   
		
		
		children[1].eval(state,thread,data,stack,individual,problem);
		double gamma = data.val;

		children[2].eval(state,thread,data,stack,individual,problem);
		double coef0 = data.val;
		
		children[3].eval(state,thread,data,stack,individual,problem);
		int degree = (int)data.val;
			
		data.val = SVC_Q_GP.powi(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0,degree);
	}


	
	
}
