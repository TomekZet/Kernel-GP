#!/bin/sh
#SBATCH --time=1
# run with sbatch -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 4000 -d protein --popmin 11 --popmax 11 --genmin 5 --genmax 5
#python experiment.py -c "results/result.2012-11-08 22:20:46.args"