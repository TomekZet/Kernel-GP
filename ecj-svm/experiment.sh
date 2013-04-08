#!/bin/sh
# run with sbatch --exclusive -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 1024 -d "$1" --genmin 1 -g 4 --genstep 1 --popmin 50 -p 50 --popstep 50 -s 2