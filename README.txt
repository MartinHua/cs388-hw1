-------------------------------------------------
README : CS388 HW1
-------------------------------------------------

---------------------------
AUTHOR
---------------------------
 Xinrui Hua, UT EID: xh3426
 
-------------------------------
EXECUTING CODE FOR TESTING
-------------------------------
1. After you unzip the project folder I sent you navigate to project path in command line:  /"path to project folder"/xinrui_hua_xh3426/.
2. Run in command line: 
    BackwardBigramModel:
      java -cp ./ lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1 >> BackwardBigramModel-trace.txt
      java -cp ./ lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/wsj/ 0.1 >> BackwardBigramModel-trace.txt
      java -cp ./ lm.BackwardBigramModel /projects/nlp/penn-treebank3/tagged/pos/brown/ 0.1 >> BackwardBigramModel-trace.txt
    BidirectionalBigramModel:
      java -cp ./ lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/atis/ 0.1 >> BidirectionalBigramModel-trace.txt
      java -cp ./ lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/wsj/ 0.1 >> BidirectionalBigramModel-trace.txt
      java -cp ./ lm.BidirectionalBigramModel /projects/nlp/penn-treebank3/tagged/pos/brown/ 0.1 >> BidirectionalBigramModel-trace.txt
