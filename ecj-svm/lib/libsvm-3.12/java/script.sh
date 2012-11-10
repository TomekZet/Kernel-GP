#!/bin/bash

kernels=(linear polynomial rbf sigmoid)
for dataset in "protein" 
do
    echo "$dataset"
    for i in {0..3}
    do
        echo '  '${kernels[$i]}
        START=$(date +%s)
        #java -classpath libsvm.jar svm_train -q -s 0 -t $i ../../../data/$dataset.scale
        #java -classpath libsvm.jar svm_predict ../../../data/$dataset.scale.t $dataset.scale.model $dataset.scale.model.$i.out
        END=$(date +%s)
        DIFF=$(( $END - $START ))
        echo "   Time: $DIFF"
    done
done