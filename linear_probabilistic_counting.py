import random
import math
from bitarray import bitarray
import mmh3
import numpy as np

class Estimator(object):

 def __init__(self, max_space):
    """
    Initializes the counter, max_space is the maximum amount of memory
    (in KB) you want to use to maintain your counter, more memory=more
    accurate
    """
    self.bit_map = bitarray(max_space)
    self.bit_map.setall(False)
    print(self.bit_map.count(False))

 def current_count(self):
    """
    Gets the current value of the bitmap, to do that we follow the formula:
    -size * ln(unset_bits/size)
    """
    print("Un%d"%self.bit_map.count(False))
    print("  N VALUE:cardinality  %d" %self.bit_map.count(True))
    float(self.bit_map.length())
    ratio = float(self.bit_map.count(False)) / float(self.bit_map.length())
    print(ratio)
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
        print('Offset',offset)
        self.bit_map[offset] = True
        print("offset%d"%self.bit_map[offset])


if __name__ == '__main__':

    def run_counts(tries, size):
        """Run some counts"""
        #Parse input file
        text = open("C:/InternetTrafficMeasurement/flow_final.txt").read()
        #Split source and destination based on \t for every entry in file.First index contains source, second index contains destination
        tokens = [s.strip().split('\t') for s in text.splitlines()]
        print(tokens[1][0])
        print("len%d" % len(tokens))
        count = 0
        s = []
        #find unique sources
        for i in range(0, len(tokens)):
            s.append(tokens[i][0])
        

        mylist = list(set(s))
        #print("length",len(mylist))
        linearpclist =[]
        cardinality =[]
        estimator=[]

        for j in range(0,len(mylist)):
            #print(mylist[j])
            #initiate bitmap objects of length "size" for each unique source and add all objects to master list called linearpclist
            linearpclist.append(Estimator(size))
            #print(linearpclist)
            #print(linearpc)
            #iterate over all elements of input file tokens
            for i in range(0,len(tokens)):
              #print("  Incrementing %d items" % tries)
              #print(tokens[i][0])
              #compare first index of tokens to list of sources and if it matches set bit in bitmap of that particular source using increment function 
              if(tokens[i][0]==mylist[j]):

               print("works",tokens[i][0])
               #print(tokens)
               linearpclist[j].increment(tokens[i][1])
               #keep tab of count being increased (estimator value)
               count = linearpclist[j].current_count()
            print("Cardinality",linearpclist[j].bit_map.count(True))
            cardinality.append(linearpclist[j].bit_map.count(True))
            print("Estimator",count)
            estimator.append(linearpclist[j].current_count())
        print(cardinality)
        print(estimator)
        print(len(cardinality))
        print(len(estimator))
        #print("  Estimator:  %d" % count)
        #print("  Actual: %d" % tries)
        #print("  Experimental:  %f%%" % abs(((float(count) / float(tries)))))




    run_counts(500, 150)


