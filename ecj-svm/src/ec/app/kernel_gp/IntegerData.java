/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.kernel_gp;
import libsvm.svm_node;
import ec.gp.*;

public class IntegerData extends GPData
    {
    public int val;    // return value

    public void copyTo(final GPData gpd)   // copy my stuff to another SVMData
        { ((IntegerData)gpd).val = val; }
    }


