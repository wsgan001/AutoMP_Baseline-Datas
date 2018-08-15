# coding: utf-8
# author：lu yf
# usedby: li zy
import numpy as np
from matplotlib import pyplot as plt

plt.rc('font', family='Times New Roman')  # 设置字体

fontsz = 35

def autolabel(rects, Num=1, rotation1=60, NN=1):
    for rect in rects:
        height = rect.get_height()
        plt.text(rect.get_x() - 0.04 + rect.get_width() / 2., Num * height, '%s' % float(height * NN),
                 rotation=rotation1)


# 直方图
def draw_Histogram():

    plt.figure(figsize=(26,10.5)) 

    plt.subplot(2, 2, 1)

    Y1 = [0.787072017862545, 0.8384119889067772, 0.8580466521609864, 0.8887105437692729, 0.8859724833292195]  # ESER
    Y2 = [0.6131036822339513, 0.6439469703167491, 0.6563017821499382, 0.6542972154521142, 0.666094347981088]  # RelSim
    Y3 = [0.8841974446492994, 0.931095783612906, 0.9379810242392816, 0.9459837115853933, 0.9506620136993076]  # Exaustive
    Y4 = [0.8524521609842416, 0.9418391614380608, 0.9524460355261457, 0.9484477943097445, 0.9708506709006545]  # Heuristic
    Y5 = [0.4326687414591507, 0.40982108491143937, 0.4022995440973937, 0.4104443894945374, 0.37144170020382455]  # ProxEmbed2
    Y6 = [0.6688340708259806, 0.6274915826466256, 0.6720513709936207, 0.7457815125543222, 0.6974879943936839]  # D2AGE2
    # fig, ax = plt.subplots()
    index = np.arange(5) + 1
    bar_width = 0.1
    opacity = 0.8
    rec1 = plt.bar(index, Y1, bar_width, alpha=opacity, color='w', edgecolor='k', label='ESER', hatch='/')
    rec2 = plt.bar(index + bar_width, Y2, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='RelSim', hatch='\\')
    rec3 = plt.bar(index + 2*bar_width, Y3, bar_width, alpha=opacity, color='w', edgecolor='k', label='Exaustive', hatch='|')
    rec4 = plt.bar(index + 3*bar_width, Y4, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='Heuristic', hatch='-')
    rec5 = plt.bar(index + 4*bar_width, Y5, bar_width, alpha=opacity, color='w', edgecolor='k', label='ProxEmbed2', hatch='.')
    rec6 = plt.bar(index + 5*bar_width, Y6, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='D2AGE2', hatch='o')

    # autolabel(rec1)
    # autolabel(rec2)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    plt.xticks(index + 2.5*bar_width, (r'$|\Lambda|=1$', r'$|\Lambda|=2$', r'$|\Lambda|=3$', r'$|\Lambda|=4$', r'$|\Lambda|=5$'), fontsize=fontsz)
    # plt.tick_params(axis='x', which='major', labelsize=36)
    # plt.xlabel('Samples Number', fontsize=30)
    # plt.ylabel('NDCG@10 on DBpedia', fontsize=fontsz)
    plt.legend(ncol=6, loc=(-0.01, 1.03), fontsize=fontsz)

    plt.subplot(2, 2, 2)

    Y1 = [0.800648069367524, 0.8635157993677471, 0.8845663465820627, 0.9190899674261537, 0.9197634051682216]  # ESER
    Y2 = [0.6193719194440509, 0.6640321068782934, 0.6737672317281317, 0.682342856994134, 0.6815494865900765]  # RelSim
    Y3 = [0.887584954557054, 0.9400757500946948, 0.9456322934877734, 0.9578878958141277, 0.9622016703754954]  # Exaustive
    Y4 = [0.8507152052329173, 0.9455222704636731, 0.9616825138671001, 0.9591644167000222, 0.98072501777296]  # Heuristic
    Y5 = [0.4509009379243352, 0.41250269646362714, 0.4205603757061974, 0.4416644085892196, 0.40060976269770376]  # ProxEmbed2
    Y6 = [0.648439483534206, 0.636849181879786, 0.675147537012236, 0.7357155117181435, 0.6978377284738132]  # D2AGE2
    # fig, ax = plt.subplots()
    index = np.arange(5) + 1
    bar_width = 0.1
    opacity = 0.8
    rec1 = plt.bar(index, Y1, bar_width, alpha=opacity, color='w', edgecolor='k', label='ESER', hatch='/')
    rec2 = plt.bar(index + bar_width, Y2, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='RelSim', hatch='\\')
    rec3 = plt.bar(index + 2*bar_width, Y3, bar_width, alpha=opacity, color='w', edgecolor='k', label='Exaustive', hatch='|')
    rec4 = plt.bar(index + 3*bar_width, Y4, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='Heuristic', hatch='-')
    rec5 = plt.bar(index + 4*bar_width, Y5, bar_width, alpha=opacity, color='w', edgecolor='k', label='ProxEmbed2', hatch='.')
    rec6 = plt.bar(index + 5*bar_width, Y6, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='D2AGE2', hatch='o')

    # autolabel(rec1)
    # autolabel(rec2)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    plt.xticks(index + 2.5*bar_width, (r'$|\Lambda|=1$', r'$|\Lambda|=2$', r'$|\Lambda|=3$', r'$|\Lambda|=4$', r'$|\Lambda|=5$'), fontsize=fontsz)
    # plt.tick_params(axis='x', which='major', labelsize=36)
    # plt.xlabel('Samples Number', fontsize=45)
    # plt.ylabel('NDCG@20 on DBpedia', fontsize=fontsz)

    plt.subplot(2, 2, 3)

    Y1 = [0.4741883195687049, 0.555323940324946, 0.55043329042543, 0.5657737164464746, 0.572017569808552]  # ESER
    Y2 = [0.30283725979704357, 0.3022397572872931, 0.31436868924965633, 0.30826984446812156, 0.28638330735289547]  # RelSim
    Y3 = [0.3470510181397143, 0.6210910761178963, 0.6423057330710209, 0.6731353447753559, 0.6935277429685933]  # Exaustive
    Y4 = [0.3051673935979549, 0.770060218300515, 0.8429259628861534, 0.8730956976341173, 0.8801387020343223]  # Heuristic
    Y5 = [0.5, 0.5, 0.5, 0.5, 0.5]  # ProxEmbed2
    Y6 = [0.5, 0.5, 0.5, 0.5, 0.5]  # D2AGE2
    # fig, ax = plt.subplots()
    index = np.arange(5) + 1
    bar_width = 0.1
    opacity = 0.8
    rec1 = plt.bar(index, Y1, bar_width, alpha=opacity, color='w', edgecolor='k', label='ESER', hatch='/')
    rec2 = plt.bar(index + bar_width, Y2, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='RelSim', hatch='\\')
    rec3 = plt.bar(index + 2*bar_width, Y3, bar_width, alpha=opacity, color='w', edgecolor='k', label='Exaustive', hatch='|')
    rec4 = plt.bar(index + 3*bar_width, Y4, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='Heuristic', hatch='-')
    rec5 = plt.bar(index + 4*bar_width, Y5, bar_width, alpha=opacity, color='w', edgecolor='k', label='ProxEmbed2', hatch='.')
    rec6 = plt.bar(index + 5*bar_width, Y6, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='D2AGE2', hatch='o')

    # autolabel(rec1)
    # autolabel(rec2)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    plt.xticks(index + 2.5*bar_width, (r'$|\Lambda|=1$', r'$|\Lambda|=2$', r'$|\Lambda|=3$', r'$|\Lambda|=4$', r'$|\Lambda|=5$'), fontsize=fontsz)
    # plt.tick_params(axis='x', which='major', labelsize=36)
    # plt.xlabel('Methods')
    # plt.ylabel('NDCG@10 on Yplt.subplots_adjust(left=0.09,right=1,wspace=0.25,hspace=0.25,bottom=0.13,top=0.91)AGO', fontsize=fontsz)

    plt.subplot(2, 2, 4)

    Y1 = [0.4947688452150338, 0.5601029796749774, 0.551756393581186, 0.5740300425952664, 0.5834285789365312]  # ESER
    Y2 = [0.30173941069472326, 0.31172223896747603, 0.32395599694848765, 0.31773385511188074, 0.303239012162424]  # RelSim
    Y3 = [0.3220593291979994, 0.5785006249390053, 0.6110023388784728, 0.6564985861150462, 0.6678375830310631]  # Exaustive
    Y4 = [0.301958636717098, 0.6799979657080398, 0.7667453735641845, 0.8221196083879466, 0.8382884695139756]  # Heuristic
    Y5 = [0.5, 0.5, 0.5, 0.5, 0.5]  # ProxEmbed2
    Y6 = [0.5, 0.5, 0.5, 0.5, 0.5]  # D2AGE2
    # fig, ax = plt.subplots()
    index = np.arange(5) + 1
    bar_width = 0.1
    opacity = 0.8
    rec1 = plt.bar(index, Y1, bar_width, alpha=opacity, color='w', edgecolor='k', label='ESER', hatch='/')
    rec2 = plt.bar(index + bar_width, Y2, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='RelSim', hatch='\\')
    rec3 = plt.bar(index + 2*bar_width, Y3, bar_width, alpha=opacity, color='w', edgecolor='k', label='Exaustive', hatch='|')
    rec4 = plt.bar(index + 3*bar_width, Y4, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='Heuristic', hatch='-')
    rec5 = plt.bar(index + 4*bar_width, Y5, bar_width, alpha=opacity, color='w', edgecolor='k', label='ProxEmbed2', hatch='.')
    rec6 = plt.bar(index + 5*bar_width, Y6, bar_width, alpha=opacity, color='w', edgecolor='k',
                   label='D2AGE2', hatch='o')

    # autolabel(rec1)
    # autolabel(rec2)
    plt.ylim(0, 1)
    plt.yticks(fontsize=fontsz)
    plt.xticks(index + 2.5*bar_width, (r'$|\Lambda|=1$', r'$|\Lambda|=2$', r'$|\Lambda|=3$', r'$|\Lambda|=4$', r'$|\Lambda|=5$'), fontsize=fontsz)
    # plt.tick_params(axis='x', which='major', labelsize=36)
    # plt.ylabel('NDCG@20 on YAGO', fontsize=fontsz)
    plt.subplots_adjust(left=0.05,right=0.99,wspace=0.15,hspace=0.15,bottom=0.10,top=0.90)
    # plt.show()
    plt.savefig('C:\\Users\\lzy96\\Desktop\\histo.pdf')

draw_Histogram()
