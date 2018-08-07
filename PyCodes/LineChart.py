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

    # plt.subplot(1, 2, 1)

    # X轴，Y轴数据
    x = [1, 2, 3, 4, 5, 6, 7, 8]
    y1 = [1.0924, 1.0870, 1.0751, 1.0658, 1.0543, 1.0234, 1.0666, 1.0698]  # Relsim
    y2 = [1.0810, 1.0766, 1.0647, 1.0552, 1.0425, 1.0689, 1.0456, 0.9968]  # Relsim+
    y3 = [1.0689, 1.0456, 0.9968, 0.9835, 0.9724, 1.0870, 1.0751, 1.0658]  # ESER
    y4 = [1.0340, 1.0228, 0.9907, 0.9772, 0.9682, 1.0766, 1.0647, 1.0552]  # ProxEmbed
    y5 = [0.9724, 1.0870, 1.0751, 1.0658, 0.9724, 0.9724, 1.0870, 1.0658]  # AutoMP-
    y6 = [1.0766, 1.0647, 1.0552, 1.0425, 1.0689, 1.0456, 1.0870, 1.0751]  # AutoMP

    plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')

    plt.xlim(1, 8)
    # plt.ylim(0.6,0.9)
    plt.ylim(0.8, 1.2)
    plt.yticks(fontsize=20)
    plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'], fontsize=20)
    plt.xlabel("Number of Meta-Paths",fontsize=35)  # X轴标签
    plt.ylabel("NDCG@10", fontsize=35)
    plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.5)
    plt.legend(fontsize=30, loc=(1.02, 0))

    # plt.subplot(1, 2, 2)

    # X轴，Y轴数据
    # x = [1, 2, 3, 4, 5, 6, 7, 8]
    # y1 = [1.0924, 1.0870, 1.0751, 1.0658, 1.0543, 1.0234, 1.0666, 1.0698]  # Relsim
    # y2 = [1.0810, 1.0766, 1.0647, 1.0552, 1.0425, 1.0689, 1.0456, 0.9968]  # Relsim+
    # y3 = [1.0689, 1.0456, 0.9968, 0.9835, 0.9724, 1.0870, 1.0751, 1.0658]  # ESER
    # y4 = [1.0340, 1.0228, 0.9907, 0.9772, 0.9682, 1.0766, 1.0647, 1.0552]  # ProxEmbed
    # y5 = [0.9724, 1.0870, 1.0751, 1.0658, 0.9724, 0.9724, 1.0870, 1.0658]  # AutoMP-
    # y6 = [1.0766, 1.0647, 1.0552, 1.0425, 1.0689, 1.0456, 1.0870, 1.0751]  # AutoMP
    #
    # plt.plot(x, y1, 'gd-', alpha=0.8, color='lightskyblue', linewidth=2, label='Relsim')  # 在当前绘图对象绘图（X轴，Y轴，蓝色虚线，线宽度）
    # plt.plot(x, y2, 'g*-', alpha=0.8, color='yellowgreen', linewidth=2, label='Relsim+')
    # plt.plot(x, y3, 'gs-', alpha=0.8, color='grey', linewidth=2, label='ESER')
    # plt.plot(x, y4, 'g^-', alpha=0.8, color='red', linewidth=2, label='ProxEmbed')
    # plt.plot(x, y5, 'gx-', alpha=0.8, color='purple', linewidth=2, label='ESER')
    # plt.plot(x, y6, 'go-', alpha=0.8, color='black', linewidth=2, label='ProxEmbed')
    #
    # plt.xlim(1, 8)
    # # plt.ylim(0.6,0.9)
    # plt.ylim(0.8, 1.2)
    # plt.xticks([1, 2, 3, 4, 5, 6, 7, 8], [r'1', r'2', r'3', r'4', r'5', r'6', r'7', r'8'])
    # plt.xlabel("Number of xx")  # X轴标签
    # plt.ylabel("NDCG@20 on xx Queries")
    # plt.grid(color='#95a5a6', linestyle='--', linewidth=1, axis='y', alpha=0.4)
    # plt.legend()

    plt.show()  # 显示图


draw_line_chart()