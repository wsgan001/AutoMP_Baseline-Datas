# Author: lizi
# Email: lzy960601@outlook.com

import os

#dbpedia_names = ['11b', '21o', '21b', '22b']
sufnames = ['11b', '21o', '21b', '22b']

prepath = 'C:\\Users\\lzy96\\Desktop\\nDCG\\'

def solve(filenm):
    ans1 = [0 for i in range(5)]
    ans2 = [0 for i in range(5)]
    wr = open(prepath + filenm + '.txt', 'w')
    for id in range(4):
        nm = prepath + filenm + '_' + sufnames[id] + '.txt'
        rd = open(nm, 'r')
        for lines in range(5):
            st = rd.readline().split(';')
            ans1[lines] += (float)(st[0])
            ans2[lines] += (float)(st[1])
        rd.close()
    for lin in range(5):
        a1 = ans1[lin]/4.0
        a2 = ans2[lin]/4.0
        print(str(a1) + ';' + str(a2), file=wr)
    wr.close()

solve('dbpedia2')
solve('dbpedia3')
solve('yago')
