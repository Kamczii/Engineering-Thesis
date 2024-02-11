import random
import sys

import numpy as np
from scipy import spatial

from db import save, get_user_matches


def match_users_random(matrix):
    matches = get_new_match_set_random(matrix)
    save(matches)
    return matches


def match_users_by_order(matrix):
    matches = get_new_match_set_by_order(matrix)
    save(matches)
    return matches


def get_new_match_set_random(matrix):
    unmatched_users_ids = list(matrix.get_user_ids())
    matches = []
    while unmatched_users_ids:
        user_id = random.choice(unmatched_users_ids)
        user_matches = get_user_matches(user_id)
        other_unmatched_users = [x for x in unmatched_users_ids if x not in ([user_id] + user_matches)]
        distances = [(other_id, distance(matrix.get_user_row(user_id), matrix.get_user_row(other_id))) for
                     other_id in
                     other_unmatched_users]
        if distances:
            best_match_id = min(distances, key=lambda t: t[1])[0]
            matches.append((user_id, best_match_id))
            unmatched_users_ids.remove(user_id)
            unmatched_users_ids.remove(best_match_id)
        else:
            print(f"No match for user {user_id}")
            break
    return matches


def get_new_match_set_by_order(matrix):
    user_ids = list(matrix.get_user_ids())
    best_matches = []
    for user_id in user_ids:
        other_ids = [other_id for other_id in user_ids if other_id != user_id]
        distances = [(other_id, distance(matrix.get_user_row(user_id), matrix.get_user_row(other_id))) for
                     other_id in
                     other_ids]
        for dst in distances:
            best_matches.append((user_id, dst[0], dst[1]))
    best_matches.sort(key=lambda x: x[2])

    matches = []
    unmatched_users_ids = user_ids
    for user_id, other_id, dist in best_matches:
        if user_id in unmatched_users_ids and other_id in unmatched_users_ids and other_id not in get_user_matches(user_id):
            matches.append((user_id, other_id))
            unmatched_users_ids.remove(user_id)
            unmatched_users_ids.remove(other_id)
    return matches


def distance(row1, row2):
    return spatial.distance.cosine(row1, row2)
