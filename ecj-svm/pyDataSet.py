#!/usr/bin/python
#-*- coding: utf-8 -*-
'''
Created on Sep 15, 2012

@author: tomek
'''

import argparse
import random
import re
import os

import arff


def split(dataset, n_train=None, n_test=None, p_test=25, p_valid=25, p_train=50, percentvalidoftrain=33):
    ''' Splits dataset into train, test and validation datasets with proportional to percentages given as arguments
        arguments:
            dataset : list of examples
            p_test  : size of test set as percent of input dataset
            p_valid: size of validations set as percent of input dataset

        returns: tuple with train, test and validations datasets
    '''

    n_train, n_test, n_valid = _get_sets_sizes(dataset, n_train=n_train, n_test=n_test, percentvalidoftrain=percentvalidoftrain,
                                               p_test=p_test, p_valid=p_valid, p_train=p_train)
    
    test = dataset[:n_test]
    train = dataset[n_test:n_test+n_train]
    valid = dataset[n_test+n_train:n_valid+n_train+n_test]
    train_val = dataset[n_test:]
    
    return train, test, valid, train_val


def _get_numbers_from_percents(dataset, n_total, p_train=50, p_test=25, p_valid=25):
    if p_test + p_valid + p_train> 100:
        raise Exception("Sumaric percent size of train, test and validation (%d) set must not be larger then 100%%" % (p_test+p_valid+p_train))
    n_test = int(p_test/100.0 * n_total)
    n_valid = int(p_valid/100.0 * n_total)
    n_train = int(p_train/100.0 * n_total)

    return n_train, n_test, n_valid


def _get_sets_sizes(dataset, n_train=None, n_test=None, p_test=25, p_valid=25, p_train=50, percentvalidoftrain=33):
    
    #if n_train is None and (n_test is None or n_valid is None) or n_test is None and n_valid is None:    
    
    n_total = len(dataset)
    if n_train is None and n_test is None:
        n_train, n_test, n_valid = _get_numbers_from_percents(dataset, n_total, p_train=p_train, p_test=p_test, p_valid=p_valid)
    else:
        if n_train is None:
            n_train = n_total - n_test
        
        if n_test is None:
            n_test = n_total - n_train
    
    n_valid = int(round(percentvalidoftrain/100.0*n_train))
    n_train = n_train - n_valid
  
    
    n_rest = n_total - (n_test or 0) - (n_valid or 0) - (n_train or 0)
    if n_rest < 0:
        raise Exception("Train, test and validation sets are too big! Make at least one of them smaller")
    elif n_rest > 0:
        if n_train is None:
            n_train = n_rest
        elif n_test is None:
            n_test = n_rest
        elif n_valid is None:
            n_valid = n_rest
        else:
            n_train += n_rest
            
    return n_train, n_test, n_valid    
    

def write_dataset(dataset, path):
    '''Write dataset to file in path'''
    with open(path, "w") as f:
        for line in dataset:
            f.write(line)


def substitute_missing(data):
    '''Replaces missing values in data with means (for numerical values) or mods (for nominal values)'''
    means = []
    modes = []
    types = []
    missing = []
    for i, row in enumerate(data):
        for j, v in enumerate(row):
            if i == 0:
                means.append([0.0,0])
                modes.append([{"count":0,"mode":None},{}])
                types.append(None)
            if type(v) in (float, int):
                types[j] = type(v)
                means[j][0] += v
                means[j][1] += 1
            elif v in ('?', float("NaN"), "NaN"):
                missing.append((i, j))
            elif type(v) == str:
                types[j] = type(v)
                modes[j][1].setdefault(v, 0)
                modes[j][1][v] += 1
                if modes[j][1][v] > modes[j][0]['count']:
                    modes[j][0]['mode'] = v
            else:
                raise Exception("Unexpected symbol: \""+str(v)+"\"in arff data")

    for row, col in missing:
        if types[col] == str:
            data[row][col] = modes[j][0]['mode']
        else:
            data[row][col] = means[col][0]/means[col][1] if means[col][1]>0 else 0


def load_from_arff(filepath):
    ''' Load dataset from arff file '''
    a = arff.ArffFile.load(filepath)   
    dataset = []
    substitute_missing(a.data)
    for row in a.data:
        class_ = str(row[-1])
        if class_.lower() == "true":
            class_ = "1"
        elif class_.lower() == "false":
            class_ = "-1"
        line =  class_+" "
        line += " ".join(["%d: %s"%(i+1, (v)) for i, v in enumerate(row[1:-1])])
        dataset.append(line+'\n')
    return dataset



