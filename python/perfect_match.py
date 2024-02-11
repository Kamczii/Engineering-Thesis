class PerfectMatch:
    def __init__(self, user_id: int, match_ids: [int]):
        self.user_id = user_id
        self.match_ids = match_ids

    def get_user_id(self) -> int:
        return self.user_id

    def has_match(self, other_user_id: int) -> bool:
        return other_user_id in self.match_ids

    def to_string(self):
        return f"Best match for user({self.user_id}) are users({self.match_ids})\n"
