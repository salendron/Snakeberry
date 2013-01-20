import tornado.web, subprocess
from snakeberryJSON import *
from common import Message

class SetVolume(tornado.web.RequestHandler):
    def get(self,volume):
        subprocess.call(['sudo', 'amixer', 'set', 'PCM', '--', volume])
        #subprocess.call(['amixer', 'cset', 'numid=3', '--', volume])
        msg = Message(-1, "SetVolumne", "Volume " + volume)
        self.write(SnakeberryJSON().encode(msg))
        
class GetVolume(tornado.web.RequestHandler):
    def get(self):
        p = subprocess.Popen(['sudo', 'amixer'], stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        volume = ""
        while(True):
            retcode = p.poll() #returns None while subprocess is running
            line = p.stdout.readline()
            
            if(line.find("Mono") != -1):
                volume = line.split()[2]
            
            if(retcode is not None):
                break
            
        msg = Message(-1, "GetVolumne", int(volume))
        self.write(SnakeberryJSON().encode(msg))