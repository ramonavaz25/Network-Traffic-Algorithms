import random
import math
from bitarray import bitarray
import mmh3
import numpy as np

import sys
filename  = open("outputfile.txt",'w')
sys.stdout = filename

class Estimator(object):

 def __init__(self, max_space):
    """
    Initializes the counter, max_space is the maximum amount of memory
    (in KB) you want to use to maintain your counter, more memory=more
    accurate
    """
    self.bit_map = bitarray(max_space)
    self.bit_map.setall(False)
    #print(self.bit_map.count(False))

 def current_count(self):
    """
    Gets the current value of the bitmap, to do that we follow the formula:
    -size * ln(unset_bits/size)
    """
    #print("Un%d"%self.bit_map.count(False))
    #print("  N VALUE:cardinality  %d" %self.bit_map.count(True))
    #float(self.bit_map.length())
    ratio = float(self.bit_map.count(False)) / float(self.bit_map.length())
    #print(ratio)
    if ratio == 0.0:
        return self.bit_map.length()
    else:
        return -self.bit_map.length() * math.log(ratio)

 def increment(self, item):
        """
        Counts an item
        """
        #print("Item",item)
        mm_hash = mmh3.hash((str(item)))
        #print(mm_hash)
        #print(self.bit_map.length())
        #hashed_value=hash(str(item))
        #print("item",mm_hash)
        offset = mm_hash % self.bit_map.length()
        #print('Offset',offset)
        self.bit_map[offset] = True
        #print("offset%d"%self.bit_map[offset])


if __name__ == '__main__':

    def run_counts(tries, size):
        """Run some counts"""
        text = open("C:/InternetTrafficMeasurement/flow_final.txt").read()
        tokens = [s.strip().split('\t') for s in text.splitlines()]
        #print(tokens[1][0])
        print("len%d" % len(tokens))
        count = 0
        s = []
        for i in range(0, len(tokens)):
            s.append(tokens[i][0])

        mylist = list(set(s))
        #print("length",len(mylist))
        linearpclist =[]
        cardinality =[]
        estimator=[]

        for j in range(0,len(mylist)):
            #print(mylist[j])
            linearpclist.append(Estimator(size))
            #print(linearpclist)
            #print(linearpc)
            for i in range(0,len(tokens)):
              #print("  Incrementing %d items" % tries)
              #print(tokens[i][0])
              if(tokens[i][0]==mylist[j]):

               #print("works",tokens[i][0])
               #print(tokens)
               linearpclist[j].increment(tokens[i][1])
               count = linearpclist[j].current_count()
            #print("Cardinality",linearpclist[j].bit_map.count(True))
            cardinality.append(linearpclist[j].bit_map.count(True))
            #print("Estimator",count)
            estimator.append(linearpclist[j].current_count())
        print(cardinality)
        print(estimator)
        #print(len(cardinality))
        #print(len(estimator))
        #print("  Estimator:  %d" % count)
        #print("  Actual: %d" % tries)
        #print("  Experimental:  %f%%" % abs(((float(count) / float(tries)))))




    run_counts(1, 150)
