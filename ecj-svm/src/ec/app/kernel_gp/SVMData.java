/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class SVMData extends GPData 
{
	  public svm_node[] svm_val;    // return value
	  public double val;
	  
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		((SVMData)gpd).val = this.val;
		System.arraycopy(svm_val,  0, ((SVMData)gpd).svm_val, 0, svm_val.length);
	}
}


