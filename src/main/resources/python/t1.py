#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import difflib
 # -*- coding: utf-8 -*
import codecs
sys.stdout = codecs.getwriter("utf-8")(sys.stdout.detach())

def read_file(filename):
    try:
        with open(filename, mode='r',encoding='utf-8') as f:
            return f.readlines()
    except IOError:
        print ("ERROR: 没有找到文件:%s或读取文件失败！" % filename)
        sys.exit(1)
#判断相似度的方法，用到了difflib库
def get_equal_rate_1(str1, str2):
   return difflib.SequenceMatcher(None, str1, str2).quick_ratio()
def compare_file():
    file1_content = read_file(sys.argv[1])
    file2_content = read_file(sys.argv[2])
    d = difflib.HtmlDiff(tabsize=4,wrapcolumn=150)
    m = get_equal_rate_1(file1_content, file2_content)
    print (m)
    ss = d.make_table(file1_content, file2_content)
   
    print(ss)
    #print (d.make_table(file1_content, file2_content))
    #result = d.make_file(file1_content, file2_content)
    #with open(out_file, 'w') as f:
    #   f.writelines(result)

if __name__ == '__main__':
    compare_file()