#!/usr/bin/python
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
import pickle
import string

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


def shuffle_datasets(dataset_path_list, splits=10, test=25, valid=25, new=True):
    '''
    Shuffles datasets from 'dataset_path_list' into 'splits' tuples of train and test datasets
    Result datasets are written into files
    Filenames are made by appending original dataset filenames with 'seed'+number of shuffle
    '''
    out_filenames = []
    for seed in range(splits):
        dataset_out = '%s.seed%d' % (dataset_path_list[0], seed)
        out_filenames.append(dataset_out)
        if new:
            pyDataSet.process(dataset_path_list, dataset_out, test=test, valid=valid, rand=True, seed=False)
    return out_filenames

def getContinueFrom(filepath):
    result = 0
    try:
        with open(filepath, "r") as file:
            for line in file:
                result = line.split(" ")[0]
            result = int(result)
    except ValueError:
        result = 0
        print "ValueError"
    finally:
        return result

if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Experiment runner")
    parser.add_argument('-x', '--xmx', help='Java heap size', type=int, default=1024)
    parser.add_argument('-s', '--splits', help='Number of splits of train, test and validation set', type=int, default=5)
    parser.add_argument('-g', '--genmax', help='Max number of generations', type=int, default=5)
    parser.add_argument('-p', '--popmax', help='Maximum size of population', type=int, default=100)
    parser.add_argument('--popmin', help='Minimum size of population', type=int, default=None)
    parser.add_argument('--genmin', help='Minimum size of generations', type=int, default=None)
    parser.add_argument('--popstep', help='Step of population size incrementation', type=int, default=10)
    parser.add_argument('--genstep', help='Step of generations size incrementation', type=int, default=2)
    parser.add_argument('--costmin', help='Step of generations size incrementation', type=float, default=0.25)
    parser.add_argument('--costmax', help='Step of generations size incrementation', type=float, default=1.5)
    parser.add_argument('--coststep', help='Step of generations size incrementation', type=float, default=0.25)    
    parser.add_argument('-d', '--datasets', help='Names of datasets to be used', nargs='+')
    parser.add_argument('-e', '--errors', help='Show errors on stdout (do not write them to file', action='store_true')
    parser.add_argument('-n', '--newdata', help='Generate new data splits', action='store_true')
    parser.add_argument('-c', '--cont', help='Path to file with stopped computations to continue')
    parser.add_argument('-a', '--append', help='Path to file with stopped computations to append, without reading args from pickled file')
    parser.add_argument('--cache', help='SVM Cache size (MB)', type=int, default=100)
    parser.add_argument('--shrinking', help='Wheter to use shrinking in SVM or not', type=bool, default=False)
    parser.add_argument('--epsilon', help='Value of epsilon parameter pased to SVM', type=float, default=0.1)
    parser.add_argument('--evalthreads', help='Number of threads used to evaluate population', type=str, default="2")
    parser.add_argument('--breedthreads', help='Number of threads used to breed population', type=str, default="2")



#    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')

    time_format = "%Y-%m-%d %H:%M:%S"
    now = datetime.datetime.now().strftime(time_format)
    args = parser.parse_args()
    write_mode = "w"
    cont = ""

    if args.cont:
        with open (args.cont,'r') as f:
            pdict = pickle.load(f)
            now = pdict.get('now', now)
            args = pdict.get('args') if pdict.get('args') else args
            write_mode = "a"
            cont = ".cont"

    elif args.append:
        now = string.split(args.append, '.')[1]
        cont = ".cont"
        write_mode = "a"

    picklefilename = "results/result.%s.args" % now
    with open(picklefilename, "w") as pfile:
        pdict = dict(
                    now = now,
                    args = args
                    )
        pickle.dump(pdict, pfile)

    output_filename = "results/result.%s" % now
    #mystatfilename = output_filename+cont+".stat"
    #statfilename = "result.%s%s.stat" % (now, cont)

    continue_from = getContinueFrom(output_filename+'.dat')

    output = open(output_filename+'.dat', write_mode)

    err_output = None
    if not args.errors:
        err_output_filename = output_filename+'.err'
        err_output = open(err_output_filename, "w")

    datasets = {'iris':["iris.scale"],
                 'dna':["dna.scale"],
                 'vowel':["vowel.scale"],
                 'protein':['protein'],
                 'mushrooms':['mushrooms'],
                 'letter':['letter.scale'],
                 'heart':['heart.scale'],
                 'breast':['breast.scale']
                 #"clinical+volumes.arff"
                 }

    java_heap = args.xmx

    maxsplits = args.splits

    pop_size_min = args.popmin or args.popmax
    pop_size_max = args.popmax
    pop_size_step = args.popstep

    generations_min = args.genmin or args.genmax
    generations_max = args.genmax
    generations_step = args.genstep
    
    
    evalthreads = args.evalthreads or "1"
    breedthreads = args.breedthreads or "1"
    
    cache_size = args.cache
    epsilon = args.epsilon
    shrinking = args.shrinking
    
    #cache_min = 50
    #cache_max = 300
    #cache_step = 50
    #cashe_sizes = range(cache_min, cache_max, cache_step)    
    cashe_sizes = (cache_size,)
    
