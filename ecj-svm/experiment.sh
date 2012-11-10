#!/bin/sh
#SBATCH --time=1
# run with sbatch -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 4000 -d protein
#python experiment.py -c "results/result.2012-11-08 22:20:46.args"