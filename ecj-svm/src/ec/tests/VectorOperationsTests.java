/**
 * 
 */
package ec.tests;

import libsvm.svm_node;
import libsvm.SVC_Q_GP;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author tomek
 *
 */
public class VectorOperationsTests {

	svm_node[] x = {
			new svm_node(1, 2.0),
			new svm_node(2, 3.0),
			new svm_node(3, 4.0),
			new svm_node(4, 1.0)
			};
	
	svm_node[] xx = {
			new svm_node(1, 2.0),
			new svm_node(2, 3.0),
			new svm_node(3, 4.0),
			new svm_node(4, 1.0)
			};
	
	svm_node[] y = {
			new svm_node(1, 1.0),
			new svm_node(2, 5.0),
			new svm_node(3, 2.0),
			new svm_node(4, 7.0)
			};
	
	svm_node[] z = {
			new svm_node(1, 5.0),
			new svm_node(2, 3.0),
			new svm_node(4, 7.0)
			};
	
	svm_node[] w = {
			new svm_node(1, 1.0),
			new svm_node(2, 5.0),
			new svm_node(3, 2.0),
			};
	
	svm_node[] x_y = {
			new svm_node(1, 1.0),
			new svm_node(2, -2.0),
			new svm_node(3, 2.0),
			new svm_node(4, -6.0)
			};
	
	svm_node[] x_z = {
			new svm_node(1, -3.0),
			new svm_node(2, 0.0),
			new svm_node(3, 4.0),
			new svm_node(4, -6.0)
			};
	
	svm_node[] z_x = {
			new svm_node(1, 3.0),
			new svm_node(2, 0.0),
			new svm_node(3, -4.0),
			new svm_node(4, 6.0)
			};
	
	svm_node[] z_w = {
			new svm_node(1, 4.0),
			new svm_node(2, -2.0),
			new svm_node(3, -2.0),
			new svm_node(4, 7.0)
			};
	
	/**
	 * Test method for {@link libsvm.SVC_Q_GP#vector_difference(libsvm.svm_node[], libsvm.svm_node[])}.
	 */
	@Test
	public final void testVector_difference() {
		Assert.assertArrayEquals(x_y, SVC_Q_GP.vector_difference(x, y));
		Assert.assertArrayEquals(x_z, SVC_Q_GP.vector_difference(x, z));
		Assert.assertArrayEquals(z_x, SVC_Q_GP.vector_difference(z, x));
		Assert.assertArrayEquals(z_w, SVC_Q_GP.vector_difference(z, w));
		
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

//	/**
//	 * Test method for {@link libsvm.SVC_Q_GP#dot(libsvm.svm_node[], libsvm.svm_node[])}.
//	 */
//	@Test
//	public final void testDot() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	/**
//	 * Test method for {@link libsvm.SVC_Q_GP#magnitude(libsvm.svm_node[])}.
//	 */
//	@Test
//	public final void testMagnitude() {
//		fail("Not yet implemented"); // TODO
//	}


}
