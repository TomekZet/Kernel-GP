#!/bin/sh
#SBATCH --time=1
srun -c 4 -o slurm.out python experiment.py -x 4000 -g 6 -p 101