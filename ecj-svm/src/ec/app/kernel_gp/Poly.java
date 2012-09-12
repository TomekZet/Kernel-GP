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
		
		children[0].eval(state,thread,input,stack,individual,problem);
        svm_node[] x = data.X;
		svm_node[] y = data.Y;		   
		
		ERCData erc = new ERCData();
		
		children[1].eval(state,thread,erc,stack,individual,problem);
		double gamma = erc.gamma;

		children[2].eval(state,thread,erc,stack,individual,problem);
		double coef0 = erc.coef0;
		
		children[3].eval(state,thread,erc,stack,individual,problem);
		int degree = erc.degree;
			
		data.val = powi(gamma*libsvm.SVC_Q_GP.dot(x,y)+coef0,degree);
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
