package ec.app.kernel_gp.kernels;

import libsvm.svm_node;
import ec.EvolutionState;
import ec.Problem;
import ec.app.kernel_gp.SVMData;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;


/**
 * The Cauchy kernel comes from the Cauchy distribution (Basak, 2008). 
 * It is a long-tailed kernel and can be used to give long-range influence 
 * and sensitivity over the high dimension space.
 * @author tomek
 *
 */
public class Cauchy extends GPNode {

	@Override
	public String toString() {
		return "Cauchy";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {

		SVMData data = (SVMData)input;
        svm_node[] x = data.X;
		svm_node[] y = data.Y;
		
		double x_square = data.X2;
		double y_square = data.Y2;
		double sum = 0;

		if (x_square == -1.0 && y_square == -1.0)
		{
			int xlen = x.length;
			int ylen = y.length;
			int i = 0;
			int j = 0;
			while(i < xlen && j < ylen)
			{
				if(x[i].index == y[j].index)
				{
					double d = x[i++].value - y[j++].value;
					sum += d*d;
				}
				else if(x[i].index > y[j].index)
				{
					sum += y[j].value * y[j].value;
					++j;
				}
				else
				{
					sum += x[i].value * x[i].value;
					++i;
				}
			}

			while(i < xlen)
			{
				sum += x[i].value * x[i].value;
				++i;
			}

			while(j < ylen)
			{
				sum += y[j].value * y[j].value;
				++j;
			}
		}
		
		else
		{
			sum = (x_square + y_square -2*libsvm.SVC_Q_GP.dot(x,y));
		}

		children[0].eval(state,thread,data,stack,individual,problem);
		double gamma = data.val;
		//double sigma = 1 / Math.sqrt(1/(2*gamma));

		data.val =	1.0 / (1.0 + sum * Math.sqrt(2*gamma));
	}
}
