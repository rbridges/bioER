from lib import xmlExtract as e

output = open("results.html", "w")

output.write("<TABLE BORDER CELLPADDING=2>")
output.write(
    "<TR>" +
        "<TH> Entity </TH>")
for section in e.table[0]:
    output.write("<TH>"+str(section)+"</TH> ")
output.write("/<TR>")

for i,lis in enumerate(e.table):
    if(i==0):
        continue
    output.write("<TR>")
    for entry in lis:
        output.write("<TD>" +str(entry) +"</TD>")
    output.write("</TR>\n")

output.write("</TABLE>")



