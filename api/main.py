from flask import Flask
from flask_restful import Resource, Api, reqparse
import ast, random

app = Flask(__name__)
api = Api(app)

# Initialize statemachine constants,variables and locations

id2pwd = dict()
id2loggedin = dict()
loggedin_users = 0

MAX_REGISTERED_USERS = 2
MAX_LOGGEDIN_USERS = 1000000


# Endpoints

class Register(Resource):
	def post(self):
		global MAX_REGISTERED_USERS
		global id2loggedin
		global id2pwd

		parser = reqparse.RequestParser() # initialize
		parser.add_argument('uid', required=True)  # add args
		args = parser.parse_args()
		uid = int(args['uid'])

		if ((not (uid in id2pwd)) and len(id2pwd) < MAX_REGISTERED_USERS):
			pwd = random.randint(100000, 1000000)
			id2pwd[uid] = pwd
			id2loggedin[uid] = False;
		
			return pwd, 200
		else:
			return "failed", 200
        
class Login(Resource):
	def post(self):
		global loggedin_users
		global MAX_LOGGEDIN_USERS
		global id2loggedin
		global id2pwd

		parser = reqparse.RequestParser() # initialize
		parser.add_argument('uid', required=True)
		parser.add_argument('pwd', required=True)
		args = parser.parse_args()
		uid = int(args['uid'])
		pwd = int(args['pwd'])
		
		if ((uid in id2pwd) and (not id2loggedin[uid]) and pwd == id2pwd[uid] and loggedin_users < MAX_LOGGEDIN_USERS):
			loggedin_users = loggedin_users + 1
			id2loggedin[uid] = True

			return 1, 200
		else:
			return 0, 200

class Logout(Resource):
	def post(self):
		global id2loggedin
		global loggedin_users

		parser = reqparse.RequestParser() # initialize
		parser.add_argument('uid', required=True)  # add args
		args = parser.parse_args()
		uid = int(args['uid'])

		if ((uid in id2loggedin) and id2loggedin[uid]):
			id2loggedin[uid] = False
			loggedin_users = loggedin_users - 1
			return 1, 200
		else:
			return 0, 200

class ChangePassword(Resource):
	def post(self):
		global id2loggedin
		global id2pwd

		parser = reqparse.RequestParser() # initialize
		parser.add_argument('uid', required=True)  # add args
		args = parser.parse_args()
		uid = int(args['uid'])
		
		if ((uid in id2loggedin) and id2loggedin[uid]):
			pwd = random.randint(100000, 1000000)
			id2pwd[uid] = pwd
			return pwd, 200
		else:
			return "failed", 200

class Reset(Resource):
	def post(self):
		global id2pwd
		global id2loggedin
		global loggedin_users

		id2pwd = dict()
		id2loggedin = dict()
		loggedin_users = 0

api.add_resource(Register, '/register')
api.add_resource(Login, '/login')
api.add_resource(Logout, '/logout')
api.add_resource(ChangePassword, '/change-password')
api.add_resource(Reset, '/reset')


# Run server
if __name__ == '__main__':
	app.run(debug = False)  # run our Flask app

