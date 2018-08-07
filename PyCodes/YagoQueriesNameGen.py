import os

def walkfile(rootDir):
    for lists in os.listdir(rootDir):
        path = os.path.join(rootDir, lists)
        if os.path.isdir(path):
            walkfile(path)
        else:
            datard = open(path, 'r')
            datas = datard.readlines()
            outdata.write(datas[0])
            outdata.write(datas[1] + '\n')
            outname.write(str(path)[35:44] + '\n')
            datard.close()

outdata = open('C:\\Users\\lzy96\\Desktop\\data.txt', 'w')
outname = open('C:\\Users\\lzy96\\Desktop\\name.txt', 'w')
walkfile('C:\\Users\\lzy96\\Desktop\\YagoQueries')
outdata.close()
outname.close()