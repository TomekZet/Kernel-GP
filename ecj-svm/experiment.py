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
import math
import pyDataSet

'''
This script computes various benchmarks:
- accuracy(population size)
- time(population size)
- accuracy(data set)
'''


def sd(data, mean):
    return math.sqrt(sum([math.pow(x - mean, 2) for x in data])/(len(data) ))


def mean(values):    
    if not values or len(values) <= 0:
        return float('nan')
    sum = 0.0
    count = 0
    for v in values:
        if v and not math.isnan(v):
            sum += v
            count += 1
    if count == 0:
        return float('nan')
    return sum / count
        


def shuffle_datasets(dataset_path_list, splits=10, p_test=25, p_valid=25, p_train=25,
                     n_test=None, n_train=None, new=True, percentvalidoftrain=33,cv=False):
    '''
    Shuffles datasets from 'dataset_path_list' into 'splits' tuples of train and test datasets
    Result datasets are written into files
    Filenames are made by appending original dataset filenames with 'seed'+number of shuffle
    '''        
    out_filenames = []
    
    if cv:
        if new:
            out_filenames = pyDataSet.process(dataset_path_list, dataset_path_list[0], 
                                          rand=True, seed=False, folds=splits,                            
                                          percentvalidoftrain=percentvalidoftrain)
        else:
            for fold in range(splits):
                dataset_out = '%s.fold%d' % (dataset_path_list[0], fold)
                out_filenames.append(dataset_out)
    else:        
        for seed in range(splits):
            dataset_out = '%s.seed%d' % (dataset_path_list[0], seed)
            out_filenames.append(dataset_out)
            if new:
                pyDataSet.process(dataset_path_list, dataset_out, rand=True, seed=False,
                                  p_test=p_test, p_valid=p_valid, p_train=p_train, 
                                  n_test=n_test, n_train=n_train,
                                  percentvalidoftrain=percentvalidoftrain)
    
    if len(out_filenames) == 0:
        return dataset_path_list
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
    parser.add_argument('-g', '--genmax', help='Max number of generations', type=int, default=50)
    parser.add_argument('-p', '--popmax', help='Maximum size of population', type=int, default=101)
    parser.add_argument('--popmin', help='Minimum size of population', type=int, default=None)
    parser.add_argument('--popstep', help='Step of population size incrementation', type=int, default=10)
    parser.add_argument('--costmin', help='Minimum cost', type=float, default=0.01)
    parser.add_argument('--costmax', help='Maximum cost', type=float, default=0.3)
    parser.add_argument('--coststep', help='Cost step', type=float, default=0.25)    
    parser.add_argument('-d', '--datasets', help='Names of datasets to be used', nargs='+',
        choices=['iris', 'dna', 'vowel', 'protein', 'mushrooms', 'letter', 'heart', 'breast', 'ion',
                'pima', 'liver', 'dummy', 'adhd1', 'adhd2', 'adhd3', 'adhd4', 'adhd5'])     
    parser.add_argument('-e', '--errors', help='Show errors on stdout (do not write them to file', action='store_true')
    parser.add_argument('-n', '--newdata', help='Generate new data splits', action='store_true')
    parser.add_argument('-c', '--cont', help='Path to file with stopped computations to continue')
    parser.add_argument('-a', '--append', help='Path to file with stopped computations to append, without reading args from pickled file')
    parser.add_argument('--cache', help='SVM Cache size (MB)', type=int, default=100)
    parser.add_argument('--shrinking', help='Wheter to use shrinking in SVM or not', type=bool, default=False)
    parser.add_argument('--epsilon', help='Value of epsilon parameter pased to SVM', type=float, default=0.5)
    parser.add_argument('--evalthreads', help='Number of threads used to evaluate population', type=str, default="1")
    parser.add_argument('--breedthreads', help='Number of threads used to breed population', type=str, default="1")
    parser.add_argument('-v', '--percentvalid', help='Percent of validation examples', type=int, default=25)
    parser.add_argument('-t', '--percenttest', help='Percent of test examples', type=int, default=25)
    parser.add_argument('-r', '--percenttrain', help='Percent of train examples', type=int, default=50)
    parser.add_argument('-T', '--numbertest', help='number of test examples', type=int, default=[None], nargs='+')
    parser.add_argument('-R', '--numbertrain', help='number of train examples', type=int, default=[None], nargs='+')
    parser.add_argument('-P', '--percentvalidoftrain', help='If number of validation examples is not given, compute it as a percent of number of train examples ', type=int, default=33)
    parser.add_argument('-f', '--fitness', help='Measure used as fitness: f1, mcc or accuracy', nargs='+', 
                        choices=['accuracy', 'f1', 'mcc', 'probability'], 
                        default=['probability', 'accuracy', 'f1', 'mcc'])
    parser.add_argument('-k', '--kernels', help='Kernels used. "gpkrnel" is a kernel generated by GP alorithm.', nargs='+', 
                        choices=['gpkernel', 'linear', 'poly', 'rbf', 'sigmoid'], 
                        default=['gpkernel'])    
    parser.add_argument('--probability', help='Wheter to use probability outputs to calculate fitness or not', type=bool, default=False)
    parser.add_argument('-l', '--disable_logging', help='Wheter to disable logging of every kernel in ECJ', action='store_true')
    parser.add_argument('--command', help='Print java ecj command', action='store_true')
    parser.add_argument('--elite', help='Size of elite', type=int, default=1)
    parser.add_argument('--cv', help='Whether to use Cross Validation in validation stage (evaluation)', action='store_true')
    parser.add_argument('--cvfolds', help='Number of folds in Cross Validation', type=int, default=5)
    parser.add_argument('--cvtest', help='Whether to use Cross Validation in validation stage (evaluation)', action='store_true')
   


