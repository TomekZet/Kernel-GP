/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.kernel_gp;
import java.util.Arrays;

import libsvm.svm_node;
import ec.gp.*;

public class SVMNodeData extends GPData
    {
    public libsvm.svm_node[] nodes;    // return value

    public void copyTo(final GPData gpd)   // copy my stuff to another DoubleData
        { 
    		
    		((SVMNodeData)gpd).nodes = Arrays.copyOf(nodes, nodes.length);
    	}
    }


