import xml.etree.ElementTree as ET
import sys
import re
from copy import deepcopy as dc


    
def nothing(m):
    return ""
def comment(m):
    return "<!--" +str(m.group(0))+"-->"

def cleanXML(dataString):
    newtext = re.sub("&#x[0-9|A-Z]{4};",comment,dataString)
    newtext = re.sub("\\<ext-link.+\\</ext-link\\>",nothing,newtext)
    newtext = re.sub("<italic>|</italic>",nothing,newtext)
    newtext = re.sub("\\<xref.+\\</xref\\>",nothing,newtext)
    newtext = re.sub("\\<\\?.+\\?\\>",nothing, newtext)
    return newtext

def getRoot(newtext):
    return ET.fromstring(newtext)
    
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
sectionsOrdered = []
def r(path,root):
    if(root.tag == "sec"):
        k = root.attrib.keys()
        append = root.attrib[k[0]]
        path.append(str(root.tag)+"="+str(append)+"<br>")
    else:
        path.append(str(root.tag))
        
    for node in root:
        if(node.text == None):
            continue
        textLength = len(node.text.split())
        for ind,word in enumerate(node.text.split()):
            index = float(ind)
            match = genePat.match(word)
            if (genePat.match(word)):

                # yes, confusing: path+[node.tag] ... it's the existing path plus the thing you are currently on
                if ",".join(path+[node.tag]) not in sectionsOrdered:
                    sectionsOrdered.append(",".join(path+[node.tag]) )
                    
                currentInd = len(possibleEntities)
                en = Entity(match.group(), (index/textLength),currentInd,dc(path)+[node.tag],"geneEntity")
                possibleEntities.append(en)
        r(dc(path),node)


#r([],root)


#possibleEntities was arg
def metaDataEntities():
    sameName = {}
    sameSection = {}
    killList = ["CONTRIBUTIONS","REPRODUCTIVE","AUTHOR","RESULTS","DNA",
                "METHODS","INTRODUCTION","QUERIES","DISCUSSION","MERISTEM"]
    deleteList = []
    for i,ent in enumerate(possibleEntities):
        if ent.ent in killList:
            #print(possibleEntities[i])
            deleteList.append(i)
            continue
        if ent.ent not in sameName:
            nameRepeats = []
            sameName[ent.ent] = nameRepeats
        sameName[ent.ent].append(ent)

        if ",".join(ent.path) not in sameSection:
            sectionGroup = []
            sameSection[",".join(ent.path)] = sectionGroup
        sameSection[",".join(ent.path)].append(ent)

 
    for i in reversed(deleteList):
        #print(possibleEntities[i])
        del possibleEntities[i]
            
            
        
        
    return sameName, sameSection


table = list()
def tableEm(sameName,sameSection):    
    
    sections = []
    for ke in sectionsOrdered:
        sections.append(ke)

    #print(str(sections) + "\n\n")
    #raw_input(sectionsOrdered)
    table.append(sections)
    for sn in sameName:
        sl=[sn]
        table.append(sl)
        for ss in sectionsOrdered:
            sl.append(0)
        
    sanityCheck = 0
    foundList = []
    for x,bogus in enumerate(possibleEntities):
     #   print(bogus.ent)
        foundList.append(x)
        
    for i,sect in enumerate(sections):
        ii = i + 1
        for j,nam in enumerate(sameName):
            jj = j + 1
            #raw_input()
            for k,ent in enumerate(possibleEntities):
                #print( str(",".join(ent.path)) + "    vs.   " +str(sect))
                if (ent.ent == table[jj][0] and ",".join(ent.path) == sect ): #
                    sanityCheck += 1
                    table[jj][ii] = int(table[jj][ii]+1)
                    foundList.remove(k)

    #print(str(sanityCheck) + "  " + str(len(possibleEntities)))


    #for y in foundList:
     #   print(possibleEntities[y])


    
    


        
        
