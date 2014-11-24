import re
import sys

file = open(sys.argv[1],"r")
text = file.read()

def comment(m):
    return "<!--" +str(m.group(0))+"-->"
newtext = re.sub("&#x[0-9|A-Z]{4};",comment,text)
print(newtext)
