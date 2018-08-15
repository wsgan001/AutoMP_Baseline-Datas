# coding: utf-8
# author：lu yf
# usedby: li zy
import numpy as np
from matplotlib import pyplot as plt

plt.rc('font', family='Times New Roman')  # 设置字体字号

fontsz = 35

def autolabel(rects, Num=1, rotation1=60, NN=1):
    for rect in rects:
        height = rect.get_height()
        plt.text(rect.get_x() - 0.04 + rect.get_width() / 2., Num * height, '%s' % float(height * NN),
                 rotation=rotation1)

# 折线图
def draw_line_chart():

    plt.figure(figsize=(26,10.5))

    plt.subplot(1, 2, 1)

    # X轴，Y轴数据
    x = [1, 2, 3, 4]
    y1 = [0.9751091672977404, 0.9325064012444002, 0.6255594562139812, 0.8990115838878243]  # ESER
    y2 = [0.7837216855897099, 1.0, 0.7527205878538072, 0.08876485515623594]  # Relsim
    y3 = [1.0, 1.0, 0.8360831817002762, 0.9158409152568503]  # Exaustive
    y4 = [1.0, 1.0, 0.8939432268477328, 0.9158409152568503]  # Greedy
    y5 = [0.947467334976222, 0.12157046892101434, 0.32506758771108035, 0.21509278478125826]  # ProxEmbed2
    y6 = [0.8936379211801049, 0.5407785970730171, 0.7825839661602851, 0.471204999561076]  # D2AGE2

    # plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='ESER')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    # plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim')
    # plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='Exaustive')
    # plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='Greedy')
    # plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ProxEmbed2')
    # plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='D2AGE2')

    plt.plot(x, y1, 'gd-', alpha=0.8, linewidth=2, label='ESER', color='black', ms=10)
    plt.plot(x, y2, 'g*-', alpha=0.8, linewidth=2, label='Relsim', color='black', ms=10)
    plt.plot(x, y3, 'gs-', alpha=0.8, linewidth=2, label='Exaustive', color='black', ms=10)
    plt.plot(x, y4, 'g^-', alpha=0.8, linewidth=2, label='Greedy', color='black', ms=10)
    plt.plot(x, y5, 'gx-', alpha=0.8, linewidth=2, label='ProxEmbed2', color='black', ms=10)
    plt.plot(x, y6, 'go-', alpha=0.8, linewidth=2, label='D2AGE2', color='black', ms=10)

    plt.xlim(1, 4)
    # plt.ylim(0.6,0.9)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    plt.xticks([1, 2, 3, 4], [r'11b', r'21b', r'21o', r'22b'], fontsize=fontsz)
    # plt.ylabel("NDCG@10 on DBpedia", fontsize=fontsz)
    # plt.xlabel("DBpedia DataSets", fontsize=fontsz)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)
    #plt.legend(fontsize=25, ncol=6, loc=(0.12, 1.04))
    plt.legend(ncol=6, loc=(0.01,1.03), fontsize=fontsz)

    plt.subplot(1, 2, 2)

    # X轴，Y轴数据
    x = [1, 2, 3, 4]
    y1 = [0.6282599426326987, 0.06514158083418359, 0.5222178826221934, 0.9861137556126445]  # ESER
    y2 = [0.5864973849349379, 0.054467574074231626, 0.4380314270875787, 0.1784783709018772]  # Relsim
    y3 = [0.9718342412090308, 0.0, 0.8193871392755444, 0.8354982219885247]  # Exaustive
    y4 = [0.9427707232422382, 0.9575073986183329, 0.7571344764627659, 0.7497816654045188]  # Greedy
    y5 = [0.5, 0.5, 0.5, 0.5]  # ProxEmbed2
    y6 = [0.5, 0.5, 0.5, 0.5]  # D2AGE2

    plt.plot(x, y1, 'gd-', alpha=0.8, linewidth=2, label='ESER', color='black', ms=10)
    plt.plot(x, y2, 'g*-', alpha=0.8, linewidth=2, label='Relsim', color='black', ms=10)
    plt.plot(x, y3, 'gs-', alpha=0.8, linewidth=2, label='Exaustive', color='black', ms=10)
    plt.plot(x, y4, 'g^-', alpha=0.8, linewidth=2, label='Greedy', color='black', ms=10)
    plt.plot(x, y5, 'gx-', alpha=0.8, linewidth=2, label='ProxEmbed2', color='black', ms=10)
    plt.plot(x, y6, 'go-', alpha=0.8, linewidth=2, label='D2AGE2', color='black', ms=10)

    plt.xlim(1, 4)
    # plt.ylim(0.6,0.9)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    # plt.xlabel("YAGO DataSets", fontsize=30)
    plt.xticks([1, 2, 3, 4], [r'11b', r'21b', r'21o', r'22b'], fontsize=fontsz)
    # plt.ylabel("NDCG@10 on YAGO", fontsize=fontsz)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)

    plt.subplots_adjust(left=0.03,right=0.98,wspace=0.15,hspace=0.15,bottom=0.08,top=0.88)
    # plt.show()  # 显示图
    plt.savefig('C:\\Users\\lzy96\\Desktop\\linechart.pdf')

draw_line_chart()
