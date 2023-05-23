import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
X_mean, Y_mean = 37.554812, 126.988204
X_std, Y_std = np.sqrt(0.0031548), np.sqrt(0.00720859)

a = np.array([(37.5549 - X_mean) / X_std, (127.0107 - Y_mean) / Y_std, (37.5549 - X_mean) / X_std, (127.0107 - Y_mean) / Y_std]).reshape(-1,2)
b = np.array([(37.5546 - X_mean) / X_std, (127.0105 - Y_mean) / Y_std, (37.5546 - X_mean) / X_std, (127.0105 - Y_mean) / Y_std]).reshape(-1,2)
d = cosine_similarity(a, b)
print(d)
print(np.mean(np.max(d, axis=0)))
X_mean, Y_mean = 37.554812, 126.988204
X_std, Y_std = np.sqrt(0.0031548), np.sqrt(0.00720859)
# 37.5546, 127.0105