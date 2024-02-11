from matching import distance
from perfect_match import PerfectMatch


def perfect_match_set(matrix) -> [PerfectMatch]:
    user_ids = list(matrix.get_user_ids())
    matches = []
    for user_id in user_ids:
        other_users = [x for x in user_ids if x != user_id]
        distances = [(other_id, distance(matrix.get_user_row(user_id), matrix.get_user_row(other_id))) for
                     other_id in other_users]
        if distances:
            smallest_distance = min(distances, key=lambda t: t[1])[1]
            best_match_ids = [x[0] for x in distances if x[1] == smallest_distance]
            perfect_match = PerfectMatch(user_id, best_match_ids)
            matches.append(perfect_match)
            print(perfect_match.to_string())
        else:
            print(f"No match for user {user_id}")
            break
    return matches
