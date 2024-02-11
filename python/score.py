from db import get_user_matches


def get_score(matrix, perfect_matches):
    perfects = 0
    for idx, (user_id, perfect_friend_id) in enumerate(perfect_matches):
        matches = get_user_matches(user_id)
        if perfect_friend_id in matches:
            perfects += 1
            print(f"PERFECT {idx} of {len(perfect_matches)}")
    return perfects / len(perfect_matches)
