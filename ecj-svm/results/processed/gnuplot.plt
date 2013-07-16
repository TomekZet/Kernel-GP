#!/usr/bin/gnuplot


set terminal pdf
set xtics rotate by 45 
set xtics out
set xtics offset -0.75,-1
set key box outside below right

set ylabel "Accuracy"
set xlabel "Population size"


kernels = "linear polynomial RBF sigmoid"
kernel(n) = word(kernels,n)
svm_vowels = "71.9696969697 7.5757575758 65.1515151515 42.7272727273"
svm_vowel(n) = word(svm_vowels,n)


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

set output "pdf/accuracy-pima-diabetes.pdf"
 plot 'grouped/result.dataset-pima-diabetes_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-pima-diabetes_scale.kernel-'.kernel(i).'.dat' using (0):"mean_accuracy":(40):(0) with vector nohead title kernel_pl(i)

set output "pdf/accuracy-liver-disorders.pdf"
 plot 'grouped/result.dataset-liver-disorders_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-liver-disorders_scale.kernel-'.kernel(i).'.dat' using (0):"mean_accuracy":(40):(0) with vector nohead title kernel_pl(i)

set output "pdf/accuracy-ionosphere.pdf"
 plot 'grouped/result.dataset-ionosphere_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-ionosphere_scale.kernel-'.kernel(i).'.dat' using (0):"mean_accuracy":(40):(0) with vector nohead title kernel_pl(i)

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

set output "pdf/f1-liver-disorders.pdf"
 plot 'grouped/result.dataset-liver-disorders_scale.fitness_measure-f1.dat' using "generations":"mean_f1" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-liver-disorders_scale.kernel-'.kernel(i).'.dat' using (0):"mean_f1":(50):(0) with vector nohead title kernel_pl(i)
 unset output

set output "pdf/f1-pima-diabetes.pdf"
 plot 'grouped/result.dataset-pima-diabetes_scale.fitness_measure-f1.dat' using "generations":"mean_f1" title "GP Kernel",\
 for [i=1:words(kernels)] 'libsvm/result.dataset-pima-diabetes_scale.kernel-'.kernel(i).'.dat' using (0):"mean_f1":(50):(0) with vector nohead title kernel_pl(i)
 unset output

#set output "pdf/f1-ionosphere.pdf"
 #plot 'grouped/result.dataset-ionosphere_scale.fitness_measure-f1.dat' using "generations":"mean_f1" title "GP Kernel",\
 #for [i=1:words(kernels)] 'libsvm/result.dataset-ionosphere_scale.kernel-'.kernel(i).'.dat' using (0):"mean_f1":(50):(0) with vector nohead title kernel_pl(i)
 #unset output

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


set output "pdf/probability-pima-diabetes.pdf"
 plot 'grouped/result.dataset-pima-diabetes_scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-pima-diabetes_scale.kernel-'.kernel(i).'.dat' using (0):"mean_prob":(40):(0) with vector nohead title kernel_pl(i)
unset output

set output "pdf/probability-liver-disorders.pdf"
 plot 'grouped/result.dataset-liver-disorders_scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-liver-disorders_scale.kernel-'.kernel(i).'.dat' using (0):"mean_prob":(40):(0) with vector nohead title kernel_pl(i)
unset output

set output "pdf/probability-ionosphere.pdf"
 plot 'grouped/result.dataset-ionosphere_scale.fitness_measure-probability.dat' using "generations":"mean_prob" title "GP Kernel" ,\
 for [i=1:words(kernels)] 'libsvm/result.dataset-ionosphere_scale.kernel-'.kernel(i).'.dat' using (0):"mean_prob":(40):(0) with vector nohead title kernel_pl(i)
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
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-heart.scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)#,\
 #'grouped/result.dataset-heart.scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy"   title "Test accuracy"
unset output

set output "pdf/fitness-heart-big.pdf"
 plot 'raw/heart-big.dat' using "generations":"mean_fitness" title "Fitness"
unset output

set output "pdf/fitness-breast.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-breast.scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)#,\
 #'grouped/result.dataset-breast.scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "Test accuracy"
 unset output


fitnesses = "accuracy probability"
fitnesses_pl = "trafność Prawdopodobieństwo"

set output "pdf/fitness-liver-disorders.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-liver-disorders_scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)#,\
 #'grouped/result.dataset-liver-disorders_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "Test accuracy"
 unset output

set output "pdf/fitness-pima-diabetes.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-pima-diabetes_scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)#,\
 #'grouped/result.dataset-pima-diabetes_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "Test accuracy"

 unset output

