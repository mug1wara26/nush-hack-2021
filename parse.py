import json
from urllib.parse import quote
import requests

f = open("data.txt","r").read()
t = json.loads(f)["dataText"]
data = []

fields = ['Score: Climate Change','Score: Water Security','Score: Forests Timber','Score: Forests Palm','Score: Forests Cattle Products','Score: Forests Soy']

score_map = {
        "A":9,
        "A-":8,
        "B":7,
        "B-":6,
        "C":5,
        "C-":4,
        "D":3,
        "D-":2,
        "E":1,
        "F":0,
        }

for i in t[:10]:
    d = {k:j[0] for j,k in zip(i,['id','Company Name','Country','Region','Sector: Climate Change', 'Score: Climate Change','Sector: Water Security','Score: Water Security','Sector: Forests','Score: Forests Timber','Score: Forests Palm','Score: Forests Cattle Products','Score: Forests Soy'])}
    score = {}
    for j in fields:
        if d[j] in score_map:
            score[j] = score_map[d[j]]
        else:
            score[j] = -1
    print(quote(d["Company Name"]),score,(lambda l:sum(l)/len(l) if len(l)!=0 else 0)([score[i] for i in score if score[i]!=-1]))

    #print(requests.get("http://172.105.114.129/add_product?data="+quote(json.dumps(data))).text)

data = list(set(data))
data.sort()
print(data)
