package ec.app.svm;

import weka.classifiers.functions.supportVector.*;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.classifiers.Evaluation;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class WekaSVM {
	
	private weka.classifiers.functions.LibSVM classifier;
	private weka.core.Instances instances;
	
	
	public WekaSVM(int svmType, int svmKernel) throws Exception{
        
        classifier = new  weka.classifiers.functions.LibSVM();
                
        //classifier.setSVMType(new weka.core.SelectedTag(svmType, classifier.TAGS_SVMTYPE));
        classifier.setKernelType(new weka.core.SelectedTag(svmKernel, classifier.TAGS_KERNELTYPE)) ;
        
        BufferedReader reader = new BufferedReader(new FileReader("iris.arff"));
        this.instances = new Instances (reader);
        reader.close();
        this.instances.setClassIndex(this.instances.numAttributes() - 1);
        this.classifier.buildClassifier(this.instances);
	}
	
	public float evaluate() throws Exception{
		
		Evaluation eval = new Evaluation(this.instances);
		eval.evaluateModel(this.classifier, this.instances);
		System.out.println(eval);
		return (float)eval.pctCorrect();
	}

}
	