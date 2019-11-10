#!/usr/bin/python3
# -*- coding: UTF-8 -*-
import camelot
import sys


def parse():  # 解析表格数据内容
    try:
        tables = camelot.read_pdf(sys.argv[1], pages=sys.argv[2])
        if not tables:
            print("")
        else:
            print(tables[0].data)
    except IOError:
        print("")


if __name__ == '__main__':
    parse()
