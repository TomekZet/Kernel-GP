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
 * Gaussian Kernel
 * The adjustable parameter sigma plays a major role in the performance of the kernel,
 *  and should be carefully tuned to the problem at hand. If overestimated, 
 *  the exponential will behave almost linearly and the higher-dimensional 
 *  projection will start to lose its non-linear power. In the other hand, 
 *  if underestimated, the function will lack regularization and the decision 
 *  boundary will be highly sensitive to noise in training data.
 * @author tomek
 *
 */
public class RBF extends GPNode {

	@Override
	public String toString() {
		return "RBF";
	}

	@Override
	public void eval(EvolutionState state, int thread, GPData input,
			ADFStack stack, GPIndividual individual, Problem problem) {

		SVMData data = (SVMData)input;
		//Eval selection node
		children[0].eval(state,thread,data,stack,individual,problem);

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

		children[1].eval(state,thread,data,stack,individual,problem);
		double gamma = data.val;

		data.val =	Math.exp(-gamma*sum);
	}
}
