import web
from json import JSONEncoder
        
urls = (
    '/(.*)', 'hello'
)
app = web.application(urls, globals())

class hello:        
    def GET(self, name):
        if not name: 
            name = 'World'
        t = test()
        t.wert = "hui"
        return MyEncoder().encode(t)

class test:
    def __init__(self):
        self.wert = "Hallo"

class MyEncoder(JSONEncoder):
    def default(self, o):
        return o.__dict__  

if __name__ == "__main__":
    app.run()

