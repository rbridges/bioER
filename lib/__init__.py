import sys
from . import xmlExtract as e


dataFile = open(sys.argv[1],"r")
dataString = dataFile.read()

#gets rid of "mal-formed" xml
newDataString = e.cleanXML(dataString)

# parse (DFS) the xml tree and find re pattern matches
e.r([], e.getRoot(newDataString))

sameName, sameSection = e.metaDataEntities()

e.tableEm(sameName,sameSection)

