/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class SVMData extends GPData 
{
	  public svm_node[] X = new svm_node[0]; //passed from terminals to kernels
	  public svm_node[] Y = new svm_node[0]; //passed from terminals to kernels
	  public double val = 0;  //pased from kernel or agregates to agregates
	  
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		((SVMData)gpd).val = this.val;
		System.arraycopy(X,  0, ((SVMData)gpd).X, 0, X.length);
		System.arraycopy(Y,  0, ((SVMData)gpd).Y, 0, Y.length);
	}
	
	public Object clone()
	{
		SVMData other = (SVMData)(super.clone());
		other.X = (svm_node[])(X.clone());
		other.Y = (svm_node[])(Y.clone());
		other.val = val;
		return other;
	}
}


