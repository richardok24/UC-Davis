# Richard Xie 915505564
# Kneser-Ney Smoothing

import math, collections

def bigram(sentence, i):
  word1 = sentence[i].word
  word2 = sentence[i+1].word
  return '%s %s' % (word1, word2)

discount = 1

class CustomModel:

  def __init__(self, corpus):
    """Initial custom language model and structures needed by this mode"""
    self.unigramCounts = collections.defaultdict(lambda: 0)
    self.unigramTotal = 0
    self.bigramCounts = collections.defaultdict(lambda: 0)
    self.previousCounts = collections.defaultdict(lambda: 0)
    self.nextCounts = collections.defaultdict(lambda: 0)
    self.train(corpus)

  def train(self, corpus):
    """ Takes a corpus and trains your language model.
    """  
    # TODO your code here
    for sentence in corpus.corpus: # iterate over sentences in the corpus
      # Unigram
      for datum in sentence.data: # iterate over datums in the sentence
        token = datum.word # get the word
        self.unigramCounts[token] += 1
        self.unigramTotal += 1
        
      # Bigram
      for i in range(0, len(sentence.data) - 1):
        token = bigram(sentence.data, i)
        self.bigramCounts[token] += 1
        
    # Unigram with Add-one smoothing
    # Unknown case
    self.unigramCounts['UNK'] = 0
    # Apply Add-one smoothing
    for token in self.unigramCounts:
      self.unigramCounts[token] += 1
      self.unigramTotal += 1
        
    for unigram in self.unigramCounts:
      self.previousCounts[unigram] = wordsBeforeCalc(unigram, self.bigramCounts)
      self.nextCounts[unigram] = wordsAfterCalc(unigram, self.bigramCounts)

  def score(self, sentence):
    """ With list of strings, return the log-probability of the sentence with language model. Use
        information generated from train.
    """
    # TODO your code here
    score = 0.0 
    for i in range(0, len(sentence) - 1):
      bigramToken = '%s %s' % (sentence[i], sentence[i+1])
      bigramCount = self.bigramCounts[bigramToken]
      word1Unigram = sentence[i] if (self.unigramCounts[sentence[i]] > 0) else 'UNK'
      word2Unigram = sentence[i+1]
      
      # Calculations
      discountBigram = max(bigramCount - discount, 0) / (self.unigramCounts[word1Unigram] * 1.0)
      interpolationWeight = interpolationWeightCalc(word1Unigram, self.unigramCounts, self.nextCounts)
      continuationProb = continuationProbCal(word2Unigram, self.previousCounts, self.bigramCounts)
      KNprob = discountBigram + interpolationWeight + continuationProb
      score += math.log(KNprob + 0.00000000001)
    return score

def wordsBeforeCalc(word2Unigram, bigramCounts):
  numBefore = 0
  for bigramToken in bigramCounts:
    if bigramToken.endswith(word2Unigram):
      numBefore += 1
  return numBefore

def wordsAfterCalc(word1Unigram, bigramCounts):
  numAfter = 0
  for bigramToken in bigramCounts:
    if bigramToken.startswith(word1Unigram):
      numAfter += 1
  return numAfter
  
def interpolationWeightCalc(word1Unigram, unigramCounts, nextCounts):
  normDiscount = discount / (unigramCounts[word1Unigram] * 1.0)
  numAfter = nextCounts[word1Unigram]
  return normDiscount * numAfter
  
def continuationProbCal(word2Unigram, previousCounts, bigramCounts):
  return (previousCounts[word2Unigram] * 1.0) / (len(bigramCounts) * 2.0)
    