/**
 * 
 */
package ec.app.kernel_gp;

import java.io.DataOutputStream;
import java.io.IOException;

import libsvm.svm;
import libsvm.svm_gp_problem;
import libsvm.svm_model;
import ec.EvolutionState;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaFitness;
import ec.gp.koza.KozaStatistics;
import ec.simple.SimpleEvolutionState;
import ec.util.Parameter;

/**
 * @author tomek
 *
 */
public class SimpleEvolutionStateSVM extends SimpleEvolutionState {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double[][] results;
	
	/**
	 * 
	 */
	public SimpleEvolutionStateSVM() {
		super();
	}
	
	 public void setup(final EvolutionState state, final Parameter base)
     {		 
		super.setup(state, base); 
		results = new double[this.numGenerations][5];
     }
	
    /**
     * @param result
     */
    public void finish(int result) 
        {
        //Output.message("Finishing");
        /* finish up -- we completed. */
        statistics.finalStatistics(this,result);
		try {
//			testSolution();
			writeSolutions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finisher.finishPopulation(this,result);
        exchanger.closeContacts(this,result);
        evaluator.closeContacts(this,result);
        }
    
    public double writeSolutions() throws IOException
    {
    	double fittnes = 0.0;
    	double accuracy = 0.0;
    	double f1 = 0.0;
    	double mcc = 0.0;
    	double probability = 0.0;
    	
    	DataOutputStream output = new DataOutputStream(System.out);
		
    	for(double[] result: results){
    		fittnes = result[0];
    		accuracy = result[1];
    		f1 = result[2];
    		mcc = result[3];
    		probability = result[4];
    		
    		output.writeBytes(fittnes+" ");
    		output.writeBytes(accuracy*100+" ");
    		output.writeBytes(f1+" ");
    		output.writeBytes(mcc+" ");
    		output.writeBytes(probability+" ");
    	}
		
		output.close();
    	return accuracy;
    }
    
//    
//    public double testSolution() throws IOException
//    {
//    	double accuracy = 0.0;
//    	double fittnes = 0.0;
//    	
//    	KozaStatistics st = (KozaStatistics)statistics;
//    	GPIndividual bestSoFar = (GPIndividual) (st.getBestSoFar())[0];
//
//	    //Parameter train_path = new Parameter("train-file");
//	    Parameter train_test_path = new Parameter("traintest-file");
//	    Parameter validation_path = new Parameter("validation-file");
//	    Parameter output_path_param = new Parameter("output-file");
//
//	    
//	    //String trainFilepath = this.parameters.getString(train_path, null);
//	    String trainTestFilepath = this.parameters.getString(train_test_path, null);	    
//	    String validationFilepath = this.parameters.getString(validation_path, null);
//	    String output_file_name = this.parameters.getString(output_path_param, null);
//	    
//
//	    //svm_gp_problem svm_probl_train = Kernel_GP_problem.read_problem(trainFilepath);
//	    svm_gp_problem svm_probl_train_test = Kernel_GP_problem.read_problem(trainTestFilepath);
//	    svm_gp_problem svm_probl_validation = Kernel_GP_problem.read_problem(validationFilepath);
//    	((svm_gp_problem)svm_probl_train_test).ind = bestSoFar;
//    	((svm_gp_problem)svm_probl_validation).ind = bestSoFar;
//    	((svm_gp_problem)svm_probl_train_test).input = new SVMData();
//    	((svm_gp_problem)svm_probl_validation).input = new SVMData();
//    	
//		Kernel_GP_problem.set_svm_params(250, 1, 0.001, 1);
//
//		//train libsvm once again using the best individual and both train+test datasets as training dataset    	
//    	svm_model model = svm.svm_train(svm_probl_train_test, Kernel_GP_problem.svm_params);
//   	
////    	DataOutputStream output = new DataOutputStream(new PrintStream(new FileOutputStream(new File(output_file_name))));
//    	//TODO: set super.output to get statistics from EvolutionState
//    	   	
//    	accuracy = libsvm.Svm_predict_gp.predict_problem(svm_probl_validation, model).accuracy;
//    	fittnes = ((KozaFitness)(bestSoFar.fitness)).adjustedFitness();
//    	
//    	DataOutputStream output = new DataOutputStream(System.out);
//		output.writeBytes(fittnes+" ");
//		output.writeBytes(accuracy*100+" ");
//		output.close();
//    	return accuracy;
//    }
}
