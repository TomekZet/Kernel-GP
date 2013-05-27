#!/usr/bin/gnuplot
#
#    
#       G N U P L O T
#       Version 4.4 patchlevel 3
#       last modified March 2011
#       System: MS-Windows 32 bit 
#    
#       Copyright (C) 1986-1993, 1998, 2004, 2007-2010
#       Thomas Williams, Colin Kelley and many others
#    
#       gnuplot home:     http://www.gnuplot.info
#       faq, bugs, etc:   type "help seeking-assistance"
#       immediate help:   type "help"
#       plot window:      hit 'h'
# set terminal wxt 0
# set output
#unset clip points
#set clip one
#unset clip two
#set bar 1.000000 front
#set border 31 front linetype -1 linewidth 1.000
#set xdata
#set ydata
#set zdata
#set x2data
#set y2data
#set timefmt x "%d/%m/%y,%H:%M"
#set timefmt y "%d/%m/%y,%H:%M"
#set timefmt z "%d/%m/%y,%H:%M"
#set timefmt x2 "%d/%m/%y,%H:%M"
#set timefmt y2 "%d/%m/%y,%H:%M"
#set timefmt cb "%d/%m/%y,%H:%M"
#set boxwidth
#set style fill  empty border
#set style rectangle back fc lt -3 fillstyle   solid 1.00 border lt -1
#set style circle radius graph 0.02, first 0, 0 
#set dummy x,y
#set format x "% g"
#set format y "% g"
#set format x2 "% g"
#set format y2 "% g"
#set format z "% g"
#set format cb "% g"
#set angles radians
#unset grid
#set key title ""
#set key inside right top vertical Right noreverse enhanced autotitles nobox
#set key noinvert samplen 4 spacing 1 width 0 height 0 
#set key maxcolumns 0 maxrows 0
#unset label
#unset arrow
#set style increment default
#unset style line
#unset style arrow
#set style histogram clustered gap 2 title  offset character 0, 0, 0
#unset logscale
#set offsets 0.2, 0.2, 0.2, 0.2
#set pointsize 1
#set pointintervalbox 1
#set encoding default
#unset polar
#unset parametric
#unset decimalsign
#set view 60, 30, 1, 1
#set samples 100, 100
#set isosamples 10, 10
#set surface
#unset contour
#set clabel '%8.3g'
#set mapping cartesian
#set datafile separator whitespace
#unset hidden3d
#set cntrparam order 4
#set cntrparam linear
#set cntrparam levels auto 5
#set cntrparam points 5
#set size ratio 0 1,1
#set origin 0,0
#set style data points
#set style function lines
#set xzeroaxis linetype -2 linewidth 1.000
#set yzeroaxis linetype -2 linewidth 1.000
#set zzeroaxis linetype -2 linewidth 1.000
#set x2zeroaxis linetype -2 linewidth 1.000
#set y2zeroaxis linetype -2 linewidth 1.000
#set ticslevel 0.5
#set mxtics default
#set mytics default
#set mztics default
#set mx2tics default
#set my2tics default
#set mcbtics default
#set xtics border in scale 1,0.5 mirror norotate  offset character 0, 0, 0
#set xtics  norangelimit
#set xtics   ()
#set ytics border in scale 1,0.5 mirror norotate  offset character 0, 0, 0
##set ytics autofreq  norangelimit
##set ytics autofreq
#set ztics border in scale 1,0.5 nomirror norotate  offset character 0, 0, 0
#set ztics autofreq  norangelimit
#set nox2tics
#set noy2tics
#set cbtics border in scale 1,0.5 mirror norotate  offset character 0, 0, 0
#set cbtics autofreq  norangelimit
#set title "" 
#set title  offset character 0, 0, 0 font "" norotate
#set timestamp bottom 
#set timestamp "" 
#set timestamp  offset character 0, 0, 0 font "" norotate
#set rrange [ * : * ] noreverse nowriteback  # (currently [8.98847e+307:-8.98847e+307] )
#set trange [ * : * ] noreverse nowriteback  # (currently [-5.00000:5.00000] )
#set urange [ * : * ] noreverse nowriteback  # (currently [-10.0000:10.0000] )
#set vrange [ * : * ] noreverse nowriteback  # (currently [-10.0000:10.0000] )
#set xlabel "" 
#set xlabel  offset character 0, 0, 0 font "" textcolor lt -1 norotate
#set x2label "" 
#set x2label  offset character 0, 0, 0 font "" textcolor lt -1 norotate
#set xrange [ * : * ] noreverse nowriteback  # (currently [1.00000:5.00000] )
#set x2range [ * : * ] noreverse nowriteback  # (currently [1.00000:5.00000] )
#set ylabel "" 
#set ylabel  offset character 0, 0, 0 font "" textcolor lt -1 rotate by -270
#set y2label "" 
#set y2label  offset character 0, 0, 0 font "" textcolor lt -1 rotate by -270
##set yrange [ * : * ] noreverse nowriteback  # (currently [10.0000:100.000] )
#set y2range [ * : * ] noreverse nowriteback  # (currently [17.1312:95.0433] )
#set zlabel "" 
#set zlabel  offset character 0, 0, 0 font "" textcolor lt -1 norotate
#set zrange [ * : * ] noreverse nowriteback  # (currently [-10.0000:10.0000] )
#set cblabel "" 
#set cblabel  offset character 0, 0, 0 font "" textcolor lt -1 rotate by -270
#set cbrange [ * : * ] noreverse nowriteback  # (currently [8.98847e+307:-8.98847e+307] )
#set zero 1e-008
#set lmargin  -1
#set bmargin  -1
#set rmargin  -1
#set tmargin  -1
##set locale "Polish_Poland.1250"
#set pm3d explicit at s
#set pm3d scansautomatic
#set pm3d interpolate 1,1 flush begin noftriangles nohidden3d corners2color mean
#set palette positive nops_allcF maxcolors 0 gamma 1.5 color model RGB 
#set palette rgbformulae 7, 5, 15
#set colorbox default
#set colorbox vertical origin screen 0.9, 0.2, 0 size screen 0.05, 0.6, 0 front bdefault
#set loadpath 
#set fontpath 
#set fit noerrorvariables
#GNUTERM = "wxt"
#f = ""



