/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class SVMData extends GPData 
{
	  public svm_node[] val;    // return value
	  
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		// TODO Auto-generated method stub
		System.arraycopy(val,  0, ((SVMData)gpd).val, 0, val.length);
	}
}



