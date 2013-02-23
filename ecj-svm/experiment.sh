#!/bin/sh
# run with sbatch --exclusive -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 1000 -d "$1" --genmin 1 -g 5 --genstep 2 --popmin 11 -p 11 --popstep 1 -s 5