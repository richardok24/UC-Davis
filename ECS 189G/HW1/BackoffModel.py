# Richard Xie 915505564

import math, collections

def bigram(sentence, i):
  word1 = sentence[i].word
  word2 = sentence[i+1].word
  return '%s %s' % (word1, word2)

class BackoffModel:

  def __init__(self, corpus):
    """Initialize your data structures in the constructor."""
    self.unigramCounts = collections.defaultdict(lambda: 0)
    self.unigramTotal = 0
    self.bigramCounts = collections.defaultdict(lambda: 0)
    self.train(corpus)

  def train(self, corpus):
    """ Takes a corpus and trains your language model. 
        Compute any counts or other corpus statistics in this function.
    """  
    # TODO your code here
    # Tip: To get words from the corpus, try
    #    for sentence in corpus.corpus:
    #       for datum in sentence.data:  
    #         word = datum.word
    
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
        
    # Bigram with Add-one smoothing
    # Unknown case
    self.bigramCounts['UNK'] = 0
    # Apply Add-one smoothing
    for token in self.bigramCounts:
      self.bigramCounts[token] += 1

  def score(self, sentence):
    """ Takes a list of strings as argument and returns the log-probability of the 
        sentence using your language model. Use whatever data you computed in train() here.
    """
    # TODO your code here
    score = 0.0 
    for i in range(0, len(sentence) - 1):
      bigramToken = '%s %s' % (sentence[i], sentence[i+1])
      bigramCount = self.bigramCounts[bigramToken]
      previous = sentence[i]
      previousCount = self.unigramCounts[previous]
      if bigramCount > 0:
        score += math.log(bigramCount)
        score -= math.log(previousCount)
      else:
        unigramCount = self.unigramCounts[sentence[i+1]]
        score += math.log(unigramCount + 1)
        score -= math.log(self.unigramTotal)
        score += math.log(0.4)
    return score