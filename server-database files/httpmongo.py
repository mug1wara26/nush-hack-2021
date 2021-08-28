from http.server import HTTPServer, BaseHTTPRequestHandler
from urllib.parse import parse_qs
from pymongo import MongoClient
from fuzzywuzzy import process
import json

client = MongoClient()
db = client['nushhack2021']
users = db['users']
products = db['products']

company_names = [c["Company Name"] for c in products.find()] 

def add_user(user_data):
    return {"_id":str(users.insert_one(json.loads(user_data)).inserted_id)}

def get_user(userid):
    u = users.find_one({'uid':userid})
    if u!=None:
        u.pop('_id')
    return {"userExists":u!=None,"user":u}

def add_product(prod_data):
    company_names.append(prod_data["Company Name"])
    return {"_id":str(products.insert_one(json.loads(prod_data)).inserted_id)}

def get_product(company):
    cscore = 100
    if company not in company_names:
        company, cscore = process.extract(company,company_names,limit=1)[0]
    p = products.find_one({'Company Name':company})
    p.pop('_id')
    score = {}
    for j in fields:
        if p[j] in score_map:
            score[j] = score_map[p[j]]
        else:
            score[j] = -1
    return {"Company Name":company,"confidence":cscore,"score":(lambda l:round(100*sum(l)/len(l)/9) if len(l)!=0 else 0)([score[i] for i in score if score[i]!=-1]),"breakdown":score,"product":p}

def append_hist(data):
    data = json.loads(data)
    return {"modified_count":users.update_one({"uid":data['uid']},{"$push":{"history":data['hist']}}).modified_count}
 
fields = ['Score: Climate Change','Score: Water Security','Score: Forests Timber','Score: Forests Palm','Score: Forests Cattle Products','Score: Forests Soy']

score_map = {"A":9,"A-":8,"B":7,"B-":6,"C":5,"C-":4,"D":3,"D-":2,"E":1,"F":0}

def get_score(company):
    return get_product(company)

switcher = {
        "add_user":add_user,
        "get_user":get_user,
        "add_product":add_product,
        "get_product":get_product,
        "append_hist":append_hist,
        "get_score":get_score
        }

class Serv(BaseHTTPRequestHandler):
    def do_GET(self):
        if "?" in self.path:
            path,query = self.path[1:].split("?",1)
        else:
            path,query = self.path[1:],""
        query = parse_qs(query)
        qret = "Error: Unknown input"
        if path.lower() in switcher:
            qret=switcher[path.lower()](query["data"][0])
        print(qret)
        self.send_response(200)
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        self.wfile.write(json.dumps(qret).encode("utf-8"))


httpd = HTTPServer(('172.105.114.129',80),Serv)
httpd.serve_forever()
