import sys
from . import xmlExtract as e


dataFile = open(sys.argv[1],"r")
dataString = dataFile.read()

#gets rid of "mal-formed" xml
newDataString = e.cleanXML(dataString)

# parse (DFS) the xml tree and find re pattern matches
e.r([], e.getRoot(newDataString))

# label each of the candidates with a section / remove DNA, etc.
sameName, sameSection = e.metaDataEntities()

# put these into a table format
e.tableEm(sameName,sameSection)

