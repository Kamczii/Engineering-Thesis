import psycopg2
import numpy as np

from WeightMatrix import WeightMatrix


def get_user_matches(user_id) -> [int]:
    try:
        connection = create_connection()
        cursor = connection.cursor()
        cursor.execute(
            'SELECT CASE WHEN M.USER_ONE_ID = U.USER_ID THEN M.USER_TWO_ID ELSE M.USER_ONE_ID END FROM USERS U LEFT JOIN MATCHES M ON U.USER_ID IN (M.USER_ONE_ID, M.USER_TWO_ID) WHERE U.USER_ID = ' + str(
                user_id) + ' ORDER BY m.created_date desc;')
        user_ids = [r[0] for r in cursor.fetchall()]
        print(f"User {user_id} has {len(user_ids)} matches")
        return user_ids
    except (Exception, psycopg2.Error) as error:
        print("Error fetching users", error)
    finally:
        if connection:
            cursor.close()
            connection.close()


def get_users_dict():
    try:
        connection = create_connection()
        cursor = connection.cursor()
        cursor.execute(
            'SELECT DISTINCT user_id from label_weights lw join rekognition_labels rl on rl.label_id = lw.label_id and rl.active = true;')
        user_ids = cursor.fetchall()
        dic = dict()
        for count, user_id in enumerate(user_ids):
            dic[user_id[0]] = count
        return dic
    except (Exception, psycopg2.Error) as error:
        print("Error fetching users", error)
    finally:
        if connection:
            cursor.close()
            connection.close()


def get_labels_dict():
    try:
        connection = create_connection()
        cursor = connection.cursor()
        sql = ('select distinct rl.label_id from label_weights lw left join rekognition_labels rl on rl.label_id = '
               'lw.label_id where rl.active=true order by 1;')
        cursor.execute(sql)
        label_ids = cursor.fetchall()
        dic = dict()
        for count, label_id in enumerate(label_ids):
            dic[label_id[0]] = count
        return dic
    except (Exception, psycopg2.Error) as error:
        print("Error fetching labels", error)
    finally:
        if connection:
            cursor.close()
            connection.close()


def get_label_title_dict():
    try:
        connection = create_connection()
        cursor = connection.cursor()
        sql = 'select rl.label_id, rl."label" from rekognition_labels rl order by rl.label_id asc;'
        cursor.execute(sql)
        results = cursor.fetchall()
        dic = dict()
        for result in results:
            dic[result[0]] = result[1]
        return dic
    except (Exception, psycopg2.Error) as error:
        print("Error fetching labels", error)
    finally:
        if connection:
            cursor.close()
            connection.close()


def create_matrix(users, labels) -> WeightMatrix:
    try:
        connection = create_connection()
        cursor = connection.cursor()
        weights_query = "select lw.user_id, lw.label_id, lw.weight from label_weights lw join rekognition_labels rl on rl.label_id = lw.label_id and rl.active = true"
        cursor.execute(weights_query)
        weights = cursor.fetchall()
        tab = np.zeros((len(users), len(labels)), dtype=int)
        for row in weights:
            user_row = users[row[0]]
            label_column = labels[row[1]]
            tab[user_row][label_column] = row[2]
        texts = get_label_title_dict()
        return WeightMatrix(labels, users, texts, np.array(tab))
    except (Exception, psycopg2.Error) as error:
        print("Error fetching weights", error)
    finally:
        if connection:
            cursor.close()
            connection.close()


def build_weight_matrix():
    try:
        connection = create_connection()
        cursor = connection.cursor()

        users = get_users_dict()
        labels = get_labels_dict()
        matrix = create_matrix(users, labels)
        print(f"Created matrix for {len(users)} users and {len(labels)} labels")
        return matrix
    except (Exception, psycopg2.Error) as error:
        print("Error while fetching data from PostgreSQL", error)

    finally:
        if connection:
            cursor.close()
            connection.close()


def save(matches):
    try:
        connection = create_connection()
        cursor = connection.cursor()
        cursor.executemany("INSERT INTO public.matches (user_one_id, user_two_id) VALUES(%s, %s);", matches)
        connection.commit()
    except (Exception, psycopg2.Error) as error:
        print("Error while fetching data from PostgreSQL", error)
    finally:
        print(f"SAVED {len(matches)} matches")
        if connection:
            cursor.close()
            connection.close()


def create_connection():
    return psycopg2.connect(user="oad",
                            password="oad",
                            host="x.x.x.x",
                            port="5432",
                            database="once_a_day")
