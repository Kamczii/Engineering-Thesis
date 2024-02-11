import numpy as np



class WeightMatrix:
    labels: dict[int, int]  # label_id => matrix column
    users: dict[int, int]  # user_id => matrix row
    matrix: np.array
    titles: dict[int, str]  # label_id => label

    def __init__(self, labels, users, texts, matrix):
        self.labels = labels
        self.users = users
        self.matrix = matrix
        self.titles = texts

    def get_user_ids(self) -> [int]:
        return self.users.keys()

    def get_users(self):
        return self.users

    def get_user_row(self, user_id):
        user_idx = self.users[user_id]
        return self.matrix[user_idx]

    def get_labels(self):
        return map(lambda label_id: self.titles[label_id],
                   [self.get_label_id_for_column(c) for c in range(len(self.labels))])

    def get_label_id_for_column(self, c):
        return [key for key in self.labels.keys() if self.labels[key] == c][0]
