# Richard Xie 915505564

import math, collections

def bigram(sentence, i):
  word1 = sentence[i].word
  word2 = sentence[i+1].word
  return '%s %s' % (word1, word2)

class SmoothBigramModel:

  def __init__(self, corpus):
    """Initialize your data structures in the constructor."""
    self.bigramCounts = collections.defaultdict(lambda: 0)
    self.total = 0
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
      for i in range(0, len(sentence.data) - 1):
        token = bigram(sentence.data, i)
        self.bigramCounts[token] += 1
        self.total += 1
      # Unknown case
      self.bigramCounts['UNK'] = 0
      # Apply Add-one smoothing
      for token in self.bigramCounts:
        self.bigramCounts[token] += 1
        self.total += 1

  def score(self, sentence):
    """ Takes a list of strings as argument and returns the log-probability of the 
        sentence using your language model. Use whatever data you computed in train() here.
    """
    # TODO your code here
    score = 0.0 
    for i in range(0, len(sentence) - 1):
      token = '%s %s' % (sentence[i], sentence[i+1])
      # Need to consider unknown case
      count = self.bigramCounts[token] | self.bigramCounts['UNK']
      if count > 0:
        score += math.log(count)
        score -= math.log(self.total)
      #Ignore unseen words
    return score