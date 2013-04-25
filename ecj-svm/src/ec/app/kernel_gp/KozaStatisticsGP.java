/**
 * 
 */
package ec.app.kernel_gp;

import java.io.*;
import java.io.DataOutputStream;
import java.io.IOException;

import libsvm.svm;
import libsvm.svm_gp_problem;
import libsvm.svm_model;

import ec.*;
import ec.steadystate.*;
import ec.simple.*;

import ec.util.*;
import ec.util.Parameter;

import ec.gp.*;
import ec.gp.GPIndividual;
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
    	
    	GPIndividual bestSoFar = (GPIndividual) best_of_run[0];

	    //Parameter train_path = new Parameter("train-file");
	    Parameter train_test_path = new Parameter("traintest-file");
	    Parameter validation_path = new Parameter("validation-file");
	    Parameter output_path_param = new Parameter("output-file");

	    
	    //String trainFilepath = this.parameters.getString(train_path, null);
	    String trainTestFilepath = state.parameters.getString(train_test_path, null);	    
	    String validationFilepath = state.parameters.getString(validation_path, null);
	    
	    
	    //svm_gp_problem svm_probl_train = Kernel_GP_problem.read_problem(trainFilepath);
	    svm_gp_problem svm_probl_train_test = Kernel_GP_problem.read_problem(trainTestFilepath);
	    svm_gp_problem svm_probl_validation = Kernel_GP_problem.read_problem(validationFilepath);
    	((svm_gp_problem)svm_probl_train_test).ind = bestSoFar;
    	((svm_gp_problem)svm_probl_validation).ind = bestSoFar;
    	((svm_gp_problem)svm_probl_train_test).input = new SVMData();
    	((svm_gp_problem)svm_probl_validation).input = new SVMData();
    	
		Kernel_GP_problem.set_svm_params(250, 1, 0.001, 1);

		//train libsvm once again using the best individual and both train+test datasets as training dataset    	
    	svm_model model = svm.svm_train(svm_probl_train_test, Kernel_GP_problem.svm_params);
   	
//    	DataOutputStream output = new DataOutputStream(new PrintStream(new FileOutputStream(new File(output_file_name))));
    	//TODO: set super.output to get statistics from EvolutionState
    	   	
    	accuracy = libsvm.Svm_predict_gp.predict_problem(svm_probl_validation, model).accuracy;
    	f1 = libsvm.Svm_predict_gp.predict_problem(svm_probl_validation, model).meanf1;
    	mcc = libsvm.Svm_predict_gp.predict_problem(svm_probl_validation, model).meanMCC;

    	fitness = ((KozaFitness)(bestSoFar.fitness)).adjustedFitness();
    	
    	
    	state.results[state.generation] = new double[4];
    	state.results[state.generation][0] = fitness;
    	state.results[state.generation][1] = accuracy;
    	state.results[state.generation][2] = f1;
    	state.results[state.generation][3] = mcc;
    	
    	state.output.println("\nBest Individual's Accuracy: "+accuracy,statisticslog);
    	state.output.println("\nBest Individual's F1: "+f1,statisticslog);   
    	state.output.println("\nBest Individual's MCC: "+mcc,statisticslog);   
    	
    	return accuracy;
    }
    
    public void finalStatistics(final EvolutionState state, final int result){
    	super.finalStatistics(state, result);
    	double accuracy = ((SimpleEvolutionStateSVM)state).results[state.generation][1];
    	double f1 = ((SimpleEvolutionStateSVM)state).results[state.generation][2];
    	double mcc = ((SimpleEvolutionStateSVM)state).results[state.generation][3];
    	state.output.println("\nBest Individual's Accuracy: "+accuracy,statisticslog);    	    	
    	state.output.println("\nBest Individual's F1: "+f1,statisticslog);    	    	
    	state.output.println("\nBest Individual's MCC: "+mcc,statisticslog);    	    	
    	
    }


}