set terminal pdf
set xtics rotate by 45 
set xtics out
set xtics offset -0.75,-1
set key box outside below

set ylabel "Accuracy"
set xlabel "Population size"


kernels = "linear polynomial RBF sigmoid"
kernel(n) = word(kernels,n)
svm_vowels = "71.9696969697 7.5757575758 65.1515151515 42.7272727273"
svm_vowel(n) = word(svm_vowels,n)



#files = "iris.dat"
#generations = "1 3 5 7"
#set output "pdf/accuracy-iris.pdf"
# plot for [g in generations] 'grouped/iris.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-iris-detailed.pdf"
# plot for [g = 1:5] 'grouped/iris.detailed.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-iris-svm.pdf"
#plot for [g in generations] 'grouped/iris.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations', for [i=2:5] 'libsvm/iris.avg.dat' using 1:i with lines title columnheader
# unset output
#
#set output "pdf/accuracy-dna.pdf"
# plot for [g in generations] 'grouped/sd/dna.new.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
#unset output
#
#set output "pdf/accuracy-dna.detailed.pdf"
# plot for [g in generations] 'grouped/sd/dna.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
#unset output
#
#set output "pdf/accuracy-dna-svm.pdf"
# plot for [g in generations] 'grouped/sd/dna.new.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', 93.086003372681 title "svm linear", 50.843170320404 title "svm polynomial", 94.5193929173693 title "svm RBF",  93.76053962900505 title "svm sigmoid"
#unset output
#
#set output "pdf/accuracy-vowel.pdf"
# plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-vowel-detailed.pdf"
# plot for [g = 1:7] 'grouped/vowel.detailed.dat.generations-'.g.'.dat' using 1:18:xticlabels(3) with linespoints title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-vowel-detailed-sd.pdf"
# plot for [g = 1:7] 'grouped/sd/vowel.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-vowel-svm.pdf"
## plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations', 49.78354978354979 title "svm linear", 12.554112554112553 title "svm polynomial", 51.298701298701296 title "svm RBF",  43.290043290043286 title "svm sigmoid"
#plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:28:xticlabels(3) with linespoints title ''.g.' generations', for [i=2:5] 'libsvm/vowel.avg.dat' using 1:i with lines title columnheader
# unset output
#
#
#set output "pdf/accuracy-letter.pdf"
# plot for [g in generations] 'grouped/sd/letter.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-letter-detailed.pdf"
# plot for [g = 1:5] 'grouped/sd/letter.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
# unset output
#
#set output "pdf/accuracy-letter-svm.pdf"
#plot for [g in generations] 'grouped/sd/letter.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', for [i=2:5] 'libsvm/letter.avg.dat' using 1:i with lines title columnheader
# unset output
#

