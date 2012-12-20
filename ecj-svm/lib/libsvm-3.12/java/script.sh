#!/bin/bash

kernels=(linear polynomial rbf sigmoid)
for dataset in "$@"
do
    echo "$dataset"
    for i in {0..3}
    do
        echo '  '${kernels[$i]}
        for s in {0..4}
        do
            START=$(date +%s)
            #echo "java -classpath libsvm.jar svm_train -q -s 0 -t $i ../../../data/nowe/$dataset/$dataset.seed$s.trtst $dataset.seed$s.$i.model"
            echo "Started training"
            java -Xmx1300M -classpath libsvm.jar svm_train -q -s 0 -t $i ../../../data/nowe/$dataset/$dataset.seed$s.trtst $dataset.seed$s.$i.model
            #echo "java -classpath libsvm.jar svm_predict ../../../data/nowe/$dataset/$dataset.seed$s.val $dataset.seed$s.$i.model $dataset.seed$s.$i.out"
            echo "Training finished, started testing"
            java -Xmx1300M -classpath libsvm.jar svm_predict ../../../data/nowe/$dataset/$dataset.seed$s.val $dataset.seed$s.$i.model $dataset.seed$s.$i.out

            END=$(date +%s)
            DIFF=$(( $END - $START ))
            echo "   Time: $DIFF"
        done
    done
done
