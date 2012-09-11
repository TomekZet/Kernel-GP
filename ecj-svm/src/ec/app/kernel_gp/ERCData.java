/**
 * @author Tomek
 *
 */

package ec.app.kernel_gp;

import ec.gp.GPData;
import libsvm.svm_node;


public class ERCData extends GPData 
{
	public double gamma;	//libsvm parameter
	public double coef0;	//libsvm parameter
	public int degree;		//libsvm parameter
	public double a;		//a used in aMul function
	  
	
	/* (non-Javadoc)
	 * @see ec.gp.GPData#copyTo(ec.gp.GPData)
	 */
	@Override
	public void copyTo(GPData gpd) {
		((ERCData)gpd).gamma = this.gamma;
		((ERCData)gpd).coef0 = this.coef0;
		((ERCData)gpd).degree = this.degree;
		((ERCData)gpd).a = this.a;

	}
}


