#!/bin/sh
# run with sbatch --exclusive -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 4000 -c "$1"