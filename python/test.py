import glob
import os
import pickle
import random
import shutil

from db import build_weight_matrix
from glboals import FOLDER
from matching import distance
from perfect import perfect_match_set
from perfect_match import PerfectMatch
from tabulate import tabulate

from plot import save_score_plot


def prepare_fs():
    if os.path.exists(f"{FOLDER}"):
        shutil.rmtree(f"{FOLDER}")
    os.mkdir(f"{FOLDER}")
    os.mkdir(f"{FOLDER}/matches")


class Test:
    def __init__(self):
        prepare_fs()
        self.matrix = build_weight_matrix()
        self.perfect_matches: [PerfectMatch] = perfect_match_set(self.matrix)
        self.write_perfect_to_file()
        self.matches: (int, int) = []
        self.scores = []

    def run_test2(self, iterations: int):
        user_ids = list(self.matrix.get_user_ids())
        best_matches = []
        for user_id in user_ids:
            other_ids = [other_id for other_id in user_ids if other_id != user_id]
            distances = [(other_id, distance(self.get_user_row(user_id), self.get_user_row(other_id))) for
                         other_id in
                         other_ids]
            for dst in distances:
                best_matches.append((user_id, dst[0], dst[1]))
        best_matches.sort(key=lambda x: x[2])

        for _ in range(iterations):
            unmatched_users_ids = list(self.matrix.get_user_ids())
            for user_id, other_id, dist in best_matches:
                if user_id in unmatched_users_ids and other_id in unmatched_users_ids and other_id not in self.get_current_matches(user_id):
                    self.matches.append((user_id, other_id))
                    unmatched_users_ids.remove(user_id)
                    unmatched_users_ids.remove(other_id)

            scr = self.test_get_score()
            self.scores.append(scr)

        print("Scores: ", self.scores)
        print("Matches: ", len(self.matches))
        self.write_score_to_file()

    def run_test(self, iterations: int):
        for i in range(iterations):
            self.run_match_phase()
            scr = self.test_get_score()
            self.scores.append(scr)
        print("Scores: ", self.scores)
        print("Matches: ", len(self.matches))
        self.write_score_to_file()

    def run_match_phase(self):
        unmatched_users_ids = list(self.matrix.get_user_ids())
        while unmatched_users_ids:
            user_id = random.choice(unmatched_users_ids)
            user_matches = self.get_current_matches(user_id)
            other_unmatched_users = [x for x in unmatched_users_ids if x not in ([user_id] + user_matches)]
            distances = [(other_id, distance(self.get_user_row(user_id), self.get_user_row(other_id))) for
                         other_id in
                         other_unmatched_users]
            if distances:
                best_similarity = min(distances, key=lambda t: t[1])[1]
                best_match_id = min(distances, key=lambda t: t[1])[0]
                self.matches.append((user_id, best_match_id))
                self.write_similarity(user_id, best_match_id, best_similarity)
                unmatched_users_ids.remove(user_id)
                unmatched_users_ids.remove(best_match_id)
            else:
                print(f"No match for user {user_id}")
                break

    def get_current_matches(self, user_id):
        return [next(y for y in [x[0], x[1]] if y != user_id) for x in self.matches if
                x[0] == user_id or x[1] == user_id]

    def get_user_row(self, user_id: int):
        return self.matrix.get_user_row(user_id)

    def write_score_to_file(self):
        with open(f"{FOLDER}/scores.txt", "w") as txt_file:
            for line in self.scores:
                txt_file.write(str(line) + "\n")
            txt_file.write("TOTAL AVERAGE: " + str(sum(self.scores) / len(self.scores)))
        save_score_plot(self.scores)

    def write_perfect_to_file(self):
        with open(f"{FOLDER}/perfect_matches.txt", "w") as txt_file:
            for perfect in self.perfect_matches:
                txt_file.write(perfect.to_string())

    def test_get_score(self):
        perfects = 0
        for perfect in self.perfect_matches:
            user_id = perfect.get_user_id()
            matches = self.get_current_matches(user_id)
            if any(perfect.has_match(other_id) for other_id in matches):
                perfects += 1
        return perfects / len(self.perfect_matches)

    def write_similarity(self, user_id, best_match_id, distance):
        with open(f"{FOLDER}/matches/{user_id}_{best_match_id}.txt", "w") as txt_file:
            txt_file.write(f"Similarity between users {user_id} and {best_match_id} is {distance}\n")
            txt_file.write(tabulate([self.get_user_row(user_id), self.get_user_row(best_match_id)],
                                    headers=self.matrix.get_labels(), tablefmt="grid"))
