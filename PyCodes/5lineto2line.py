import os

strpre = 'C:\\Users\\liziyang\\Desktop\\'

for i in range(3):
    k = i + 1
    rd = open(strpre + 'origin' + str(k) + '.txt', 'r')
    lin = rd.readlines()
    rd.close()
    wt = open(strpre + 'st' + str(k) + '.txt', 'w')
    cnt = 0
    for lines in lin:
        cnt += 1
        if cnt % 5 == 2 or cnt % 5 == 0:
            continue
        for ch in lines:
            if ch != '\n':
                wt.write(ch)
        if cnt % 5 == 1 or cnt % 5 == 4:
            wt.write('\n')
    wt.close()