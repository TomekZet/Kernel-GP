/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class SVMData extends GPData 
{
	  public svm_node[] X = new svm_node[0]; //passed from select to kernels
	  public svm_node[] Y = new svm_node[0]; //passed from select to kernels
	  public boolean[] select_vector = new boolean[0]; //passed from erc vector to select
	  public double X2; //passed from terminals to kernels
	  public double Y2; //passed from terminals to kernels
	  public double val = 0;  //pased from kernel or agregates to agregates
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		((SVMData)gpd).val = this.val;
		((SVMData)gpd).X2 = this.X2;
		((SVMData)gpd).Y2 = this.Y2;
		System.arraycopy(X,  0, ((SVMData)gpd).X, 0, X.length);
		System.arraycopy(Y,  0, ((SVMData)gpd).Y, 0, Y.length);
	}
	
	public Object clone()
	{
		SVMData other = (SVMData)(super.clone());
		other.X = (svm_node[])(X.clone());
		other.Y = (svm_node[])(Y.clone());
		other.val = val;
		other.X2 = X2;
		other.Y2 = Y2;
		return other;
	}
}


