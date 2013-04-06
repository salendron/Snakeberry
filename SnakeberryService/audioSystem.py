## audioSystem.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.

import tornado.web, subprocess
from snakeberryJSON import *
from common import *

#Webservice requesthandler to set system volume on raspberry pi as root
#Author: Bruno Hautzenberger
class SetVolume(tornado.web.RequestHandler):
    def get(self,volume):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            subprocess.call(['sudo', 'amixer', 'set', 'PCM', '--', volume]) #ROOT CALL
            #subprocess.call(['amixer', 'cset', 'numid=3', '--', volume]) #USER CALL
        except Exception, err:
            errMsg = str(err)
            errNum = errNumSetVolumeFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')

#Webservice requesthandler to retrieve system volume from raspberry pi as root
#Author: Bruno Hautzenberger
class GetVolume(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            p = subprocess.Popen(['sudo', 'amixer'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            volume = ""
            while(True):
                retcode = p.poll() #returns None while subprocess is running
                line = p.stdout.readline()

                if(line.find("Mono") != -1):
                    volume = line.split()[2]

                if(retcode is not None):
                    break

            rObject = int(volume)
        except Exception, err:
            errMsg = str(err)
            errNum = errNumGetVolumeFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')
