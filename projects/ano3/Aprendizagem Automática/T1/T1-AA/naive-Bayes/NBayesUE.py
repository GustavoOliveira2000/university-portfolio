import numpy as np

class NBayesClassifier:
    def __init__(self, suave=1e-9):
        self.suave = suave  

    def fit(self, X, y):
        
        X = np.array(X, dtype=float)
        y = np.array(y)
           
        self.classes = np.unique(y)
        self.mean = {}
        self.var = {}
        self.priors = {}

       
        for c in self.classes:
            X_c = X[y == c]
            self.mean[c] = np.mean(X_c, axis=0)
            self.var[c] = np.var(X_c, axis=0) + self.suave  
            self.priors[c] = X_c.shape[0] / X.shape[0]

    def _gaussian_likelihood(self, x, mean, var):
        
        x = np.array(x, dtype=float)
        mean = np.array(mean, dtype=float)
        var = np.array(var, dtype=float)

        exponent = np.exp(-((x - mean) ** 2) / (2 * var))
        return (1 / np.sqrt(2 * np.pi * var)) * exponent

    def _class_probabilities(self, x):
       
        probabilities = {}
        for c in self.classes:
            prior = np.log(self.priors[c])  
            likelihood = np.sum(np.log(self._gaussian_likelihood(x, self.mean[c], self.var[c])))
            probabilities[c] = prior + likelihood
        return probabilities

    def predict(self, X):
        
        X = np.array(X, dtype=float)
        
        predictions = []
        for x in X:
            probabilities = self._class_probabilities(x)
            predictions.append(max(probabilities, key=probabilities.get))
        return np.array(predictions)

    def score(self, X, y):
        
        predictions = self.predict(X)
        accuracy = np.mean(predictions == y)
        return accuracy