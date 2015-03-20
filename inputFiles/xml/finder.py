import os

hits = []
nots = []

for a,b,f in os.walk("."):
    for fiLe in f:
        if "<body>" in open(fiLe,"r").read():
            hits.append(fiLe)
        else:
            nots.append(fiLe)
 
        


print len(hits)
print hits

print("\n"+str(len(nots)))
print nots