#    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')

    time_format = "%Y-%m-%d %H-%M-%S"
    now = datetime.datetime.now().strftime(time_format)
    args = parser.parse_args()
    write_mode = "w"
    cont = ""
    WORK_DIR = os.path.abspath(os.path.dirname(__file__))
    
    
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
    #picklefilename = os.path.join(WORK_DIR, picklefilename)
    
    with open(picklefilename, "w") as pfile:
        pdict = dict(
                    now = now,
                    args = args
                    )
        pickle.dump(pdict, pfile)

    output_filename = "results/result.%s" % now
    #output_filename = os.path.join(WORK_DIR, output_filename)
    
    #mystatfilename = output_filename+cont+".stat"
    #statfilename = "result.%s%s.stat" % (now, cont)

    continue_from = getContinueFrom(output_filename+'.dat')

    output = open(output_filename+'.dat', write_mode)

    err_output = None
    if not args.errors:
        err_output_filename = output_filename+'.err'
        err_output = open(err_output_filename, "w")

    datasets = {
                 'iris':["iris.scale"],
                 'dna':["dna.scale"],
                 'vowel':["vowel.scale"],
                 'protein':['protein'],
                 'mushrooms':['mushrooms'],
                 'letter':['letter.scale'],
                 'heart':['heart.scale'],
                 'breast':['breast.scale'],
                 'ion':['ionosphere_scale'],
                 'pima':['pima-diabetes_scale'],
                 'liver':['liver-disorders_scale'],
                 'dummy':['dummy.dat'],
                 'adhd1':['cse+s.volumes.incomplete.arff'],
                 'adhd2':['cse+s.volumes.preprocessing.all.arff'],
                 'adhd3':['cs+f.balls.mean.corr.distance.pca.arff'],
                 'adhd4':['cs+s.dd.dartel.all.30x36x30.20000.pca.arff'],
                 'adhd5':['cs+s.dd.dartel.all.48x58x48.10000.pca.arff']
                 #"clinical+volumes.arff"
                 }
    
    datasets_splits = {
                 'heart':{"train":180, "test":90},
                 'breast':{"train":409, "test":274},
                 'ion':{"train":234, "test":117},
                 'pima':{"train":512, "test":256},
                 'liver':{"train":230, "test":115}
                 }

    java_heap = args.xmx

    disable_logging = args.disable_logging
    maxsplits = args.splits


    generations = args.genmax
    
    evalthreads = args.evalthreads or "1"
    breedthreads = args.breedthreads or "1"
    
    fitness_measures = args.fitness or ["accuracy"]

    kernels = args.kernels or ["gpkernel", "linear", "poly", "sigmoid", "rbf"]
    
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

    use_probability = (1 if (args.probability or ("probability" in fitness_measures)) else 0,)

    def frange(start, stop, step):
        result = start
        while result <= stop:
            yield result
            result += step
            
    def mrange(start, stop, multiplier):
        result = start
        while result <= stop:
            yield result
            result *= multiplier

    #costs = [c for c in frange(args.costmin, args.costmax, args.coststep)]
    #costs = [c for c in mrange(args.costmin, args.costmax, 2)]
    costs = (args.costmax,)
    

    cv_folds = args.cvfolds
    cv = args.cv
    
    ih = []
    for s in range(maxsplits):
        ih.append("fitness_{0} accuracy_{0} f1_{0} mcc_{0} prob_{0} time_{0}".format(s))
   
    iterative_headings = " ".join(ih)         
    
    if not cont:
        output.write("N dataset kernel fitness_measure population_size generations cross_validation cv_folds epsilon cache shrinking cost %s\
 mean_fitness mean_accuracy mean_f1 mean_mcc mean_prob sd_fitness sd_accuracy sd_f1 sd_mcc sd_prob time sd_time\n"% iterative_headings)
    else:
        output.write("\n")

    i = 0
    
    data_n = -1
    
    #d = len(args.datasets or datasets.keys())
    #for arg in (args.numbertest, args.numbertrain):
            #while d > len(arg):
                #arg.append(arg[-1])
    
    for dataset_name in args.datasets or datasets.keys():
        data_n += 1
        
        dataset_list = datasets.get(dataset_name)
        dataset = dataset_list[0]
        #data_dir_path = os.path.join(WORK_DIR, "data/")
        dataset_path_list = [os.path.join("data", d) for d in dataset_list]
        if not dataset:
            continue
        
        ## Size of validation set can be computed automatically if not given manually, using percentvalidoftrain argument
        #if args.numbertest[data_n] is not None and args.numbertrain[data_n] is not None:
            #if args.numbervalid[data_n] is None:
                #args.numbervalid[data_n] = int(round(args.percentvalidoftrain/100.0*args.numbertrain[data_n]))
                #args.numbertrain[data_n] = args.numbertrain[data_n] - args.numbervalid[data_n]
                
        shuffled_datasets = shuffle_datasets(dataset_path_list, maxsplits, 
                                             p_test=args.percenttest,
                                             p_train=args.percenttrain,
                                             p_valid=args.percentvalid,
                                             #n_test=args.numbertest[data_n],
                                             #n_train=args.numbertrain[data_n],
                                             #n_valid=args.numbervalid[data_n],
                                             n_test=datasets_splits.get(dataset_name, {}).get("test",None),
                                             n_train=datasets_splits.get(dataset_name, {}).get("train", None),
                                             percentvalidoftrain=args.percentvalidoftrain,
                                             new=(args.newdata and not cont),
                                             cv=args.cvtest)
 
        print "Running experiment for {0} dataset".format(dataset)
        for kernel in kernels:
            if kernel == "gpkernel":
                fitness_measures_temp = fitness_measures        
                pop_size_min = args.popmin or args.popmax
                pop_size_max = args.popmax
                pop_size_step = args.popstep
                generations = args.genmax
                breed_elite = args.elite
            
            else:
                pop_size_min = 1
                pop_size_max = 1
                pop_size_step = 1
                fitness_measures_temp = ("accuracy",)
                generations = 1
                breed_elite = 1
    
            for fitness_measure in fitness_measures_temp:
                for pop in range(pop_size_min, pop_size_max+1, pop_size_step):
                    for cache_size in cashe_sizes:
                        for epsilon in epsilons:
                            for shrinking in shrinkings:
                                for cost in costs:
                                    if cont and i < continue_from:
                                        i+=generations
                                    if not cont or i >= continue_from:          
                                        interval = 0.0;
                    
                                        mean_fit = 0.0
                                        mean_acc = 0.0
                                        mean_f1 = 0.0
                                        mean_mcc = 0.0
                                        mean_time = 0.0
                                                    
                                        print "Kernel: %s; Pop.size:%d; cache:%d, eps:%f, shrink:%s, cost:%f"%(kernel, pop,cache_size,epsilon,shrinking,cost)
                                                    
                                        results_all_seeds = []
                                        #splits = max(1, int((2.0/(generations+1))*len(shuffled_datasets)))
                                        splits = len(shuffled_datasets)
                                        for k, shuffled_dataset in enumerate(shuffled_datasets):
                                            results_all_seeds.append([])
                                            if k >= splits:
                                                output.write("NaN ")
                                                output.write("NaN ")
                                                output.write("NaN ")                                        
                                                output.write("NaN ")
                                                continue
                                            print "\t\tProcessing %s"%(shuffled_dataset)
                                            shuffled_dataset = os.path.join(os.getcwd(), shuffled_dataset)
                                            train = shuffled_dataset+".tr"
                                            train_val = shuffled_dataset+".trval"
                                            test = shuffled_dataset+".t"
                                            validation = shuffled_dataset+".val"
                    
                                            mystatfilename = '%s.%s.p-%d.g-%d.%d.fit-%s.stat'    % (output_filename,dataset,pop,generations,k,fitness_measure)
                                            statfilename =   '%s.%s.p-%d.g-%d.%d.fit-%s.ecjstat' % (output_filename,dataset,pop,generations,k,fitness_measure)
                    
                    
                                            args_list = [
                                                    'java',
                                                    '-classpath', os.pathsep.join(('bin', os.path.join('lib', 'ecj'))),
                                                    '-Xmx%dm'%(java_heap),  
                                                     'ec.Evolve',
                                                    '-file', os.path.join('src', 'ec', 'app', 'kernel_gp', 'kernel_gp.params'),
                                                    '-p', 'output-file=%s'% mystatfilename,
                                                    '-p', 'stat.file=$%s'% statfilename,
                                                    '-p', 'train-file=%s'% train,
                                                    '-p', 'test-file=%s'% test,
                                                    '-p', 'trainval-file=%s'% train_val,
                                                    '-p', 'valid-file=%s'% validation,
                                                    '-p', 'generations=%d'% generations,
                                                    '-p', 'pop.subpop.0.size=%s'%pop,
                                                    '-p', 'cross-validation=%s'%cv,
                                                    '-p', 'cv-folds=%d' % cv_folds,
                                                    '-p', 'cache_size=%d' % cache_size,
                                                    '-p', 'shrinking=%d' % shrinking,
                                                    '-p', 'epsilon=%f' % epsilon,
                                                    '-p', 'cost=%f' % cost,
                                                    '-p', 'breedthreads=%s' % breedthreads,
                                                    '-p', 'evalthreads=%s' % evalthreads,
                                                    '-p', 'fitness_measure=%s' % fitness_measure,
                                                    '-p', 'probability=%s' % use_probability,
                                                    '-p', 'kernel=%s' % kernel,
                                                    '-p', 'breed.elite.0=%d' % breed_elite,
                                                    '-p', 'disable_logging=%s' % disable_logging
                                                    ]
                                                
                                            if args.command:
                                                print "\n"+(" ".join(args_list))+"\n"
                                            start = time.time()
                        #                    subprocess.call(args_list, stdout=output, stderr=err_output)
                                            results = subprocess.check_output(args_list, stderr=err_output)
                                            end = time.time()
                                            interval = end - start
                                            results  = results.split()
                                            for g in range(generations):
                                                try:
                                                    fitness = results.pop(0)
                                                except:
                                                    fitness = 'nan'

                                                try:
                                                    accuracy = results.pop(0)
                                                except:
                                                    accuracy = 'nan'
                                                
                                                try:
                                                    f1 = results.pop(0)
                                                except:
                                                    f1 = 'nan'
                                                
                                                try:
                                                    mcc = results.pop(0)
                                                except:
                                                    mcc = 'nan'
                                                
                                                try:
                                                    probability = results.pop(0)                                            
                                                except:
                                                    probability = 'nan'
                                                    
                                                results_all_seeds[k].append({"accuracy":float(accuracy), 
                                                                             "fitness":float(fitness),
                                                                             "f1": float(f1),
                                                                             "mcc": float(mcc),
                                                                             "probability": float(probability),
                                                                             "time":interval})                                   
                                                                                          
                                            print "\t\tTime: %.03f s\t%s:%.03f" % (
                                                   interval, fitness_measure, float(vars()[fitness_measure]))             
                                            
                                        for g in range(generations):
                                            output.write("%d "% i) #row number in output file
                                            output.write("%s " % dataset) #Dataset name
                                            output.write("%s " % kernel) #kernel
                                            output.write("%s " % fitness_measure) #fitness_measure
                                            output.write("%d " % pop) #population size
                                            output.write("%d " % (g+1)) #number of generations
                                            output.write("%s " % cv) # was cross validation  used
                                            output.write("%d " % cv_folds) # number of cross validation folds
                                            output.write("%f " % epsilon) # 
                                            output.write("%d " % cache_size) # n
                                            output.write("%d " % shrinking) #
                                            output.write("%f " % cost) # 
                                            
                                            fits = []
                                            accs = []
                                            f1s = []
                                            mccs = []       
                                            probabilities = []
                                            times = []
                                            
                                            for no_seed in range(splits):
                                                output.write("%f " % results_all_seeds[no_seed][g]["fitness"])
                                                output.write("%f " % results_all_seeds[no_seed][g]["accuracy"])
                                                output.write("%f " % results_all_seeds[no_seed][g]["f1"])                                                                                                                                                                                                                                                                   
                                                output.write("%f " % results_all_seeds[no_seed][g]["mcc"])
                                                output.write("%f " % results_all_seeds[no_seed][g]["probability"])
                                                output.write("%f " % results_all_seeds[no_seed][g]["time"])         
                                                                                                                                                                                                                                                                                                                   
                                                fits.append(results_all_seeds[no_seed][g]["fitness"])
                                                accs.append(results_all_seeds[no_seed][g]["accuracy"])
                                                f1s.append(results_all_seeds[no_seed][g]["f1"])
                                                mccs.append(results_all_seeds[no_seed][g]["mcc"])
                                                probabilities.append(results_all_seeds[no_seed][g]["probability"])
                                                times.append(results_all_seeds[no_seed][g]["time"])
                                                
                                            mean_fit  = mean(fits)
                                            mean_acc = mean(accs)
                                            mean_f1 = mean(f1s)
                                            mean_mcc = mean(mccs)
                                            mean_probability = mean(probabilities)
                                            mean_time = mean(times)
                                            
                                            sd_fit  = sd(fits, mean_fit)
                                            sd_acc = sd(accs, mean_acc)
                                            sd_f1 = sd(f1s, mean_f1)
                                            sd_mcc = sd(mccs,mean_mcc)
                                            sd_probability = sd(probabilities, mean_probability)
                                            sd_time = sd(times, mean_time)
                                            
                                            output.write("%f " % (mean_fit)) # mean fitness
                                            output.write("%f " % (mean_acc)) # mean accuracy
                                            output.write("%f " % (mean_f1)) # mean f1
                                            output.write("%f " % (mean_mcc)) # mean mcc
                                            output.write("%f " % (mean_probability)) # mean mcc
                                            
                                            output.write("%f " % (sd_fit)) # sd fitness
                                            output.write("%f " % (sd_acc)) # sd accuracy
                                            output.write("%f " % (sd_f1)) # sd f1
                                            output.write("%f " % (sd_mcc)) # sd mcc
                                            output.write("%f " % (sd_probability)) # sd mcc
                                            
                                            output.write("%f " % mean_time) # execution time
                                            output.write("%f\n" % sd_time) # execution time
                                            output.flush()
                                            i+=1
                                        
                                        print "\tAvg.time: %.03f s\tAvg.accuracy: %.03f\tAvg.f1: %.03f\tAvg.mcc: %.03f\tAvg.prob.: %.03f" % (
                                                mean_time, mean_acc, mean_f1, mean_mcc, mean_probability)

    output.close()
    if not args.errors:
        err_output.close()
