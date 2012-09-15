#-*- coding: utf-8 -*-
'''
Created on Sep 15, 2012

@author: tomek
'''

import argparse
import random
import re

import arff
        
        
def divide(dataset, p_test=20, p_valid=20):
    if p_test + p_valid > 100:
        raise Exception("Sumaric percent size of test and validation %d set must not be larger then 100%"%(p_test+p_valid))
    n_total = len(dataset)
    n_test = int(p_test/100.0 * n_total)
    n_valid = int(p_valid/100.0 * n_total)
    n_train = n_total - n_test - n_valid
    if n_train < 0:
        raise Exception("Test and validation sets are too big!")
    train = dataset[:n_train]
    test = dataset[n_train:n_train+n_test]
    valid = dataset[n_train+n_test:]
    return train, test, valid

def write_dataset(dataset, path):
    with open(path, "w") as f:
        for line in dataset:
            f.write(line)
            f.write("\n")
            
def substitute_missing(data):
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
    a = arff.ArffFile.load(filepath)
    dataset = []
    substitute_missing(a.data)
    for row in a.data:
        line = str(row[-1])+" "
        line += " ".join(["%d: %s"%(i+1, (v)) for i, v in enumerate(row[1:-1])])    
        dataset.append(line)
    return dataset
        
    
if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Preprocessing of ML data sets")
    parser.add_argument('-i', '--input', help='Path to input file')
    parser.add_argument('-o', '--output', help='Path to output file', default='')
    parser.add_argument('-v', '--valid', help='Percent of cases assigned to validation dataset', type=int, default=20)
    parser.add_argument('-t', '--test', help='Percent of cases assigned to test dataset', type=int, default=20)
    parser.add_argument('-r', '--rand', help='Randomize set before dividing', action='store_true')
    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')
    
    args = parser.parse_args()
    if not args.output:
        args.output = args.input

    if args.input.endswith(".arff"):
        dataset = load_from_arff(args.input)        
    else:
        with open(args.input) as f: 
            dataset = f.readlines()
        
    if args.rand:
        if args.seed:
            random.seed()
        random.shuffle(dataset)
    
    train, test, valid = divide(dataset, p_test = args.test, p_valid = args.valid)
    
    if train:
        write_dataset(train, args.output+".tr")
    if test:
        write_dataset(test, args.output+".t")
    if valid:
        write_dataset(valid, args.output+".val")