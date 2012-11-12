#!/bin/sh
#SBATCH --time=1
# run with sbatch --exclusive -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
#python experiment.py -x 4000 -c 'results/result.2012-11-11 00:47:56.args'
python experiment.py -x 4000 -d $1 -g 5 --genstep 2 -p 101 --popstep 10 -n