set output "pdf/fitness-ionosphere.pdf"
 plot for [i=1:words(fitnesses)] 'grouped/result.dataset-ionosphere_scale.fitness_measure-'.fit(i).'.dat' using "generations":"mean_fitness" title fit_pl(i)#,\
 #'grouped/result.dataset-ionosphere_scale.fitness_measure-accuracy.dat' using "generations":"mean_accuracy" title "Test accuracy" 
 unset output


###################################
### Overfitting ##################
##################################


set key title 'Trafność'

set output "pdf/fitness-heart-CV.pdf"
 plot 'grouped/cv.dat.result.dataset-heart.scale.dat' using "generations":"mean_fitness" title "Walidacja",\
 'grouped/cv.dat.result.dataset-heart.scale.dat' using "generations":"mean_accuracy"   title "Testowanie"
unset output

set output "pdf/fitness-breast-CV.pdf"
 plot 'grouped/cv.dat.result.dataset-breast.scale.dat' using "generations":"mean_fitness" title "Walidacja",\
'grouped/cv.dat.result.dataset-breast.scale.dat' using "generations":"mean_accuracy" title "Testowanie"
 unset output

set output "pdf/fitness-liver-disorders-CV.pdf"
 plot 'grouped/cv.dat.result.dataset-liver-disorders_scale.dat' using "generations":"mean_fitness" title "Walidacja",\
 'grouped/cv.dat.result.dataset-liver-disorders_scale.dat' using "generations":"mean_accuracy" title "Testowanie"
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


################################
########## Elite #############
################################

elites = "0 5

set ylabel "Fitness"
set xlabel "Czas (pokolenia)
set key title 'Wielkość elit

set output "pdf/elites-heart.pdf"
 plot for [i in elites] 'grouped/heart.scale.dat.elites-'.i.'.dat' using "generations":"mean_fitness" title i
unset output


################################
########## Time ###############
################################

set ylabel "Czas [s]"
set xlabel "Kernel"

unset key

#set auto fix

set offsets 0.5, 0.5


datasets = "breast_scale heart_scale Liver-disorders_scale Pima-diabetes_scale ionosphere_scale" 
datasets_pl = "breast heart Liver-disorders Pima-diabetes Ionosphere"
dataset(i) = word(datasets,i)
dataset_pl(i) = word(datasets_pl,i)

kernels = "sigmoid rbf poly GP-Linear GP-Poly GP-RBF GP-Sigmoid GP-Kernel GP-100"
kernels_pl = "linear poly rbf sigmoid GP-Linear GP-Poly GP-RBF GP-Sigmoid GP(P=1,G=1) GP(P=10,G=10)"

set boxwidth 0.5 relative
set style boxplot sorted

set notitle

set output "pdf/time-ionosphere.pdf"
#plot for [i=1:words(kernels)] 'grouped/time_all.dat.result.dataset-ionosphere_scale.dat' using "N":"time":"time_sd":xticlabels(3) title 'Kernel: '.kernel_pl(i) with boxerrorbars
 plot 'grouped/time_some.dat.result.dataset-ionosphere_scale.dat' using "N":"time":"time_sd":xticlabels(3) with boxerrorbars
unset output

set output "pdf/time-liver-disorders.pdf"
 plot 'grouped/time_some.dat.result.dataset-liver-disorders_scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars
unset output

set output "pdf/time-pima-diabetes.pdf"
 plot 'grouped/time_some.dat.result.dataset-pima-diabetes_scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars
unset output

set output "pdf/time-heart.pdf"
 plot 'grouped/time_some.dat.result.dataset-heart.scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

set output "pdf/time-breast.pdf"
 plot 'grouped/time_some.dat.result.dataset-breast.scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars
unset output



set output "pdf/time_almost_all_ionosphere.pdf"
  plot 'grouped/time_almost_all.dat.result.dataset-ionosphere_scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

set output "pdf/time_almost_all_liver-disorders.pdf"
  plot 'grouped/time_almost_all.dat.result.dataset-liver-disorders_scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

set output "pdf/time_almost_all_pima-diabetes.pdf"
  plot 'grouped/time_almost_all.dat.result.dataset-pima-diabetes_scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

set output "pdf/time_almost_all_heart.pdf"
  plot 'grouped/time_almost_all.dat.result.dataset-heart.scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

set output "pdf/time_almost_all_breast.pdf"
  plot 'grouped/time_almost_all.dat.result.dataset-breast.scale.dat' using "N":"time":"time_sd":xticlabels(3)  with boxerrorbars notitle
unset output

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