#fitnesses = "accuracy f1 mcc"

#files = "iris.dat"
#generations = "1 3 5 7"
#set output "pdf/accuracy-iris.pdf"
 #plot for [g in generations] 'grouped/iris.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output

#set output "pdf/accuracy-iris-detailed.pdf"
 #plot for [g in generations] 'grouped/iris.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output

##set yrange[0.95:1.05]
#set ylabel "Fitness"
#set output "pdf/fitness-iris-detailed.pdf"
 #plot for [g in generations] 'grouped/iris.detailed.dat.generations-'.g.'.dat' using 1:17:20:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output
#set ylabel "Accuracy"
##set yrange [ * : * ] noreverse nowriteback  # (currently [10.0000:100.000] )

#set output "pdf/accuracy-iris-svm.pdf"
#plot for [g in generations] 'grouped/iris.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', for [i=2:5] 'libsvm/iris.avg.dat' using 1:i with lines title columnheader
 #unset output


#set output "pdf/accuracy-dna.pdf"
 #plot for [g in generations] 'grouped/dna.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
#unset output

#set output "pdf/accuracy-dna-detailed.pdf"
 #plot for [g in generations] 'grouped/dna.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
#unset output

#set output "pdf/accuracy-dna-svm.pdf"
 #plot for [g in generations] 'grouped/dna.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', 93.086003372681 title "svm linear", 50.843170320404 title "svm polynomial", 94.5193929173693 title "svm RBF",  93.76053962900505 title "svm sigmoid"
#unset output



#set output "pdf/accuracy-vowel.pdf"
 #plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output

#set output "pdf/accuracy-vowel-detailed.pdf"
 #plot for [g in generations] 'grouped/vowel.detailed.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output

#set ylabel "Fitness"
#set output "pdf/fitness-vowel-detailed.pdf"
 #plot for [g in generations] 'grouped/vowel.detailed.dat.generations-'.g.'.dat' using 1:17:20:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output
#set ylabel "Accuracy"

#set output "pdf/accuracy-vowel-svm.pdf"
## plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:18:xticlabels(3) with linespoints title ''.g.' generations', 49.78354978354979 title "svm linear", 12.554112554112553 title "svm polynomial", 51.298701298701296 title "svm RBF",  43.290043290043286 title "svm sigmoid"
#plot for [g in generations] 'grouped/vowel.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', for [i=2:5] 'libsvm/vowel.avg.dat' using 1:i with lines title columnheader
 #unset output

#generations = "1 3 5"
#set output "pdf/accuracy-letter.pdf"
 #plot for [g in generations] 'grouped/letter.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations'
 #unset output

#set output "pdf/accuracy-letter-detailed.pdf"
 #plot for [g in generations] 'grouped/letter.detailed.dat.generations-'.g.'.dat' using 1:18:xticlabels(3) with linespoints title ''.g.' generations'
 #unset output

#set output "pdf/accuracy-letter-svm.pdf"
#plot for [g in generations] 'grouped/letter.dat.generations-'.g.'.dat' using 1:18:19:xticlabels(3) with yerrorlines title ''.g.' generations', for [i=2:5] 'libsvm/letter.avg.dat' using 1:i with lines title columnheader
 #unset output


