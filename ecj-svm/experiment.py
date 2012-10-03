import subprocess
import argparse
import time
import datetime

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


if __name__ == "__main__":
    
    parser = argparse.ArgumentParser(description="Experiment runner")
    parser.add_argument('-x', '--xmx', help='Java heap size', type=int, default=1024)
    parser.add_argument('-g', '--generations', help='Max number of generations', type=int, default=5)
    parser.add_argument('-p', '--popmax', help='Maximum size of population', type=int, default=101)
    
#    parser.add_argument('-r', '--rand', help='Randomize set before dividing', action='store_true')
#    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')
    
    args = parser.parse_args()
    
    
    datasets = [#"iris.scale",
                 "dna.scale",
                 "vowel.scale",
                 #"clinical+volumes.arff"
                 ]
    
    java_heap = args.xmx
    
    pop_size_min = 1
    pop_size_max = args.popmax
    pop_size_step = 20

    generations = args.generations
    cv_folds = 10
    cv = False
    
    output_filename = "results/results.%s.dat" % (datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"))
    statfilename = output_filename+".stat"
    err_output_filename = output_filename+'.err'
    err_output = open(err_output_filename, "w")   
    
    output = open(output_filename, "w")   
    output.write("""'dataset' 'population size' 'n.o.generations' 'cros validation' 'cv folds' 'fitness' 'accuracy' 'time'\n""")
    output.flush()
       
    for dataset in datasets:
        print "Running experiment for {0} dataset".format(dataset)
        train = "data/"+dataset+".tr"
        test = "data/"+dataset+".t"
        validation = "data/"+dataset+".v"
        for i in range(pop_size_min, pop_size_max+1, pop_size_step):
            print "Population size:%d"%i
            args_list = [
                 'java',
                 '-classpath',
                 r'bin:lib/ecj',
                 '-Xmx%dm'%(java_heap),
                 'ec.Evolve',
                 '-file', 'src/ec/app/kernel_gp/kernel_gp.params',
                 '-p', 'output-file=%s'% statfilename,
                 '-p', 'train-file=%s'% train,
                 '-p', 'test-file=%s'% test,
                 '-p', 'validation-file=%s'% validation,
                 '-p', 'generations=%d'% generations,
                 '-p', 'pop.subpop.0.size=%s'%i,
                 '-p', 'cross-validation=%s'%cv,
                 '-p', 'cv-folds=%d' % cv_folds
                 ]
            output.write("%s " % dataset) #Dataset name
            output.write("%d " % i) #population size
            output.write("%d " % generations) #number of generations
            output.write("%s " % cv) # was cross validation  used
            output.write("%d " % cv_folds) # number of cross validation folds
            output.flush()
            
            start = time.time()
            subprocess.call(args_list, stdout=output, stderr=err_output)
            end = time.time()
            interval = end - start
            print "Time: %.03f seconds" % interval
            output.flush()
            output.write("%f\n" % interval) # execution time
    
    output.close()
    err_output.close()
            