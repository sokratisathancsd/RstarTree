# -*- coding: utf-8 -*-
"""
Created on Mon Sep 21 22:00:43 2020

@author: Dani
"""
import matplotlib.pyplot as plt

infile = open('outData.txt', 'r')
for line in infile:
    line=line.replace("[","")
    line=line.replace("]","")
    line=line.replace(" ","")
    line=line.split(",")
    x1=float(line[0])
    x2=float(line[1])
    y1=float(line[2])
    y2=float(line[3])
   
    rectangle = [(x1,y1),(x1,y2),(x2,y2),(x2,y1)]
    plt.plot(*zip(*(rectangle+rectangle[:1])), marker='')
infile.close()
infile = open('points.txt', 'r')  
for line in infile:
    line=line.split(",")
    p1=float(line[0])
    p2=float(line[1])
    plt.plot([p1], [p2], 'rx')#, markersize=0.5,alpha=0.4
   
plt.savefig('filename.png', dpi=300)
infile.close()


