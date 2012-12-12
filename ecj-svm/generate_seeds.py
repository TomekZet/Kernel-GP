#!/usr/bin/python
import pyDataSet
import os
import argparse

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Join datasets and splits them into train, test and validations datasets")
    parser.add_argument('-d', '--dir', help='Path to input dir', default=os.getcwd())
    parser.add_argument('-p', '--pwd', action='store_true')

    args = parser.parse_args()

    print os.getcwd()

    for root, dirs, files in os.walk(args.dir):
       if root != args.dir:
         files = [os.path.join(root,f) for f in files]
         print "pyDataSet.shuffle_datasets(",files,", splits=5, test=25, valid=25, new=True)"
         print "\n"
         pyDataSet.shuffle_datasets(files, output_name=os.path.join(root, os.path.split(root)[1]), splits=5, test=25, valid=25, new=True)