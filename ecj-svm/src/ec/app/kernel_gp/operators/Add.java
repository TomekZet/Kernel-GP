/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/
package ec.app.kernel_gp.operators;

import ec.*;
import ec.app.kernel_gp.SVMData;
import ec.gp.*;
import ec.util.Parameter;

public class Add extends GPNode
    {
    public String toString() { return "+"; }

    public void checkConstraints(final EvolutionState state,
        final int tree,
        final GPIndividual typicalIndividual,
        final Parameter individualBase)
        {
        super.checkConstraints(state,tree,typicalIndividual,individualBase);
        if (children.length!=2)
            state.output.error("Incorrect number of children for node " + 
                toStringForError() + " at " +
                individualBase);
        }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
	        double result;
	        SVMData rd = ((SVMData)(input));
	
	        children[0].eval(state,thread,input,stack,individual,problem);
	        result = rd.val;
	
	        children[1].eval(state,thread,input,stack,individual,problem);
	        rd.val = result + rd.val;
        }
    }

