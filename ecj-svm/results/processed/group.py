#-*- coding: utf-8 -*-
'''
Created on Sep 15, 2012

@author: tomek
'''
import argparse
import string

if __name__ == '__main__':
    
    parser = argparse.ArgumentParser(description='Splits datasets by grouping it according to data in column given as argument. Each group is written to a different file.')
    parser.add_argument('-i', '--input', help='Input file path')
    parser.add_argument('-c', '--column', help='Number of column to group by', type=int)
    parser.add_argument('-n', '--name', help='Name of column to group by (assumes that column names are unique', type=str)

    args = parser.parse_args()
    
    c = args.column
    headers = ''
    groups = {}
    with open(args.input, 'r') as f:
        headers = string.split(f.readline())
        headersDict = dict([(h,i) for i, h in enumerate(headers)])
        if args.name:
            c = headersDict.get(args.name, c)
        columns = len(headers)
        for line in f:
           line = string.split(line)
           #Some rows may be shorter due to smaller number of acuuracy and time measurments
           line[-3:-3] = ['NaN' for i in range(columns-len(line))]
           groups.setdefault(line[c], []).append(line)
               
    for val, group in groups.items():
        with open(args.input+'.'+headers[c]+'-'+val+'.dat', 'w') as out:
            out.write(' '.join(headers)+'\n')
            for i, line in enumerate(group):
                out.write(str(i)+' '+' '.join(line[1:])+'\n')
        
    
    
    