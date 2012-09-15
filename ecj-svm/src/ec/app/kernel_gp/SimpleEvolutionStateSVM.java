/**
 * 
 */
package ec.app.kernel_gp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import libsvm.SVC_Q_GP;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import ec.Individual;
import ec.gp.GPIndividual;
import ec.gp.koza.KozaStatistics;
import ec.simple.SimpleEvolutionState;

/**
 * @author tomek
 *
 */
public class SimpleEvolutionStateSVM extends SimpleEvolutionState {

	/**
	 * 
	 */
	public SimpleEvolutionStateSVM() {
		super();
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
			testSolution();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finisher.finishPopulation(this,result);
        exchanger.closeContacts(this,result);
        evaluator.closeContacts(this,result);
        }
    
    public double testSolution() throws IOException
    {
    	double accuracy = 0.0;
    	
    	KozaStatistics st = (KozaStatistics)statistics;
    	Individual bestSoFar = (st.getBestSoFar())[0];
    	SVC_Q_GP.ind = bestSoFar;

    	String trainFilepath = 		"data/clinical+volumes.arff.tr";
    	String validationFilepath = "data/clinical+volumes.arff.val";

    	String resultFilepath = "";
		Kernel_GP_problem.read_problem(trainFilepath );
		Kernel_GP_problem.set_svm_params();
    	
    	svm_model model = svm.svm_train(Kernel_GP_problem.svm_probl, Kernel_GP_problem.svm_params);
    	DataOutputStream output = new DataOutputStream(System.out);
		try 
		{
			BufferedReader input = new BufferedReader(new FileReader(validationFilepath));
//			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(resultFilepath)));
			
			
	    	SVC_Q_GP.ind = bestSoFar;
			
	    	accuracy = libsvm.Svm_predict_gp.predict(input,output,model,0);
			input.close();
		} 
		catch(FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output.writeBytes("\nAccuracy = "+accuracy*100+"\n");
		output.close();
    	return accuracy;
    }
}
