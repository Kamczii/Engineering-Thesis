from flask import Flask
from flask import request

from db import build_weight_matrix
from matching import match_users_random, match_users_by_order
from perfect import perfect_match_set
from test import Test

app = Flask(__name__)


@app.route('/match')
def match():
    method = request.args.get('method')
    matrix = build_weight_matrix()
    if 'random' == method:
        return match_users_random(matrix)
    else:
        return match_users_by_order(matrix)


@app.route('/perfect-match')
def perfect():
    method = request.args.get('method')
    matrix = build_weight_matrix()
    return perfect_match_set(matrix, method)


if __name__ == "__main__":
    # app.run(port=8079, host='0.0.0.0')
    matrix = build_weight_matrix()
    Test().run_test2(20)