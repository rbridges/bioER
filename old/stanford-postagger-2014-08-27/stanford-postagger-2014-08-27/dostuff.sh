java -mx300m -classpath stanford-postagger.jar edu.stanford.nlp.tagger.maxent.MaxentTagger -model models/english-left3words-distsim.tagger -textFile sample-input.txt > sample-tagged.txt
python putLines.py
python queryDB.py