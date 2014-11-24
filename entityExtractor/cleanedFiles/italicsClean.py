import re
import sys

file = open(sys.argv[1],"r")
text = file.read()

def nothing(m):
    return ""
newtext = re.sub("<italic>|</italic>",nothing,text)
print(newtext)
