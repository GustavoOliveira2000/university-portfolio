import numpy as np
from collections import Counter

class KNeighborsClassifier:

    #Construtor :
    def __init__(self, n_neighbors=3, p=1, metric="minkowski"): 
        self.n_neighbors = n_neighbors
        self.p = p
        self.metric = metric

    def fit(self, X, y):
        self.X_train = np.array(X, dtype=float)
        self.y_train = np.array(y)

    def kneighbors(self, X):
         
        neigh_dist = []
        neigh_indice = []
        distances = []
        i = 0 
        X = np.array(X, dtype=float)
        
        for x_test in X :
            
            
            for i, x_train in enumerate(self.X_train):

                if self.metric == "minkowski" :
                    dist = self.minkowski_compute(x_test, x_train, self.p)
                   
                
                distances.append((dist, i))
        
        minDistancesArray = self.KneighborsMinDistance(distances,X)

        return minDistancesArray
    
    def KneighborsMinDistance(self, distances, X_test):
        
        j = 0
        idcDist = 0 
        
        
        n, m = X_test.shape[0], self.n_neighbors

        
        Min = np.full((n, m), np.inf)
        MinIdx = np.full((n,m), -1) 

        for dist, idx in distances:
            
            if(j ==  self.X_train.shape[0]):
                idcDist =  idcDist + 1
                j = 0 


            for k in range(Min.shape[1]): # itera sob as colunas de Min 
                if(dist < Min[idcDist, k]):
                    Min[idcDist, k] = dist
                    MinIdx[idcDist, k] = idx
                    break
           
            j = j + 1 

        return (Min, MinIdx)

    def minkowski_compute(self,d1,d2,p):  
        return np.sum( np.abs(d1 - d2) ** p ) ** (1 / p)

    def predict(self, X):

        result = [None] * X.shape[0]
        nearstNeighbors = [None] * self.n_neighbors
        X = np.array(X, dtype=float)  
        neighbors = self.kneighbors(X)

        neigh_dist, neigh_indice = neighbors
              
        for i in range(X.shape[0]):

            for k in range(self.n_neighbors):
               nearstNeighbors[k] = self.y_train[ neigh_indice[i, k] - 1 ]
            result[i] = self.mostCommonString(nearstNeighbors)

        return result

     # métodod para determinar a string que aparece 
    #mais vezes no array de vizinhos de um tuplo  
    def mostCommonString(self, strings): 
        frequency = {}

        for string in strings:
            if string in frequency:
             frequency[string] += 1
            else:
             frequency[string] = 1

        return  max(frequency, key=frequency.get)
    
    def score(self, y, X):
        
        predictions = self.predict(X)
       
        correct_predictions = sum(pred == true for pred, true in zip(predictions, y))
    
       
        accuracy = correct_predictions / len(y)
    
        return accuracy