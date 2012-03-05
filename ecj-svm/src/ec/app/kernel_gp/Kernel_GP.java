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

import libsvm.SVM_GP;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import ec.util.*;
import java.io.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

public class Kernel_GP extends GPProblem implements SimpleProblemForm
{
  public svm_node[] currentX;
  public svm_node[] currentY;
  
  public DoubleData input;
  private svm_parameter param;	
  private svm_problem prob;		// set by read_problem
  private String input_file_name;		
  private int nr_fold;

  public Object clone()
      {
	  Kernel_GP newobj = (Kernel_GP) (super.clone());
      newobj.input = (DoubleData)(input.clone());
      return newobj;
      }

  public void setup(final EvolutionState state,
      final Parameter base)
      {
      // very important, remember this
      super.setup(state,base);

      // set up our input -- don't want to use the default base, it's unsafe here
      input = (DoubleData) state.parameters.getInstanceForParameterEq(
          base.push(P_DATA), null, DoubleData.class);
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
	          SVM_GP.setInd(ind);
	          SVM_GP.setInput(input);
	          SVM_GP.setProblem(this);
	          SVM_GP.setStack(stack);
	          SVM_GP.setState(state);
	          SVM_GP.setSubpopulation(subpopulation);
	          SVM_GP.setThreadnum(threadnum);
	          
	  		  double[] target = new double[prob.l];
	          SVM_GP.svm_cross_validation(prob, param, nr_fold, target);
	    	  
	              /*((GPIndividual)ind).trees[0].child.eval(
	                  state,threadnum,input,stack,((GPIndividual)ind),this);*/
	
	//              result = Math.abs(expectedResult - input.x);
	
	          for(i=0;i<prob.l;i++)
					if(target[i] == prob.y[i])
						++total_correct;
	          accuracy = (float) (total_correct/prob.l);
	          System.out.print("Cross Validation Accuracy = "+100.0*accuracy+"%\n");
	          
	           }
	      		SimpleFitness f = ((SimpleFitness)ind.fitness);
	      		f.setFitness(state, accuracy, accuracy == 1.0);
	          // the fitness better be KozaFitness!

	      //KozaFitness f = ((KozaFitness)ind.fitness);
	          //f.setStandardizedFitness(state,(float)accuracy);
	          //f.hits = hits;
	          ind.evaluated = true;
      }
  
  
  private void set_params()
  {
		param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = 0;	// 1/num_features
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];

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
		BufferedReader fp = new BufferedReader(new FileReader(input_file_name));
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

		prob = new svm_problem();
		prob.l = vy.size();
		prob.x = new svm_node[prob.l][];
		for(int i=0;i<prob.l;i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for(int i=0;i<prob.l;i++)
			prob.y[i] = vy.elementAt(i);

		if(param.gamma == 0 && max_index > 0)
			param.gamma = 1.0/max_index;


		fp.close();
	}
}




