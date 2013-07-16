/**
 * 
 */
package ec.app.kernel_gp;

import java.io.*;

import libsvm.Results;
import libsvm.svm;
import libsvm.svm_gp_problem;
import libsvm.svm_model;

import ec.*;
import ec.steadystate.*;
import ec.simple.*;

import ec.util.*;

import ec.gp.*;
import ec.gp.koza.KozaFitness;
import ec.gp.koza.KozaStatistics;
import ec.simple.SimpleEvolutionState;


/**
 * @author tomek
 *
 */
public class KozaStatisticsGP extends KozaStatistics {

	/**
	 * 
	 */
	
	public KozaStatisticsGP() {
		super();

	}
	
	public void postEvaluationStatistics(final EvolutionState state)
    {
	    super.postEvaluationStatistics(state);
		try {
			testSolution((SimpleEvolutionStateSVM)state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public double testSolution(final SimpleEvolutionStateSVM state) throws IOException
    {
    	double accuracy = 0.0;
    	double fitness = 0.0;
    	double f1 = 0.0;
    	double mcc = 0.0;
    	double probability = 0.0;
    	
    	GPIndividual bestSoFar = (GPIndividual) best_of_run[0];

	    //Parameter train_path = new Parameter("train-file");
	    Parameter train_val_path = new Parameter("trainval-file");
	    Parameter test_path = new Parameter("test-file");
	    Parameter output_path_param = new Parameter("output-file");

	    
	    //String trainFilepath = this.parameters.getString(train_path, null);
	    String trainValFilepath = state.parameters.getString(train_val_path, null);	    
	    String testFilepath = state.parameters.getString(test_path, null);
	    
	    
	    //svm_gp_problem svm_probl_train = Kernel_GP_problem.read_problem(trainFilepath);
	    svm_gp_problem svm_probl_train_val = Kernel_GP_problem.read_problem(trainValFilepath);
	    svm_gp_problem svm_probl_test = Kernel_GP_problem.read_problem(testFilepath);
    	((svm_gp_problem)svm_probl_train_val).ind = bestSoFar;
    	((svm_gp_problem)svm_probl_test).ind = bestSoFar;
    	((svm_gp_problem)svm_probl_train_val).input = new SVMData();
    	((svm_gp_problem)svm_probl_test).input = new SVMData();
    	
//		//TODO: use thesame parameters as in training 
//    	Kernel_GP_problem.set_svm_params(250, 1, 0.001, 1, 1);

		//train libsvm once again using the best individual and both train+test datasets as training dataset    	
    	svm_model model = svm.svm_train(svm_probl_train_val, Kernel_GP_problem.svm_params);
   	
//    	DataOutputStream output = new DataOutputStream(new PrintStream(new FileOutputStream(new File(output_file_name))));
    	//TODO: set super.output to get statistics from EvolutionState
    	   	
    	Results results = libsvm.Svm_predict_gp.predict_problem(svm_probl_test, model);
    	accuracy = results.accuracy;
    	f1 = results.meanf1;
    	mcc = results.meanMCC;
    	probability = results.meanProbability;

    	fitness = ((KozaFitness)(bestSoFar.fitness)).adjustedFitness();
    	
    	
    	state.results[state.generation] = new double[5];
    	state.results[state.generation][0] = fitness;
    	state.results[state.generation][1] = accuracy;
    	state.results[state.generation][2] = f1;
    	state.results[state.generation][3] = mcc;
    	state.results[state.generation][4] = probability;
    	
    	state.output.println("\nBest Individual's Accuracy: "+accuracy,statisticslog);
    	state.output.println("\nBest Individual's F1: "+f1,statisticslog);   
    	state.output.println("\nBest Individual's MCC: "+mcc,statisticslog);   
    	state.output.println("\nBest Individual's Probability: "+probability,statisticslog);   
    	
    	return accuracy;
    }
    
    public void finalStatistics(final EvolutionState state, final int result){
    	super.finalStatistics(state, result);
    	double accuracy = ((SimpleEvolutionStateSVM)state).results[state.generation][1];
    	double f1 = ((SimpleEvolutionStateSVM)state).results[state.generation][2];
    	double mcc = ((SimpleEvolutionStateSVM)state).results[state.generation][3];
    	double probability = ((SimpleEvolutionStateSVM)state).results[state.generation][4];
    	
    	state.output.println("\nBest Individual's Accuracy: "+accuracy,statisticslog);    	    	
    	state.output.println("\nBest Individual's F1: "+f1,statisticslog);    	    	
    	state.output.println("\nBest Individual's MCC: "+mcc,statisticslog);    	    	
    	state.output.println("\nBest Individual's Probability: "+probability,statisticslog);    	    	
    	
    }


}
