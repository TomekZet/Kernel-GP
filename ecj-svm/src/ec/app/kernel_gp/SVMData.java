/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class SVMData extends GPData 
{
	  public svm_node[] X; //used by kernel functions
	  public svm_node[] Y; //used by kernel functions	  
	  public double val;  //USed by kernel agregation functions
	  
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		((SVMData)gpd).val = this.val;
		System.arraycopy(X,  0, ((SVMData)gpd).X, 0, X.length);
		System.arraycopy(Y,  0, ((SVMData)gpd).Y, 0, Y.length);
	}
}


