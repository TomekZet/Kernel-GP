#!/bin/sh
sbatch --exclusive -o slurm-$1.out -t 4:0:0 experiment.sh $1