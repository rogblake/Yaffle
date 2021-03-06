
import csv, httplib, urllib

DBL_QUOTE="\""
COMMA = " , "
DEBUG_MODE=false
host_env="http://yaffle-2016.appspot.com"
host_env_port=80
player_spreadsheet_path="./YafflePlayers1516.csv"

all_players = None
conn = None


def initialize():
	global all_players
	if DEBUG_MODE:
		host_env="http://localhost"
		host_env_port=8888
	else:
		host_env="http://yaffle-2016.appspot.com"
		host_env_port=80		
	all_players=[]
	global conn
	conn=httplib.HTTPConnection(host_env, host_env_port)

	readPlayerData (player_spreadsheet_path)

def readPlayerData(playersFile):
	with open (playersFile, 'rU') as csvfile:
		playerreader = csv.reader(csvfile)
		plr = None
		rowCount = 0
		for row in playerreader:
			print row
			if (rowCount != 0) and (rowCount < 17):
				plr = createPlayerProfile (row)
				global all_players
				all_players.append(plr)
			rowCount = rowCount+1
			#print plr.name + "  "  + plr.id


def createPlayerProfile (row):
	i=0
	for r in row:
		print "row: " + str(i) + ": " + r
		i=i+1
		
	player = Player()
	if (len(row) > 12) and (row[0] != ''):
		player.name= row[1]
		player.email=row[0]
		player.nickname=row[3]
		player.id=row[5]
		player.pin=row[4]
		player.E1=row[6]
		player.E2=row[7]
		player.E3=row[8]
		player.E4=row[9]
		player.S1=row[10]
		player.S2=row[11]
		player.S3=row[12]
		player.S4=row[13]
	else:
		print "Error! csv data is corrupt"
		print "Length = " +  str(len(row)) + " Row zero = " + row[0] + "."
		
	return player

def updatePlayerTeams(player,id):
	print "===============Updating newly created players with team data"
	leagues = ['E1','E2','E3','E4','S1','S2','S3','S4']
	if None != player:
		for lc in leagues:
			player.updateTeam(lc,id)
	else:
		"Error: Player does not exist"
		

def createPlayers():
	for p in all_players:
		id = createPlayer(p)
		
		print "Player Created with ID of " + id
		print "____Updating....________________"
		#updatePlayerTeams(p,id)

def createPlayer(player):
	targetUrl=host_env + ":" + host_port +  "/playerupdate/create"
	jsonData = player.toJSON()
	print "Sending json to " + host_env + "  on context path: " + targetUrl
	print "Sending following POST data : \n" + jsonData
	conn.request("POST", targetUrl, jsonData)
	response = conn.getresponse()
	conn.close()
	ret = response.read()
	print "response from createPlayer = " + ret
	print "response status = " , response.status, " " , response.reason	
	return ret

class Player:
	name = ""
	pin=0
	nickname=""
	email=""
	id=""
	pld="0"
	pos="0"
	gd="0"
	pts="0"
	E1=""
	E2=""
	E3=""
	E4=""
	S1=""
	S2=""
	S3=""
	S4=""

	def __init__(self):
		self.name = ""
		self.pin=0
		self.nickname=""
		self.email=""
		self.E1=""
		self.E2=""
		self.E3=""
		self.E4=""
		self.S1=""
		self.S2=""
		self.S3=""
		self.S4=""

	def updateTeam(self, lc, id):
		 targetTeam = getattr(self,lc) 
		 targetTeam = urllib.quote(targetTeam)
		 targetUrl =  "/admin/update/" + str(id) + "/" + lc + "/" + targetTeam
		 print self.nickname  + ' : ' + targetUrl
		 conn.request('PUT',targetUrl)
		 resp = conn.getresponse()
		 print str(resp.msg)
		 conn.close()
		
		
	def toJSON(self):
		ret ="{"
		ret += self.__addJSON__("id", 1)
		ret += COMMA
		ret += self.__addJSON__("nickname")
		ret +=COMMA
		ret += self.__addJSON__("email")
		ret +=COMMA
		ret += self.__addJSON__("name")
		ret +=COMMA
		ret += self.__addJSON__("pin")	
		ret +=COMMA
		ret += self.__addJSON__("pld",1)
		ret +=COMMA
		ret += self.__addJSON__("gd", 1)
		ret +=COMMA
		ret += self.__addJSON__("pts", 1)
		ret +=COMMA
		ret += self.__addJSON__("pos", 1)
		ret +=COMMA
		ret += self.__addJSON__("E1")
		ret +=COMMA
		ret += self.__addJSON__("E2")
		ret +=COMMA
		ret += self.__addJSON__("E3")
		ret +=COMMA
		ret += self.__addJSON__("E4")
		ret +=COMMA
		ret += self.__addJSON__("S1")
		ret +=COMMA
		ret += self.__addJSON__("S2")
		ret +=COMMA
		ret += self.__addJSON__("S3")
		ret +=COMMA
		ret += self.__addJSON__("S4")

		ret +="}"

		return ret

	def __addJSON__(self, propName, no_quotes=0):
		ret = ""
		propValue = getattr(self, propName)
		if no_quotes==1:
			ret = DBL_QUOTE + propName + DBL_QUOTE + ":" + str(propValue)
		else:	
			ret = DBL_QUOTE + str(propName) + DBL_QUOTE + ":" + DBL_QUOTE + str(propValue) + DBL_QUOTE 
		return ret

initialize()
createPlayers()


