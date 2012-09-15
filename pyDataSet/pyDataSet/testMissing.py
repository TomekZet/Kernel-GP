'''
Created on Sep 15, 2012

@author: tomek
'''
import unittest
from pyDataSet import substitute_missing

class Test(unittest.TestCase):


    def setUp(self):
        self.data = [
           ['?',  6, '?', 5, 'b'],
           ['?',  3,  5,  8, 'a'],
           ['?','?','?','?', '?'],
           ['?',  3,  1,  5, 'a']]
        
        self.correct = [
           [0,  6, 3.0, 5, 'b'],
           [0,  3,  5,  8, 'a'],
           [0,  4, 3.0,6.0,'a'],
           [0,  3,  1,  5, 'a']]

    def tearDown(self):
        pass


    def testMissing(self):
        substitute_missing(self.data)
        self.assertEqual(self.data, self.correct)


if __name__ == "__main__":
    #import sys;sys.argv = ['', 'Test.testName']
    unittest.main()