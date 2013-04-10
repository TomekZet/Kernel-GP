#!/bin/sh
sbatch --exclusive -o slurm-"$2".out -t 4:0:0 experiment-cont.sh "$1"