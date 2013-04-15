#-*- coding: utf-8 -*-
'''
Created on Sep 15, 2012

@author: tomek
'''

import argparse
import random
import re

import arff


def split(dataset, p_test=20, p_valid=20, p_train=20):
    ''' Splits dataset into train, test and validation datasets with proportional to percentages given as arguments
        arguments:
            dataset : list of examples
            p_test  : size of test set as percent of input dataset
            p_valid: size of validations set as percent of input dataset

        returns: tuple with train, test and validations datasets
    '''
    if p_test + p_valid + p_train> 100:
        raise Exception("Sumaric percent size of train, test and validation (%d) set must not be larger then 100%%" % (p_test+p_valid+p_train))
    n_total = len(dataset)
    n_test = int(p_test/100.0 * n_total)
    n_valid = int(p_valid/100.0 * n_total)
    n_train = int(p_train/100.0 * n_total)
    n_rest = n_total - n_test - n_valid - n_train
    if n_rest < 0:
        raise Exception("Train, test and validation sets are too big! Make at least one of them smaller")
    valid = dataset[:n_valid]
    train = dataset[n_valid:n_valid+n_train]
    test = dataset[n_valid+n_train:n_valid+n_train+n_test]
    train_test = dataset[n_valid:]
    return train, test, valid, train_test


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
        line = str(row[-1])+" "
        line += " ".join(["%d: %s"%(i+1, (v)) for i, v in enumerate(row[1:-1])])
        dataset.append(line)
    return dataset

def process(input, output, test=33, valid=33, train=34, rand=None, seed=False):
    '''
    input  : path to input file(s). If there are multiple files their content is joined into one big dataset
    output : path for output files
    test   : size of output test set in percents of input set
    valid  : size of output validation set in percents of input set
    rand   : if true then input dataset is randomized (Shuffled)
    seed   : if true then seed for randomization is set to current time (if false seed is not set so each time shuffles will be thesame)

    '''

    dataset = []
    for file in input:
        if file.endswith(".arff"):
            dataset.extend(load_from_arff(input))
        else:
            with open(file) as f:
                dataset.extend(f.readlines())

    if rand:
        if seed:
            random.seed()
        random.shuffle(dataset)

    train, test, valid, train_test = split(dataset, p_test=test, p_valid=valid, p_train=train)

    if train:
        write_dataset(train, output+".tr")
    if test:
        write_dataset(test, output+".t")
    if valid:
        write_dataset(valid, output+".val")
    if train_test:
        write_dataset(train_test, output+".trtst")


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
    parser.add_argument('-i', '--input', help='Path to input file(s)', nargs='+')
    parser.add_argument('-o', '--output', help='Path to output file', default='')
    parser.add_argument('-v', '--valid', help='Percent of cases assigned to validation dataset', type=int, default=20)
    parser.add_argument('-t', '--test', help='Percent of cases assigned to test dataset', type=int, default=20)
    parser.add_argument('-r', '--rand', help='Shuffle set before dividing', action='store_true')
    parser.add_argument('-s', '--seed', help='Generete seed for randomizing', action='store_true')
    parser.add_argument('-m', '--multiple', help='Make multple splits, each with other seed', type=int, default=0)

    args = parser.parse_args()
    if not args.output:
        args.output = args.input

    if args.multiple > 0:
        shuffle_datasets(args.input, splits = args.multiple, test=args.test, valid=args.valid)
    else:
        process(args.input, args.output, args.test, args.valid, args.rand, args.seed)