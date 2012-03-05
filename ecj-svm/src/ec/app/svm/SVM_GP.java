/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.svm;
import ec.*;
import ec.simple.*;
import ec.vector.*;



public class SVM_GP extends Problem implements SimpleProblemForm
    {
    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {
	        if (ind.evaluated) return;
	
	        if (!(ind instanceof IntegerVectorIndividual))
	            state.output.fatal("Whoa!  It's not a IntegerVectorIndividual!!!",null);
	        
	        
	        IntegerVectorIndividual ind2 = (IntegerVectorIndividual)ind;
	        
	        WekaSVM wekaSVM;
			try {
				wekaSVM = new WekaSVM(ind2.genome[0], ind2.genome[1]);
		        float prctCorrect;
				prctCorrect = wekaSVM.evaluate();
	
	        
	        if (!(ind2.fitness instanceof SimpleFitness))
	            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
	        ((SimpleFitness)ind2.fitness).setFitness(state,
	            /// ...the fitness...
	        		prctCorrect,
	            ///... is the individual ideal?  Indicate here...
	        		prctCorrect == 100.0);
	        ind2.evaluated = true;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
    }
