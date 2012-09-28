/*
  Copyright 2011 by Tomasz Zietkiewicz
  Based on ECJ tutorial by by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.kernel_gp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.SVC_Q_GP;
import libsvm.Svm_GP;
import libsvm.svm;
import libsvm.svm_gp_problem;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import ec.util.*;
import java.io.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

public class Kernel_GP_problem extends GPProblem implements SimpleProblemForm
{
  public GPData input;
  static public svm_parameter svm_params = new svm_parameter();
  static public int logNumber = 0;
  public svm_gp_problem svm_probl_train;		// set by read_problem
  public svm_gp_problem svm_probl_test;
  private String train_file_name;
  private String test_file_name;
  private String output_file_name;
  private int nr_fold;
  boolean cv;
  PrintStream outputStream = System.out;

  public Object clone()
      {
	  Kernel_GP_problem newobj = (Kernel_GP_problem) (super.clone());
      newobj.input = (GPData)(input.clone());
      return newobj;
      }

  public void setup(final EvolutionState state,
      final Parameter base)
      {
	      // very important, remember this
	      super.setup(state,base);
	
	      Parameter train_path_param = new Parameter("train-file");
	      Parameter test_path_param = new Parameter("test-file");
	      Parameter output_path_param = new Parameter("output-file");
	      Parameter cv_param = new Parameter("cross-validation");
	      Parameter nr_fold_param = new Parameter("cv-folds");
	      
	      train_file_name = state.parameters.getString(train_path_param, null);
	      test_file_name = state.parameters.getString(test_path_param, null);
	      cv = state.parameters.getBoolean(cv_param, null, false);
	      nr_fold = state.parameters.getInt(nr_fold_param, null, 4);
	      output_file_name = state.parameters.getString(output_path_param, null);
	      File output_file = new File(output_file_name);
	      
	      try {
			logNumber = state.output.addLog(output_file, false);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	      
//	      try {
//			outputStream = new PrintStream(new FileOutputStream(output_file));
//		} catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	      
	      set_svm_params();
	      if (svm_probl_train == null)
	      {
	    	  try {	      
	    		  svm_probl_train = read_problem(train_file_name);
	    		  svm_probl_test = read_problem(test_file_name);
		      }
		      catch (Exception e) {
		    	  System.err.println(e);
		      }
	      }
	      // set up our input -- don't want to use the default base, it's unsafe here
	      input = (SVMData) state.parameters.getInstanceForParameterEq(
	          base.push(P_DATA), null, SVMData.class);
	      input.setup(state,base.push(P_DATA));
      }

  public void evaluate(final EvolutionState state, 
      final Individual ind, 
      final int subpopulation,
      final int threadnum)
      {
       	  KozaFitness f = ((KozaFitness)ind.fitness);
	  	  long start = System.nanoTime();
	  	  double accuracy =  0.0;
	      
	  	  if (!ind.evaluated)  // don't bother reevaluating
          {
	  		  double[] target = new double[svm_probl_train.l];
		      int i;
		      double correct = 0;
	  		  
		      svm_probl_train.ind = ((GPIndividual)ind);
//		      svm_probl_train.subpopulation = subpopulation;
//		      svm_probl_train.state = state;
//		      svm_probl_train.threadnum = threadnum;
//		      svm_probl_train.problem = this;
		      svm_probl_train.input = input;
//		      svm_probl_train.stack = stack;
		      
		      svm_probl_test.ind = ((GPIndividual)ind);
//		      svm_probl_test.subpopulation = subpopulation;
//		      svm_probl_test.threadnum = threadnum;
//		      svm_probl_test.problem = this;
		      svm_probl_test.input = input;
//		      svm_probl_test.stack = stack;
	  		  
	  		 if(cv)
	  		 {
	  			svm.svm_cross_validation(svm_probl_train, svm_params, nr_fold, target);
		
		         for(i=0;i<svm_probl_train.l;i++){
		        	  if(target[i] == svm_probl_train.y[i])
							++correct;
		         }
		         accuracy = (correct/svm_probl_train.l);
	  		  }
	  		 else
	  		  {
	  			svm_model model = svm.svm_train(svm_probl_train, svm_params);
  				accuracy = libsvm.Svm_predict_gp.predict_problem(svm_probl_test, model);
	  		  }
	          
	  		  f.setStandardizedFitness(state,(float)((1-accuracy)/(accuracy+0.00000000000000000001)));	          
	          ind.evaluated = true;
          }
	  	  long time = System.nanoTime()-start;
	  	  double time_seconds = (double)time/1000000000;

	  	  ((GPIndividual)ind).trees[0].printTreeForHumans(state, logNumber);
          String message = "";
	  	  if(cv)
        	  message +="CV Accuracy = "+100.0*accuracy+"%\n";
          else
        	  message +="Test Accuracy = "+100.0*accuracy+"%\n";
	  	  message += "Time elapsed: "+Double.toString(time_seconds)+"\n";
	  	  message += "Standardized Fitness = " + f.standardizedFitness() +"\n";
	      message += "Adjusted Fitness = " + f.fitness()+"\n\n";
	      state.output.print(message, logNumber);
      }
  
  
  public static void set_svm_params()
  {
//		svm_params = new svm_parameter();
		// default values
		svm_params.svm_type = svm_params.C_SVC;
		svm_params.kernel_type = svm_params.RBF;
		svm_params.degree = 3;
		svm_params.gamma = 0;	// 1/num_features - set by read_problem
		svm_params.coef0 = 0;
//		svm_params.nu = 0.5;
		svm_params.cache_size = 100;
		svm_params.C = 1;
		svm_params.eps = 1e-3;
		svm_params.p = 0.1;
		svm_params.shrinking = 1;
//		svm_params.probability = 0;
//		svm_params.nr_weight = 0;
//		svm_params.weight_label = new int[0];
//		svm_params.weight = new double[0];
  }
  
	private static int atoi(String s)
	{
		return Integer.parseInt(s);
	}
	
	private static double atof(String s)
	{
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d))
		{
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return(d);
	}
  
  
	public static svm_gp_problem read_problem(String file_name) throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(file_name));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		int max_index = 0;

		while(true)
		{
			String line = fp.readLine();
			if(line == null) break;

			StringTokenizer st = new StringTokenizer(line," \t\n\r\f:");

			vy.addElement(atof(st.nextToken()));
			int m = st.countTokens()/2;
			svm_node[] x = new svm_node[m];
			for(int j=0;j<m;j++)
			{
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			if(m>0) max_index = Math.max(max_index, x[m-1].index);
			vx.addElement(x);
		}

		svm_gp_problem svm_probl = new svm_gp_problem();
		svm_probl.l = vy.size();
		svm_probl.x = new svm_node[svm_probl.l][];
		for(int i=0;i<svm_probl.l;i++)
			svm_probl.x[i] = vx.elementAt(i);
		svm_probl.y = new double[svm_probl.l];
		for(int i=0;i<svm_probl.l;i++)
			svm_probl.y[i] = vy.elementAt(i);

		if(svm_params.gamma == 0 && max_index > 0)
			svm_params.gamma = 1.0/max_index;

		fp.close();
		return svm_probl;
	}
}




