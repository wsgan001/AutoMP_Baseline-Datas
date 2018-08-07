# coding: utf-8
# author：lu yf
# usedby: li zy
import numpy as np
from matplotlib import pyplot as plt

plt.rc('font', family='Times New Roman')  # 设置字体


def autolabel(rects, Num=1, rotation1=60, NN=1):
    for rect in rects:
        height = rect.get_height()
        plt.text(rect.get_x() - 0.04 + rect.get_width() / 2., Num * height, '%s' % float(height * NN),
                 rotation=rotation1)


# 直方图
def draw_Histogram():
    Y1 = [0.6676, 1.2675, 1.1204, 1.0558, 1.1332, 0.5678]  # NDCG@10
    Y2 = [0.6768, 1.2370, 1.1499, 1.1747, 0.8976, 1.0067]  # NDCG@20
    fig, ax = plt.subplots()
    index = np.arange(6) + 1
    bar_width = 0.15
    opacity = 0.8
    rec1 = plt.bar(index, Y1, bar_width, alpha=opacity, facecolor='lightskyblue', edgecolor='white', label='NDCG@10')
    rec2 = plt.bar(index + bar_width, Y2, bar_width, alpha=opacity, facecolor='yellowgreen', edgecolor='white',
                   label='NDCG@20')

    # autolabel(rec1)
    # autolabel(rec2)
    plt.ylim(0, 1.3)
    plt.yticks(fontsize=20)
    plt.xticks(index + bar_width, ('Relsim', 'Relsim+', 'ESER', 'ProxEmbed', 'AutoMP-', 'AutoMP'))
    plt.tick_params(axis='x', which='major', labelsize=36)
    # plt.xlabel('Methods')
    plt.ylabel('NDCG', fontsize=30)
    plt.legend(bbox_to_anchor=(0., 1.02, 1., .102), loc=3,
           ncol=2, mode="expand", borderaxespad=0., fontsize=30)
    plt.show()

draw_Histogram()