set xlabel "Liczba pokoleń"
set key title 'Kernel'

population_size = "1 51 101"

set autoscale xfix
set autoscale y

kernels = "sigmoid rbf poly"
kernels_pl = "Sigmoidalny RBF Wielomianowy"

kernel(i) = word(kernels,i)
kernel_pl(i) = word(kernels_pl,i)

set clip two

###################################
########### Accuracy ##############
###################################

set ylabel "Trafność"
set output "pdf/accuracy-heart.pdf"
 plot 'grouped/result.dataset-heart.scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-heart.scale.kernel-'.kernel(i).'.dat' using (0):"mean_accuracy":(50):(0) with vector nohead title kernel_pl(i)
 unset output

set output "pdf/accuracy-breast.pdf"
 plot 'grouped/result.dataset-breast.scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-breast.scale.kernel-'.kernel(i).'.dat' using (0):"mean_accuracy":(50):(0) with vector nohead title kernel_pl(i)

 unset output

set output "pdf/accuracy-heart-big.pdf"
 plot 'raw/heart-big.dat' using "generations":"mean_accuracy" title "mean_accuracy"
unset output

#set output "pdf/accuracy-dna.pdf"
# plot for [p in population_size] 'grouped/dna.scale.dat.fitness_measure-accuracy.dat.population_size-'.p.'.dat' using "generations":"mean_accuracy"
# unset output


###################################
########## F1 meassure ############
###################################
set ylabel "Miara F1"
set output "pdf/f1-heart.pdf"
 plot 'grouped/result.dataset-heart.scale.fitness_measure-f1.dat' using "generations":"mean_f1" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-heart.scale.kernel-'.kernel(i).'.dat' using (0):"mean_f1":(50):(0) with vector nohead title kernel_pl(i)
 unset output


set output "pdf/f1-breast.pdf"
 plot 'grouped/result.dataset-breast.scale.fitness_measure-f1.dat' using "generations":"mean_f1" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-breast.scale.kernel-'.kernel(i).'.dat' using (0):"mean_f1":(50):(0) with vector nohead title kernel_pl(i)

 unset output

#set output "pdf/f1-dna.pdf"
# plot for [p in population_size] 'grouped/dna.scale.dat.fitness_measure-f1.dat.population_size-'.p.'.dat' using "generations":"mean_f1"
# unset output



###################################
#### Mathews Corelation Coef ######
###################################

set ylabel "Mathews Corelation Coefficent"
set output "pdf/mcc-heart.pdf"
 plot 'grouped/result.dataset-heart.scale.fitness_measure-mcc.dat' using "generations":"mean_mcc" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-heart.scale.kernel-'.kernel(i).'.dat' using (0):"mean_mcc":(50):(0) with vector nohead title kernel_pl(i)

 unset output


set output "pdf/mcc-breast.pdf"
 plot 'grouped/result.dataset-breast.scale.fitness_measure-mcc.dat' using "generations":"mean_mcc" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-breast.scale.kernel-'.kernel(i).'.dat' using (0):"mean_mcc":(50):(0) with vector nohead title kernel_pl(i)

unset output

#set output "pdf/mcc-dna.pdf"
# plot for [p in population_size] 'grouped/dna.scale.dat.fitness_measure-mcc.dat.population_size-'.p.'.dat' using "generations":"mean_mcc":xticlabels(5) 
# unset output



###################################
########## Probability ############
###################################

set ylabel "Średnie prawdopodobieństwo"
set output "pdf/probability-heart.pdf"
 plot 'grouped/result.dataset-heart.scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-heart.scale.kernel-'.kernel(i).'.dat' using (0):"mean_prob":(50):(0) with vector nohead title kernel_pl(i)
 
  #'grouped/result.dataset-heart.scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel 50/50",\
 unset output

