
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from NBayesUE import NBayesClassifier

# Leitura... 

data=pd.read_csv('/Users/gustavooliveira/Desktop/WORKSPACE/AA/T1-AA-49075-46395/Data/rice.csv')

# separar atributos da classificação : 

X = data.drop('class',axis=1)
y = data['class']

# data split : 

X_train, X_test, y_train, y_test = train_test_split(X, y,test_size=0.25, random_state=3)

Nbayes = NBayesClassifier()

Nbayes.fit(X_train,y_train)
print(Nbayes.predict(X_test))
print(Nbayes.score(X_test, y_test))

