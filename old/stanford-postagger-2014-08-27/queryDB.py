import httplib, urllib
import re

posTagged = open("poses.txt","r")
postxt = posTagged.read()
postxt2 = postxt.split("\n")

conn = httplib.HTTPConnection("eutils.ncbi.nlm.nih.gov",80)

for noun in postxt2:
    print(noun)
    
    conn.connect()
    #conn.request("POST", "/entrez/eutils/esearch.fcgi?db=gene&term=URA3&sort=relevance/gene?term=URA3[Gene%20Name]")
    conn.request("POST", "/entrez/eutils/esearch.fcgi?db=gene&term="+noun+"&sort=relevance/gene?term=URA3[Gene%20Name]")
    response = conn.getresponse()
    print response.read()
    conn.close()
    raw_input()
