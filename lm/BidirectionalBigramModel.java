package lm;

import java.io.*;
import java.util.*;

/** 
  * @author Xinrui Hua, Ray Mooney
  * A simple bigram language model that uses simple fixed-weight interpolation
  * with a unigram model for smoothing.  It's a "backward" bigram model that 
  * models the generation of a sentence from right to left.
*/

public class BidirectionalBigramModel {

    /** Unigram model that maps a token to its unigram probability */
    public BigramModel bigramModel = null; 

    /**  Bigram model that maps a bigram as a string "A\nB" to the
     *   P(B | A) */
    public BackwardBigramModel backwardBigramModel = null;

    /** Total count of tokens in training data */
    public double tokenCount = 0;
	
	/** Interpolation weight for bigram model */
	public double lambda1 = 0.5;
	
	/** Interpolation weight for backwardBigram model */
    public double lambda2 = 0.5;

    /** Initialize model with empty hashmaps with initial
     *  unigram entries for sentence start (<S>), sentence end (</S>)
     *  and unknown tokens */
    public BidirectionalBigramModel() {
		  bigramModel = new BigramModel();
		  backwardBigramModel = new BackwardBigramModel();
    }

    /** Train the model on a List of sentences represented as
     *  Lists of String tokens */
    public void train (List<List<String>> sentences) {
		bigramModel.train(sentences);
		backwardBigramModel.train(sentences);
	}

    /** Return bigram string as two tokens separated by a newline */
    public String bigram (String prevToken, String token) {
		return prevToken + "\n" + token;
    }

    /** Return fist token of bigram (substring before newline) */
    public String bigramToken1 (String bigram) {
		int newlinePos = bigram.indexOf("\n");
		return bigram.substring(0,newlinePos);
    }

    /** Return second token of bigram (substring after newline) */
    public String bigramToken2 (String bigram) {
		int newlinePos = bigram.indexOf("\n");
		return bigram.substring(newlinePos + 1, bigram.length());
    }


    /** Like test1 but excludes predicting end-of-sentence when computing perplexity */
    public void test2 (List<List<String>> sentences) {
		double totalLogProb = 0;
		double totalNumTokens = 0;
		for (List<String> sentence : sentences) {
			totalNumTokens += sentence.size();
			double sentenceLogProb = sentenceLogProb2(sentence);
			totalLogProb += sentenceLogProb;
		}
		double perplexity = Math.exp(-totalLogProb / totalNumTokens);
		System.out.println("Word Perplexity = " + perplexity );
    }
    
    /** Like sentenceLogProb but excludes predicting end-of-sentence when computing prob */
    public double sentenceLogProb2 (List<String> sentence) {
		double sentenceLogProb = 0;
		double[] bigramTokenProbs = bigramModel.sentenceTokenProbs(sentence);
		double[] backwardBigramTokenProbs = backwardBigramModel.sentenceTokenProbs(sentence);
		for (int i = 0; i < sentence.size(); i++) {
			double logProb = Math.log(interpolatedProb(bigramTokenProbs[i], backwardBigramTokenProbs[sentence.size()-i-1]));
			sentenceLogProb += logProb;
		}
		return sentenceLogProb;
    }
	
	public double interpolatedProb(Double bigramVal, Double backwardBigramVal) {
		// Linearly combine weighted bigram and backwardBigram probs
		return lambda1 * bigramVal + lambda2 * backwardBigramVal;
    }

    public static int wordCount (List<List<String>> sentences) {
		int wordCount = 0;
		for (List<String> sentence : sentences) {
			wordCount += sentence.size();
		}
		return wordCount;
    }

    /** Train and test a bigram model.
     *  Command format: "lm.BidirectionalBigramModel [DIR]* [TestFrac]" where DIR 
     *  is the name of a file or directory whose LDC POS Tagged files should be 
     *  used for input data; and TestFrac is the fraction of the sentences
     *  in this data that should be used for testing, the rest for training.
     *  0 < TestFrac < 1
     *  Uses the last fraction of the data for testing and the first part
     *  for training.
     */
    public static void main(String[] args) throws IOException {
		// All but last arg is a file/directory of LDC tagged input data
		File[] files = new File[args.length - 1];
		for (int i = 0; i < files.length; i++) 
			files[i] = new File(args[i]);
		// Last arg is the TestFrac
		double testFraction = Double.valueOf(args[args.length -1]);
		// Get list of sentences from the LDC POS tagged input files
		List<List<String>> sentences = 	POSTaggedFile.convertToTokenLists(files);
		int numSentences = sentences.size();
		// Compute number of test sentences based on TestFrac
		int numTest = (int)Math.round(numSentences * testFraction);
		// Take test sentences from end of data
		List<List<String>> testSentences = sentences.subList(numSentences - numTest, numSentences);
		// Take training sentences from start of data
		List<List<String>> trainSentences = sentences.subList(0, numSentences - numTest);
		System.out.println("# Train Sentences = " + trainSentences.size() + 
				" (# words = " + wordCount(trainSentences) + 
				") \n# Test Sentences = " + testSentences.size() +
				" (# words = " + wordCount(testSentences) + ")");
		// Create a bigram model and train it.
		BidirectionalBigramModel model = new BidirectionalBigramModel();
		System.out.println("Training...");
		model.train(trainSentences);
		// Test on training data using test2
		model.test2(trainSentences);
		System.out.println("Testing...");
		// Test on test data using test2
		model.test2(testSentences);
    }

}
