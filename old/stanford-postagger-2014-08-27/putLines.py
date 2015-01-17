
postagged = open("sample-tagged.txt","r")
poses = open("poses.txt","w")

text = postagged.read()
text2 = text.split()
for line in text2:
    if "_NNP" in line:
        poses.write(line.replace("_NNP","\n"))
        continue
    if "_NNS" in line:
        poses.write(line.replace("_NNS","\n"))
        continue
    if "_NN" in line:
        poses.write(line.replace("_NN","\n"))

