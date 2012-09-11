/*
  Copyright 2011 by Tomasz Zietkiewicz
  Based on ECJ tutorial by by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.kernel_gp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.SVC_Q_GP;
import libsvm.Svm_GP;
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
  public svm_node[] currentX;
  public svm_node[] currentY;
  
  public GPData input;
  public svm_parameter svm_param;	
  private svm_problem svm_probl;		// set by read_problem
  private String train_file_name = "/home/tomek/studia/magisterka/Kernel-GP Git/ecj-svm/data/vowel.scale";		
  private String test_file_name = "/home/tomek/studia/magisterka/Kernel-GP Git/ecj-svm/data/vowel.scale.t";
  private int nr_fold = 3;

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
	
	      this.set_params();
	      try {
	    	  this.read_problem();
	      }
	      catch (Exception e) {
	    	  System.err.println(e);
	      }
	      
	      
	      // set up our input -- don't want to use the default base, it's unsafe here
	      input = (GPData) state.parameters.getInstanceForParameterEq(
	          base.push(P_DATA), null, GPData.class);
	      input.setup(state,base.push(P_DATA));
      }

  public void evaluate(final EvolutionState state, 
      final Individual ind, 
      final int subpopulation,
      final int threadnum)
      {
	      float accuracy = (float) 0.0;
	      int i;
	      double total_correct = 0;
	      
	      if (!ind.evaluated)  // don't bother reevaluating
	          {
	          
	  		  double[] target = new double[svm_probl.l];
	      
	  		
	  		  SVC_Q_GP.state = state; 
	  		  SVC_Q_GP.ind = ((GPIndividual)ind);
	  		  SVC_Q_GP.subpopulation = subpopulation;
	  		  SVC_Q_GP.threadnum = threadnum;
	  		  SVC_Q_GP.problem = this;
	  		  SVC_Q_GP.input = input;
	  		  SVC_Q_GP.stack = stack;
	  		  
	  		  
	  		((GPIndividual)ind).trees[0].printTreeForHumans(state, 0);
	  		  
	  		Svm_GP.svm_cross_validation(svm_probl, svm_param, nr_fold, target);
	    	  
	
	          for(i=0;i<svm_probl.l;i++)
					if(target[i] == svm_probl.y[i])
						++total_correct;
	          accuracy = (float) (total_correct/svm_probl.l);
	          System.out.print("Cross Validation Accuracy = "+100.0*accuracy+"%\n");
	          
	           }
	      

	      KozaFitness f = ((KozaFitness)ind.fitness);
	          f.setStandardizedFitness(state,(float)((1-accuracy)/(accuracy+0.000000001)));
	          ind.evaluated = true;
      }
  
  
  private void set_params()
  {
		svm_param = new svm_parameter();
		// default values
		svm_param.svm_type = svm_param.C_SVC;
		svm_param.kernel_type = svm_param.RBF;
		svm_param.degree = 3;
		svm_param.gamma = 0;	// 1/num_features - set by read_problem
		svm_param.coef0 = 0;
		svm_param.nu = 0.5;
		svm_param.cache_size = 100;
		svm_param.C = 1;
		svm_param.eps = 1e-3;
		svm_param.p = 0.1;
		svm_param.shrinking = 1;
		svm_param.probability = 0;
		svm_param.nr_weight = 0;
		svm_param.weight_label = new int[0];
		svm_param.weight = new double[0];

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
  
  
	private void read_problem() throws IOException
	{
		BufferedReader fp = new BufferedReader(new FileReader(train_file_name));
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

		svm_probl = new svm_problem();
		svm_probl.l = vy.size();
		svm_probl.x = new svm_node[svm_probl.l][];
		for(int i=0;i<svm_probl.l;i++)
			svm_probl.x[i] = vx.elementAt(i);
		svm_probl.y = new double[svm_probl.l];
		for(int i=0;i<svm_probl.l;i++)
			svm_probl.y[i] = vy.elementAt(i);

		if(svm_param.gamma == 0 && max_index > 0)
			svm_param.gamma = 1.0/max_index;


		fp.close();
	}
}




