package ec.app.kernel_gp;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Poly extends GPNode {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eval(EvolutionState state,
			int thread,
			GPData input,
			ADFStack stack,
			GPIndividual individual,
			Problem problem) 
	{
		// TODO Auto-generated method stub

/*        double result1;
        double result2;
        DoubleData data = ((DoubleData)(input));

        children[0].eval(state,thread,input,stack,individual,problem);
        result1 = data.val;

        children[1].eval(state,thread,input,stack,individual,problem);
        result2 = data.val;
        
        data.val = powi(gamma*libsvm.SVC_Q_GP.dot(result1,result2)+coef0,degree);
        */
        
        
		SVMData data = (SVMData)input;
		
		children[0].eval(state,thread,input,stack,individual,problem);
        svm_node[] x = data.val;
        
		children[1].eval(state,thread,input,stack,individual,problem);
		svm_node[] y = data.val;		   
		
		
		DoubleData erc = (DoubleData)input;
		
		children[2].eval(state,thread,input,stack,individual,problem);
		double gamma = erc.val;

		children[3].eval(state,thread,input,stack,individual,problem);
		double coef0 = erc.val;
		
		
		IntegerData ercInt = (IntegerData)input;
		
		children[4].eval(state,thread,input,stack,individual,problem);
		int degree = ercInt.val;
	

		DoubleData doubleData = (DoubleData)input;
		
		doubleData.val = powi(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0,degree);
		
	}

	private static double powi(double base, int times)
	{
		double tmp = base, ret = 1.0;

		for(int t=times; t>0; t/=2)
		{
			if(t%2==1) ret*=tmp;
			tmp = tmp * tmp;
		}
		return ret;
	}
	
	
}
