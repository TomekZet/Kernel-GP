#!/bin/sh
#SBATCH --time=1
srun -o slurm-srun.out -t 4:0:0 python experiment.py -x 4000 -g 6 -p 101
