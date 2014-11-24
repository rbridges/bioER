import xml.etree.ElementTree as ET
import sys
import re

dataFile = open(sys.argv[1],"r")
dataString = dataFile.read()
#tree = ET.parse(sys.argv[1])
#root = tree.getroot()
root = ET.fromstring(dataString)


class Entity:
    kind = ""
    #topneighbor = -1
    #bottomneighbor = -1
    absPos = -1
    indexPos = -1
    ent = ""
    path = ""

    def __init__(self, strn, position):
        ent = strn
        absPos = position
    def __init__(self, strn, position, index,p, k):
        self.ent = strn
        self.absPos = position
        self.indexPos = index
        self.path=p
        self.kind = k
        
    def setpreceding(self,index):
        self.topneighbor = index
    def setpostceding(self, index):
        self.bottomneighbor = index


    def __repr__(self):
        return (self.ent + ":\n"
        + "\tPath: "+str(self.path)
        + "\n\tPercentage Into Section:"+ str(self.absPos)
        + "\n\tFound as number: " + str(self.indexPos)+"\n" )        
    def __str__(self):
        return (self.ent + ":\n"
        + "\tPath: "+str(self.path)
        + "\n\tPercentage Into Section:"+ str(self.absPos)
        + "\n\tFound as number: " + str(self.indexPos)+"\n" )

    
genePat = re.compile("[A-Z]+[0-9]+")
possibleEntities = []

        
def nested():
    for child in root:
        for subchild in child:
            for sub2child in subchild:
                for sub3child in sub2child:
                    #print(str(sub2child) + ": ")
                    #print( "\t" + sub3child.tag + " " )#+ str(sub3child.text) )
                    #print("\t" + str(sub3child.text))
                    if(sub3child.text != None):
                        textLength = len(sub3child.text.split())
                        for ind,word in enumerate(sub3child.text.split()):
                            #print(word)
                            index = float(ind)
                            match = genePat.match(word)
                            #print ("match: " + str(match))
                            if (genePat.match(word)):
                                path = str(child.tag)+"->"+str(subchild.tag)+"->"+str(sub2child.tag) +"->"+str(sub3child.tag)
                                currentInd = len(possibleEntities)
                                en = Entity(match.group(), (index/textLength),currentInd,path,"geneEntity")
                                possibleEntities.append(en)
            #print("\n")

def byWord():
    #print(root.findall(".//abstract"))
    for paragraph in root.findall(".//xref"):
        print("para!: " + str(paragraph.text) +"\n\n")
        
byWord()
nested()
#print("ents: " +str(possibleEntities) )

sameName = {}
sameSection = {}
for ent in possibleEntities:
    #print(ent)
    if ent.ent not in sameName:
        nameRepeats = []
        sameName[ent.ent] = nameRepeats
    sameName[ent.ent].append(ent)

    if ent.path not in sameSection:
        sectionGroup = []
        sameSection[ent.path] = sectionGroup
    sameSection[ent.path].append(ent)

print("Uniques: \n")
for k in sameName:
    print(k + " has " +str(len(sameName[k])) + " repeats\n")


print("\n\n\n\n\nSection Groups: \n")
for k in sameSection:
    print(k + " has " +str(len(sameSection[k])) + " entities\n")
    

