#!/bin/bash

# maly skrypcik do konwersji dot i gviz'ow do PDFow. Moze sie przydac.

EXT=dot
for i in `find . -name "*.${EXT}"`; do
    NAME=`basename $i .${EXT}`
    DIR=`dirname $i`
    BASE=${DIR}/${NAME}

    if [ $i -nt ${BASE}.pdf ]; then
        echo "Processing ${EXT}: " $i
        dot -Tpdf -o ${BASE}.pdf $i
    else
        echo "Skipping ${EXT}: " $i
    fi

    if [ $i -nt ${BASE}.svg ]; then
        echo "Processing ${EXT}: " $i
        dot -Tsvg -o ${BASE}.svg $i
    else
        echo "Skipping ${EXT}: " $i
    fi

done

EXT=gviz
for i in `find . -name "*.${EXT}"`; do
    NAME=`basename $i .${EXT}`
    DIR=`dirname $i`
    BASE=${DIR}/${NAME}

    if [ $i -nt ${BASE}.pdf ]; then
        echo "Processing ${EXT}: " $i
        neato -s -n2 -Tpdf -o ${BASE}.pdf $i
    else
        echo "Skipping ${EXT}: " $i
    fi
done
