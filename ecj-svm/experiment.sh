#!/bin/sh
#SBATCH --time=1
# run with sbatch -o slurm-sbatch.out -t 4:0:0 ./experiment.sh
python experiment.py -x 4000 -d dna --popmin 1 --popmax 101 --popstep 10 --genmin 1 --genmax 5 --genstep 2
#python experiment.py -c "results/result.2012-11-08 22:20:46.args"