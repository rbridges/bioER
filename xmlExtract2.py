import xml.etree.ElementTree as ET
import sys
import re
from copy import deepcopy as dc

dataFile = open(sys.argv[1],"r")
dataString = dataFile.read()

def nothing(m):
    return ""
def comment(m):
    return "<!--" +str(m.group(0))+"-->"
newtext = re.sub("&#x[0-9|A-Z]{4};",comment,dataString)
newtext = re.sub("\\<ext-link.+\\</ext-link\\>",nothing,newtext)
newtext = re.sub("<italic>|</italic>",nothing,newtext)
newtext = re.sub("\\<xref.+\\</xref\\>",nothing,newtext)



#tree = ET.parse(sys.argv[1])
#root = tree.getroot()
root = ET.fromstring(newtext)


class Entity:
    kind = ""
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

    
genePat = re.compile("[A-Z][A-Z][A-Z|-|:]+([0-9]+)?")
possibleEntities = []

def r(path,root):
    path.append(str(root.tag))#+ "~"+str(root.attrib))
    #print(str(dc(path)) +str(root.attrib))
    for node in root:
        if(node.text == None):
            continue
        textLength = len(node.text.split())
        for ind,word in enumerate(node.text.split()):
            #raw_input(word)
            index = float(ind)
            match = genePat.match(word)
            #print ("match: " + str(match))
            if (genePat.match(word)):
                #print("\t\tmatched!")
                currentInd = len(possibleEntities)
                en = Entity(match.group(), (index/textLength),currentInd,dc(path)+[node.tag],"geneEntity")
                possibleEntities.append(en)
        r(dc(path),node)

#print(root.tag)
r([],root)
sameName = {}
sameSection = {}
killList = ["CONTRIBUTIONS","AUTHOR","RESULTS","DNA","METHODS","INTRODUCTION","QUERIES"]
for ent in possibleEntities:
    if ent.ent in killList:
        del ent
        continue
    if ent.ent not in sameName:
        nameRepeats = []
        sameName[ent.ent] = nameRepeats
    sameName[ent.ent].append(ent)

    if ",".join(ent.path) not in sameSection:
        sectionGroup = []
        sameSection[",".join(ent.path)] = sectionGroup
    sameSection[",".join(ent.path)].append(ent)



table = list()
sections = []
for ke in sameSection.keys():
    sections.append(ke)

table.append(sections)
for sn in sameName:
    sl=[sn]
    for ss in sameSection:
        sl.append(0)
    table.append(sl)
    


for i,sect in enumerate(sections):
    ii = i + 1
    for j,nam in enumerate(sameName):
        for ent in possibleEntities:
        #print(str(ent.ent) + " vs. " +str(table[j]
            if ent.ent == table[ii][0] and ",".join(ent.path) == sect:
                print("condition!")
                table[ii][j] = table[i][j]+1



            
for l in table:
    print(l)

    
    


        
        
