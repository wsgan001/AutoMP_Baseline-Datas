# coding: utf-8
# author：lu yf
# usedby: li zy
import numpy as np
from matplotlib import pyplot as plt

plt.rc('font', family='Times New Roman')  # 设置字体字号


def autolabel(rects, Num=1, rotation1=60, NN=1):
    for rect in rects:
        height = rect.get_height()
        plt.text(rect.get_x() - 0.04 + rect.get_width() / 2., Num * height, '%s' % float(height * NN),
                 rotation=rotation1)

# 折线图
def draw_line_chart():

    plt.subplot(2, 2, 1)

    # X轴，Y轴数据
    x = [1, 2, 3, 4, 5, 6, 7, 8]
    y1 = [1.1924, 1.1870, 1.1751, 1.1658, 1.1543, 1.1234, 1.1166, 1.1098]  # Relsim
    y2 = [1.1324, 1.1270, 1.1151, 1.1058, 1.0943, 1.0634, 1.0566, 1.0498]  # Relsim+
    y3 = [1.0724, 1.0670, 1.0551, 1.0458, 1.0343, 1.0034, 0.9966, 0.9898]  # ESER
    y4 = [1.0124, 1.0070, 0.9951, 0.9858, 0.9743, 0.9434, 0.9366, 0.9298]  # ProxEmbed
    y5 = [0.9524, 0.9470, 0.9351, 0.9258, 0.9143, 0.8834, 0.8766, 0.8698]  # AutoMP-
    y6 = [0.8924, 0.8870, 0.8751, 0.8658, 0.8543, 0.8234, 0.8166, 0.8098]  # AutoMP

    plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')

    plt.xlim(1, 8)
    # plt.ylim(0.6,0.9)
    plt.ylim(0.8, 1.2)
    plt.yticks(fontsize=15)
    plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'], fontsize=15)
    # plt.xlabel("Number of Meta-Paths",fontsize=35)  # X轴标签
    plt.ylabel("SIMPLE_NDCG@10", fontsize=20)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)
    plt.legend(fontsize=20, ncol=6, loc=(0.005, 1.04))

    plt.subplot(2, 2, 2)

    # X轴，Y轴数据
    x = [1, 2, 3, 4, 5, 6, 7, 8]
    y1 = [1.1924, 1.1870, 1.1751, 1.1658, 1.1543, 1.1234, 1.1166, 1.1098]  # Relsim
    y2 = [1.1324, 1.1270, 1.1151, 1.1058, 1.0943, 1.0634, 1.0566, 1.0498]  # Relsim+
    y3 = [1.0724, 1.0670, 1.0551, 1.0458, 1.0343, 1.0034, 0.9966, 0.9898]  # ESER
    y4 = [1.0124, 1.0070, 0.9951, 0.9858, 0.9743, 0.9434, 0.9366, 0.9298]  # ProxEmbed
    y5 = [0.9524, 0.9470, 0.9351, 0.9258, 0.9143, 0.8834, 0.8766, 0.8698]  # AutoMP-
    y6 = [0.8924, 0.8870, 0.8751, 0.8658, 0.8543, 0.8234, 0.8166, 0.8098]  # AutoMP

    plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')

    plt.xlim(1, 8)
    # plt.ylim(0.6,0.9)
    plt.ylim(0.8, 1.2)
    plt.yticks(fontsize=15)
    plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'], fontsize=15)
    # plt.xlabel("Number of Meta-Paths",fontsize=35)  # X轴标签
    plt.ylabel("SIMPLE_NDCG@20", fontsize=20)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)

    plt.subplot(2, 2, 3)

    # X轴，Y轴数据
    x = [1, 2, 3, 4, 5, 6, 7, 8]
    y1 = [1.1924, 1.1870, 1.1751, 1.1658, 1.1543, 1.1234, 1.1166, 1.1098]  # Relsim
    y2 = [1.1324, 1.1270, 1.1151, 1.1058, 1.0943, 1.0634, 1.0566, 1.0498]  # Relsim+
    y3 = [1.0724, 1.0670, 1.0551, 1.0458, 1.0343, 1.0034, 0.9966, 0.9898]  # ESER
    y4 = [1.0124, 1.0070, 0.9951, 0.9858, 0.9743, 0.9434, 0.9366, 0.9298]  # ProxEmbed
    y5 = [0.9524, 0.9470, 0.9351, 0.9258, 0.9143, 0.8834, 0.8766, 0.8698]  # AutoMP-
    y6 = [0.8924, 0.8870, 0.8751, 0.8658, 0.8543, 0.8234, 0.8166, 0.8098]  # AutoMP

    plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')

    plt.xlim(1, 8)
    # plt.ylim(0.6,0.9)
    plt.ylim(0.8, 1.2)
    plt.yticks(fontsize=15)
    plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'], fontsize=15)
    # plt.xlabel("Number of Meta-Paths",fontsize=35)  # X轴标签
    plt.ylabel("COMPLEX_NDCG@10", fontsize=20)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)

    plt.subplot(2, 2, 4)

    # X轴，Y轴数据
    x = [1, 2, 3, 4, 5, 6, 7, 8]
    y1 = [1.1924, 1.1870, 1.1751, 1.1658, 1.1543, 1.1234, 1.1166, 1.1098]  # Relsim
    y2 = [1.1324, 1.1270, 1.1151, 1.1058, 1.0943, 1.0634, 1.0566, 1.0498]  # Relsim+
    y3 = [1.0724, 1.0670, 1.0551, 1.0458, 1.0343, 1.0034, 0.9966, 0.9898]  # ESER
    y4 = [1.0124, 1.0070, 0.9951, 0.9858, 0.9743, 0.9434, 0.9366, 0.9298]  # ProxEmbed
    y5 = [0.9524, 0.9470, 0.9351, 0.9258, 0.9143, 0.8834, 0.8766, 0.8698]  # AutoMP-
    y6 = [0.8924, 0.8870, 0.8751, 0.8658, 0.8543, 0.8234, 0.8166, 0.8098]  # AutoMP

    plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')

    plt.xlim(1, 8)
    # plt.ylim(0.6,0.9)
    plt.ylim(0.8, 1.2)
    plt.yticks(fontsize=15)
    plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'], fontsize=15)
    # plt.xlabel("Number of Meta-Paths",fontsize=35)  # X轴标签
    plt.ylabel("COMPLEX_NDCG@20", fontsize=20)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)

    plt.show()  # 显示图


draw_line_chart()