set output "pdf/probability-breast.pdf"
 plot 'grouped/result.dataset-breast.scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-breast.scale.kernel-'.kernel(i).'.dat' using (0):"mean_prob":(50):(0) with vector nohead title kernel_pl(i)

unset output

set output "pdf/probability-heart-big.pdf"
 plot 'raw/heart-big.dat' using "generations":"mean_prob" title "mean_accuracy"
unset output

#set output "pdf/probability-dna.pdf"
# plot for [p in population_size] 'grouped/dna.scale.dat.fitness_measure-probability.dat.population_size-'.p.'.dat' using "generations":"mean_probability":xticlabels(5) 
# unset output



################################
######### Fitness ##############
################################

fitnesses = "accuracy f1 mcc probability"
fitnesses_pl = "trafność F1 MCC Prawdopodobieństwo"
fit(i) = word(fitnesses,i)
fit_pl(i) = word(fitnesses_pl,i)

set ylabel "Fitness"
set key title 'Miara przystosowania'

set output "pdf/fitness-heart.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-heart.scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)
unset output

set output "pdf/fitness-heart-big.pdf"
 plot 'raw/heart-big.dat' using "generations":"mean_fitness" title "Fitness"
unset output

set output "pdf/fitness-breast.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-heart.scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)
 unset output

#fitnesses = "accuracy f1"
#set output "pdf/fitness-dna.pdf"
# plot for [f in fitnesses] 'grouped/dna.scale.dat.fitness_measure-'.f.'.dat.population_size-101.dat' using "generations":"mean_fitness" title 'Miara przystosowania: '.f
#unset output


################################
########### Cost ###############
################################

kernels = "linear poly sigmoid rbf"

set xlabel "C (cost)"
set ylabel "Trafność"

set output "pdf/cost-accuracy-heart.pdf"
 plot for [k in kernels] 'grouped/cost.dat.result.dataset-heart.scale.kernel-'.k.'.dat' using "N":"mean_accuracy":xticlabels(12) title 'Kernel: '.k
unset output

set output "pdf/cost-accuracy-breast.pdf"
 plot for [k in kernels] 'grouped/cost.dat.result.dataset-breast.scale.kernel-'.k.'.dat' using "N":"mean_accuracy":xticlabels(12) title 'Kernel: '.k
 unset output

set ylabel "Czas"
set output "pdf/cost-time-heart.pdf"
 plot for [k in kernels] 'grouped/cost.dat.result.dataset-heart.scale.kernel-'.k.'.dat' using "N":"time":xticlabels(12) title 'Kernel: '.k
unset output

set output "pdf/cost-time-breast.pdf"
 plot for [k in kernels] 'grouped/cost.dat.result.dataset-breast.scale.kernel-'.k.'.dat' using "N":"time":xticlabels(12) title 'Kernel: '.k
 unset output


################################
########## Epsilon #############
################################

kernels = "linear poly sigmoid rbf"

set xlabel "C (epsilon)"
set ylabel "Trafność"

set output "pdf/epsilon-mean_prob-heart.pdf"
 plot for [k in kernels] 'grouped/epsilon.dat.result.dataset-heart.scale.kernel-'.k.'.dat' using "N":"mean_prob":xticlabels(9) title 'Kernel: '.k
unset output

set output "pdf/epsilon-mean_prob-breast.pdf"
 plot for [k in kernels] 'grouped/epsilon.dat.result.dataset-breast.scale.kernel-'.k.'.dat' using "N":"mean_prob":xticlabels(9) title 'Kernel: '.k
 unset output

set ylabel "Czas"
set output "pdf/epsilon-time-heart.pdf"
 plot for [k in kernels] 'grouped/epsilon.dat.result.dataset-heart.scale.kernel-'.k.'.dat' using "N":"time":xticlabels(9) title 'Kernel: '.k
unset output

set output "pdf/epsilon-time-breast.pdf"
 plot for [k in kernels] 'grouped/epsilon.dat.result.dataset-breast.scale.kernel-'.k.'.dat' using "N":"time":xticlabels(9) title 'Kernel: '.k
 unset output
