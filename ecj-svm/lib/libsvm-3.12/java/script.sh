#!/bin/bash

kernels=(linear polynomial rbf sigmoid)
for dataset in "protein" 
do
    echo "$dataset"
    for i in {0..3}
    do
        echo '  '${kernels[$i]}
        for s in {0..9}
        do
            START=$(date +%s)
            java -classpath libsvm.jar svm_train -q -s 0 -t $i ../../../data/$dataset.seed$s.trtst $dataset.seed$s.$i.model
            java -classpath libsvm.jar svm_predict ../../../data/$dataset.seed$s.val $dataset.seed$s.$i.model $dataset.seed$s.$i.out
            END=$(date +%s)
            DIFF=$(( $END - $START ))
            echo "   Time: $DIFF"
        done
    done
done