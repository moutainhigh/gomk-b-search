#!/usr/bin/env python
# -*- coding: utf-8 -*-

import difflib
import sys

defaultencoding = 'utf-8'
if sys.getdefaultencoding() != defaultencoding:
    reload(sys)
    sys.setdefaultencoding(defaultencoding)

try:
    textfile1 = sys.argv[1]
    textfile2 = sys.argv[2]
except Exception as e:
    print ("Error:" +str(e))
    print ("Usage: python sample3.py filename1 filename2")
    sys.exit()

def readfile(filename):
    try:
        filehandle = open(filename,'rb')
        text = filehandle.read().splitlines()
        filehandle.close()
        return text
    except IOError as error:
        print ('Read file Error:' +str(error))
        sys.exit()
        
#判断相似度的方法，用到了difflib库
def get_equal_rate_1(str1, str2):
   return difflib.SequenceMatcher(None, str1, str2).quick_ratio()

def diffdo(t1,t2):
    text1_lines = t1.splitlines() #dsfsd
    text2_lines = t2.splitlines() #dsklf
#print (text1_lines)
#d = difflib.Differ()
#d=difflib.Differ()
#diff=d.compare(text1_lines,text2_lines)
#print ('\n'.join(list(diff)))

   
d = difflib.HtmlDiff(tabsize=4,wrapcolumn=150)
#print (d.make_file(text1_lines,text2_lines))

    
print (get_equal_rate_1(text1_lines, text2_lines))
    
print (d.make_table(text1_lines, text2_lines))
    #return d.make_file(text1_lines,text2_lines)
    #return d.make_file(str_2,str_3)


diffdo(textfile1,textfile2)