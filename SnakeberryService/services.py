## services.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.

import tornado.web
from uuid import getnode as get_mac
from snakeberryJSON import *
from common import *

#Representing a service (radio, etc...)
#Author: Bruno Hautzenberger
class Service:
    def __init__(self, displayName, baseUrl):
        self.DisplayName = displayName
        self.BaseUrl = baseUrl

#Holds a list of services
#Author: Bruno Hautzenberger        
class Services:
    def __init__(self):
        self.Services = []
        self.loadServices()
        
    def loadServices(self):
        self.Services.append(Service("Radio","/radio"))

#Webservice requesthandler to recieve list of services
#Author: Bruno Hautzenberger
class ListServices(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk
        
        try:
            services = Services()
            rObject = services
        except Exception, err:
            errMsg = str(err)
            errNum = errNumListServicesFailed
            
        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        
#Returns the Snakeberries Mac Adress
#Author: Bruno Hautzenberger
class GetMac(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk
        
        try:
            rObject = get_mac()
        except Exception, err:
            errMsg = str(err)
            errNum = errNumGetMacFailed
            
        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))