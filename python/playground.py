import numpy as np
from scipy import spatial

if __name__ == "__main__":
    u1 = np.array([1, 3, 0, 0, 12])
    u2 = np.array([3, 9, 0, 0, 15])

    a1 = [1, 3, 0, 0, 5]
    a2 = [3, 9, 0, 0, 15]
    print(spatial.distance.cosine(a1, a2))
    print(spatial.distance.cosine(u1, u2))