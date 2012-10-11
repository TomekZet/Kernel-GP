#-*- coding: utf-8 -*-
'''
Created on Sep 15, 2012

@author: tomek
'''

import subprocess
import argparse
import time
import datetime
import os

import pyDataSet

'''
This script computes various benchmarks:
- accuracy(population size)
- time(population size)
- accuracy(data set)
'''
def mean(data):
    return sum(data) / len(data)


def sd(data, mean):
    return math.sqrt(sum([math.pow(x - mean, 2) for x in data])/(len(data) ))


def shuffle_datasets(dataset_path, no_seeds=10):
    '''
    Shuffles dataset 'dataset_path' into 'no_seeds' tuples of train and test datasets
    Result datasets are written into files
    Filenames are made by appending original dataset filenam with 'seed'+number of shuffle
    '''
    out_filenames = []
    for seed in range(no_seeds):
        dataset_out = '%s.seed%d' % (dataset_path, seed)
        out_filenames.append(dataset_out)
        pyDataSet.process(dataset_path, dataset_out, test=33, rand=True)
    return out_filenames


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description="Experiment runner")
    parser.add_argument('-x', '--xmx', help='Java heap size', type=int, default=1024)
    parser.add_argument('-s', '--seeds', help='Number of seeds for division of train and test set', type=int, default=10)        
    parser.add_argument('-g', '--generations', help='Max number of generations', type=int, default=5)
    parser.add_argument('-p', '--popmax', help='Maximum size of population', type=int, default=101)
    parser.add_argument('--popstep', help='Step of generations size incrementation', type=int, default=2)    
    parser.add_argument('--genstep', help='Step of population size incrementation', type=int, default=20)
    parser.add_argument('-d', '--datasets', help='Names of datasets to be used', nargs='+')    
    parser.add_argument('-e', '--errors', help='Show errors on stdout (do not write them to file', action='store_true')
#    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')
    
    args = parser.parse_args()
    
    
    datasets = {'iris':"iris.scale",
                 'dna':"dna.scale",
                 'vowel':"vowel.scale",
                 #"clinical+volumes.arff"
                 }
    
    java_heap = args.xmx

    no_seeds = args.seeds
    
    pop_size_min = 1
    pop_size_max = args.popmax
    pop_size_step = args.popstep

    generations_min = 1
    generations_max = args.generations
    generations_step = args.genstep
        
    cv_folds = 10
    cv = False
    
    output_filename = "results/result.%s" % (datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")) 
    mystatfilename = output_filename+".stat"
    statfilename = "result.%s.stat" % (datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))    

    err_output = None
    if not args.errors:
        err_output_filename = output_filename+'.err'
        err_output = open(err_output_filename, "w")
    
    output = open(output_filename+'.dat', "w")   
    #TODO: dodać nagłówki accuracy dla kolejnych seedow i dla średniej accuracy? (Jak dostać się do wartości, żeby obliczyć średnią?)
    iterative_hadings = " ".join(['fitness{0} accuracy{0}'.format(i) for i in range(no_seeds)])
    output.write("N dataset population_size generations cross_validation cv_folds %s mean_fitness mean_accuracy time\n"% iterative_hadings)
    output.flush()
           
    i = 0       
    for dataset_name in args.datasets or datasets.keys():
        dataset = datasets.get(dataset_name)
        dataset_path = "data/"+dataset
        if not dataset:
            continue
        shuffled_datasets  = shuffle_datasets(dataset_path, no_seeds)        
        
        for r in range(pop_size_min, pop_size_max+1, pop_size_step):
            for generations in range(generations_min, generations_max+1, generations_step):                
                i+=1
                print "Running experiment for {0} dataset".format(dataset)
                print "Population size:%d, generations size:%d"%(r,generations)
                output.write("%d "% i) #row number in output file
                output.write("%s " % dataset) #Dataset name
                output.write("%d " % r) #population size
                output.write("%d " % generations) #number of generations
                output.write("%s " % cv) # was cross validation  used
                output.write("%d " % cv_folds) # number of cross validation folds
                output.flush()
                interval = 0.0;

                mean_acc = 0.0
                mean_fit = 0.0
                for shuffled_dataset in shuffled_datasets:
                    print "Processing %s"%(shuffled_dataset)
                    shuffled_dataset = os.path.join(os.getcwd(), shuffled_dataset)
                    train = shuffled_dataset+".tr"                    
                    test = shuffled_dataset+".t"
                    validation = dataset_path+".v"
                    
                    args_list = [
                         'java',
                         '-classpath',
                         r'bin:lib/ecj',
                         '-Xmx%dm'%(java_heap),
                         'ec.Evolve',
                         '-file', 'src/ec/app/kernel_gp/kernel_gp.params',
                         '-p', 'output-file=%s'% mystatfilename,
                         '-p', 'stat.file=%s'% statfilename,
                         '-p', 'train-file=%s'% train,
                         '-p', 'test-file=%s'% test,
                         '-p', 'validation-file=%s'% validation,
                         '-p', 'generations=%d'% generations,
                         '-p', 'pop.subpop.0.size=%s'%r,
                         '-p', 'cross-validation=%s'%cv,
                         '-p', 'cv-folds=%d' % cv_folds
                         ]
                    
                    start = time.time()
#                    subprocess.call(args_list, stdout=output, stderr=err_output)
                    results = subprocess.check_output(args_list, stderr=err_output)
                    end = time.time()
                    interval += end - start
                    print "Time: %.03f seconds" % interval
                    fitness, accuracy = results.split()
                    output.write(fitness+" ")
                    output.write(accuracy+" ")
                    mean_fit += float(fitness)
                    mean_acc += float(accuracy)
#                    output.flush()
                mean_acc /= no_seeds
                mean_fit /= no_seeds
                interval /= no_seeds
                output.write("%f " % mean_fit) # mean fitness
                output.write("%f " % mean_acc) # men accuracy
                output.write("%f\n" % interval) # execution time
    
    output.close()
    if not args.errors:
        err_output.close()
            