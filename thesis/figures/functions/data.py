#!/usr/bin/python

from random import randint, choice
from math import sin, cos, pi

points = 150
_range = 10
z_range = 190
border = 200
classes = ("minus", "plus")

for cls in classes:
    with open(cls+".dat", 'w') as f:
        for i in range(points):
            if cls == "minus":
                r = randint(0, _range-1)
            else:                
                r = randint(_range+1, 2*_range)
            a = 0.001 * randint(0, int(2000*pi))
            x = sin(a) * r
            y = cos(a) * r
            z = 3*x*x + 3*y*y + 200
            f.write("%f %f %f\n" % (x, y, z))        