def fold(dataset, output_filename, folds=10, percentvalidoftrain=33):
    ''' Splits dataset into train, test and validation datasets with proportional to percentages given as arguments
        arguments:
            dataset : list of examples
            p_test  : size of test set as percent of input dataset
            p_valid: size of validations set as percent of input dataset

        returns: tuple with train, test and validations datasets
    '''

    fold_size = len(dataset) / folds
    rest = len(dataset) % folds
    fold_sizes = [fold_size + (1 if f < rest else 0) for f in range(folds)]
    fold_points = [sum(fold_sizes[:i]) for i in range(len(fold_sizes)+1)]
    
    output_filenames = []
    
    for f in range(len(fold_sizes)):              
    
        test = dataset[fold_points[f]:fold_points[f+1]]
        train = dataset[:fold_points[f]] or []
        train.extend(dataset[fold_points[f+1]:] or [])          
    
        n_valid = int(round(percentvalidoftrain/100.0*len(train)))
        train_val = train
        valid = train[:n_valid]
        train = train[n_valid:]
#        print "\nFold {0}:".format(f)
#        print train_val
#        print train
#        print valid
#        print test
#        print "\n"
        
        output_filename_fold = "{output}.fold{fold}".format(output=output_filename, fold=f)        
        write_datasets(output_filename_fold, train, test, valid, train_val);                   
        output_filenames.append(output_filename_fold)
    
    return output_filenames


def process(input, output, p_test=33, p_valid=33, p_train=34, n_test=None, n_train=None,
            percentvalidoftrain=33, rand=None, seed=False, folds=0):
    '''
    input  : path to input file(s). If there are multiple files their content is joined into one big dataset
    output : path for output files
    test   : size of output test set in percents of input set
    valid  : size of output validation set in percents of input set
    rand   : if true then input dataset is randomized (Shuffled)
    seed   : if true then seed for randomization is set to current time (if false seed is not set so each time shuffles will be thesame)
    folds  : number of folds for generating data for n-fold cross-validation
    '''

    dataset = []
    for file in input:
        if file.endswith(".arff"):
            dataset.extend(load_from_arff(file))
        else:
            with open(file) as f:
                dataset.extend(f.readlines())

    if rand:
        if seed:
            random.seed()
        random.shuffle(dataset)
    
    if folds == 0:
        train, test, valid, train_val = split(dataset, p_test=p_test, p_valid=p_valid, p_train=p_train,
                                               n_test=n_test, n_train=n_train, percentvalidoftrain=percentvalidoftrain)
        
        write_datasets(output, train, test, valid, train_val);
    
    else:
        return fold(dataset, output, folds, percentvalidoftrain)


def write_datasets(output_file, train=None, test=None, valid=None, train_val=None):
    if train:
        write_dataset(train, output_file+".tr")
    if test:
        write_dataset(test, output_file+".t")
    if valid:
        write_dataset(valid, output_file+".val")
    if train_val:
        write_dataset(train_val, output_file+".trval")


def shuffle_datasets(dataset_path_list, output_name="", splits=10, test=22, valid=33, new=True):
    '''
    Shuffles datasets from 'dataset_path_list' into 'splits' tuples of train and test datasets
    Result datasets are written into files
    Filenames are made by appending original dataset filenames with 'seed'+number of shuffle
    '''
    if not output_name:
        output_name = dataset_path_list[0]
    out_filenames = []
    for seed in range(splits):
        dataset_out = '%s.seed%d' % (output_name, seed)
        out_filenames.append(dataset_out)
        if new:
            process(dataset_path_list, dataset_out, test=test, valid=valid, rand=True, seed=False)
    return out_filenames


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Join datasets and splits them into train, test and validations datasets")
    parser.add_argument('-i', '--input', help='Path to input file(s)', nargs='+', default=[None])
    parser.add_argument('-o', '--output', help='Path to output file', default=None)
    parser.add_argument('-d', '--input_dir', help='Path to input directory', default=None)
    parser.add_argument('-p', '--outpath', help='Path to output directory', default='')
    parser.add_argument('-v', '--valid', help='Percent of cases assigned to validation dataset', type=int, default=20)
    parser.add_argument('-t', '--test', help='Percent of cases assigned to test dataset', type=int, default=20)
    parser.add_argument('-T', '--numbertest', help='number of test examples', type=int, default=None)
    parser.add_argument('-R', '--numbertrain', help='number of train examples', type=int, default=None)
    parser.add_argument('-P', '--percentvalidoftrain', help='If number of validation examples is not given, compute it as a percent of number of train examples ', type=int, default=33)
    parser.add_argument('-r', '--rand', help='Shuffle set before dividing', action='store_true')
    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')
    parser.add_argument('-m', '--multiple', help='Make multple splits, each with other seed', type=int, default=0)

    args = parser.parse_args()
    
    if args.input_dir:
        input_files = os.listdir(args.input_dir)
    else:
        input_files = args.input
     
    
    for f in input_files:    
        input = os.path.join(args.input_dir, f)
        output =  os.path.join(args.input_dir, args.outpath, f)
        print "Processing {0}".format(input)
        if args.multiple > 0:
            shuffle_datasets([input], splits = args.multiple, test=args.test, valid=args.valid)
        else:
            process([input], output, p_test=args.test, p_valid=args.valid, 
                    n_test=args.numbertest, n_train=args.numbertrain,
                    percentvalidoftrain=args.percentvalidoftrain, rand=args.rand, seed=args.seed)
        
        
        