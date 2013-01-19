import tornado.ioloop
import tornado.web
from snakeberryJSON import *

class Service:
    def __init__(self, displayName, baseUrl):
        self.DisplayName = displayName
        self.BaseUrl = baseUrl
        
class Services:
    def __init__(self):
        self.Services = []
        self.loadServices()
        
    def loadServices(self):
        self.Services.append(Service("Radio","/radio"))

class ListServices(tornado.web.RequestHandler):
    def get(self):
        services = Services()
        self.write(SnakeberryJSON().encode(services))

if __name__ == "__main__":
    application = tornado.web.Application([
        (r"/", ListServices),
    ])
    application.listen(8888)
    tornado.ioloop.IOLoop.instance().start()