#    epsilon_min = 0.001
#    epsilon_max = 0.501
#    epsilon_step = 0.01
    #epsilons = (0.001, 0.005, 0.01, 0.1, 0.2, 0.5)
    #epsilons = (0.4,)    
    epsilons = (epsilon,) 

    shrinkings = (1 if args.shrinking else 0,)


    def frange(start, stop, step):
        result = start
        while result <= stop:
            yield result
            result += step

    costs = [c for c in frange(args.costmin, args.costmax, args.coststep)]
    costs = (0.3,)
    

    cv_folds = 10
    cv = False
    
    ih = []
    for s in range(maxsplits):
        for g in range(generations_max):
            ih.append("fitness_{0}_{1} accuracy_{0}_{1}".format(s, g))
   
    iterative_headings = " ".join(ih)         
    
    if not cont:
        output.write("N dataset population_size generations cross_validation cv_folds epsilon cache shrinking cost %s mean_fitness mean_accuracy time\n"% iterative_headings)
    else:
        output.write("\n")

    i = 0
    for dataset_name in args.datasets or datasets.keys():
        dataset_list = datasets.get(dataset_name)
        dataset = dataset_list[0]
        dataset_path_list = ["data/"+d for d in dataset_list]
        if not dataset:
            continue
        shuffled_datasets = shuffle_datasets(dataset_path_list, maxsplits, valid=25, test=25,
                                             new=(args.newdata and not cont))
        j=0
        print "Running experiment for {0} dataset".format(dataset)
        for generations in range(generations_min, generations_max+1, generations_step):
            for pop in range(pop_size_min, pop_size_max+1, pop_size_step):
                for cache_size in cashe_sizes:
                    for epsilon in epsilons:
                        for shrinking in shrinkings:
                            for cost in costs:
                                i+=1
                                if not cont or i >= continue_from:
                                    j+=1
                                    print "\tgenerations:%d; Pop. size:%d; cache:%d, eps:%f, shrink:%s, cost:%f"%(generations,pop,cache_size,epsilon,shrinking,cost)
                                    output.write("%d "% i) #row number in output file
                                    output.write("%s " % dataset) #Dataset name
                                    output.write("%d " % pop) #population size
                                    output.write("%d " % generations) #number of generations
                                    output.write("%s " % cv) # was cross validation  used
                                    output.write("%d " % cv_folds) # number of cross validation folds
                                    output.write("%f " % epsilon) # 
                                    output.write("%d " % cache_size) # n
                                    output.write("%d " % shrinking) #
                                    output.write("%f " % cost) # 
                                     
                                    output.flush()
                
                                    interval = 0.0;
                
                                    mean_acc = 0.0
                                    mean_fit = 0.0
                                    mean_time = 0.0
                                    #splits = max(1, int((2.0/(generations+1))*len(shuffled_datasets)))
                                    splits = len(shuffled_datasets)
                                    for k, shuffled_dataset in enumerate(shuffled_datasets):
                                        if k >= splits:
                                            output.write("NaN ")
                                            output.write("NaN ")
                                            continue
                                        print "\t\tProcessing %s"%(shuffled_dataset)
                                        shuffled_dataset = os.path.join(os.getcwd(), shuffled_dataset)
                                        train = shuffled_dataset+".tr"
                                        train_test = shuffled_dataset+".trtst"
                                        test = shuffled_dataset+".t"
                                        validation = shuffled_dataset+".val"
                
                                        mystatfilename = '%s.%s.p-%d.g-%d.%d.stat'    % (output_filename,dataset,pop,generations,k)
                                        statfilename =   '%s.%s.p-%d.g-%d.%d.ecjstat' % (output_filename,dataset,pop,generations,k)
                
                
                                        args_list = [
                                             'java',
                                             '-classpath',
                                             r'bin:lib/ecj',
                                             '-Xmx%dm'%(java_heap),
                                             'ec.Evolve',
                                             '-file', 'src/ec/app/kernel_gp/kernel_gp.params',
                                             '-p', 'output-file=%s'% mystatfilename,
                                             '-p', 'stat.file=$%s'% statfilename,
                                             '-p', 'train-file=%s'% train,
                                             '-p', 'test-file=%s'% test,
                                             '-p', 'traintest-file=%s'% train_test,
                                             '-p', 'validation-file=%s'% validation,
                                             '-p', 'generations=%d'% generations,
                                             '-p', 'pop.subpop.0.size=%s'%pop,
                                             '-p', 'cross-validation=%s'%cv,
                                             '-p', 'cv-folds=%d' % cv_folds,
                                             '-p', 'cache_size=%d' % cache_size,
                                             '-p', 'shrinking=%d' % shrinking,
                                             '-p', 'epsilon=%f' % epsilon,
                                             '-p', 'cost=%f' % cost,
                                             '-p', 'breedthreads=%s' % breedthreads,
                                             '-p', 'evalthreads=%s' % evalthreads
                                             ]
                                            
    #                                    print "\n"+(" ".join(args_list))+"\n"
                                        start = time.time()
                    #                    subprocess.call(args_list, stdout=output, stderr=err_output)
                                        results = subprocess.check_output(args_list, stderr=err_output)
                                        end = time.time()
                                        interval = end - start
                                        results = results.split()
                                        for g in range(generations_max):
                                            if g < generations:
                                                try:
                                                    fitness = results.pop(0)
                                                    accuracy = results.pop(0)
                                                except:
                                                    fitness = 'nan'
                                                    accuracy = 'nan'
                                                output.write(fitness+" ")
                                                output.write(accuracy+" ")
                                            else:
                                                output.write("  ")
                                        
                                        output.flush()
                                        
                                        print "\t\tTime: %.03f s\taccuracy:%.03f" % (interval, float(accuracy))
                                        mean_fit += float(fitness)
                                        mean_acc += float(accuracy)
                                        mean_time += interval
                    #                    output.flush()
                                    mean_acc /= splits
                                    mean_fit /= splits
                                    mean_time /= splits
                                    print "\tAvg. time: %.03f s\tAvg. accuracy: %.03f" % (mean_time, mean_acc)
                                    output.write("%f " % mean_fit) # mean fitness
                                    output.write("%f " % mean_acc) # men accuracy
                                    output.write("%f\n" % mean_time) # execution time

    output.close()
    if not args.errors:
        err_output.close()
