import re
import sys
import xml.etree.ElementTree as ET

class Entity:
    kind = ""
    #topneighbor = -1
    #bottomneighbor = -1
    absPos = -1
    indexPos = -1
    ent = ""

    def __init__(self, strn, position):
        ent = strn
        absPos = position
    def __init__(self, strn, position, index, k):
        self.ent = strn
        self.absPos = position
        self.indexPos = index
        self.kind = k
        
    def setpreceding(self,index):
        self.topneighbor = index
    def setpostceding(self, index):
        self.bottomneighbor = index


    def __repr__(self):
        return str(self.indexPos)
    def __str__(self):
        return (self.ent + ":\n"
        + "\tPercentage Into Paper:"+ str(self.absPos)
        + "\n\tFound as number: " + str(self.indexPos)+"\n" )


f = open(sys.argv[1],"r")
words = f.read().split()
## THE REGULAR EXPRESSION PATTERNS
genePat = re.compile("[A-Z]+[0-9]+")
headingPat = re.compile("[A-Z]+$")

textLength = len(words)
possibleEntities = []
possibleHeadings = []
clusters = []

## go through file and put entities and headings into the above lists
for ind,word in enumerate(words):
    index = float(ind)
    match = genePat.match(word) #how to get the pattern from this object?
    headingmatch = headingPat.match(word)
    #print ("match: " + str(match))
    if (genePat.match(word)):
        currentInd = len(possibleEntities)
        en = Entity(match.group(), (index/textLength),currentInd,"geneEntity")
        possibleEntities.append(en)
    if (headingPat.match(word)):
        currentInd = len(possibleHeadings)
        heading = Entity(headingmatch.group(), (index/textLength),currentInd,"headingEntity")
        possibleHeadings.append(heading)

        


## put together clusters of proximal entities
percentageQuantum = 1/float(textLength)
wordStride = 1.001 #doesn't catch adjacent entities with just 1
i = 0
#for i in range(len(possibleEntities)-1):
while(i<len(possibleEntities)-1):
    cluster = set()
    while (i<len(possibleEntities)-1):
        thisEn = possibleEntities[i]
        nextEn = possibleEntities[i+1]
        
        if( nextEn.absPos-thisEn.absPos <= (wordStride*percentageQuantum) ):
            print ("difference: " + str(nextEn.absPos-thisEn.absPos))
            cluster.add(nextEn)
            cluster.add(thisEn)
        elif( len(cluster) > 1 ):
            clusters.append(cluster)
            i+=1
            break
        i+=1


## just print some stuff to see        
for i in range (len(possibleEntities)):
    print possibleEntities[i]
print("\n\n")
for h in possibleHeadings:
    print h
print("the following genes clustered within " +str(wordStride)
    +" words of one another:")
for cluster in clusters:
    print(str(cluster)+"\n")
    
