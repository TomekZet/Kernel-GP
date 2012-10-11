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

    args = parser.parse_args()
    
    c = args.column
    headers = ''
    groups = {}
    with open(args.input, 'r') as f:
        headers = string.split(f.readline())
        for line in f:
           line = string.split(line)
           groups.setdefault(line[c], []).append(line)
               
    for val, group in groups.items():
        with open(args.input+'.'+headers[c]+'-'+val+'.dat', 'w') as out:
            out.write(' '.join(headers)+'\n')
            for i, line in enumerate(group):
                out.write(str(i)+' '+' '.join(line[1:])+'\n')
        
    
    
    