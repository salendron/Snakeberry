import tornado.web
from snakeberryJSON import *
from snakeberry import *
from mplayerInterface import *

class Radio:
    def __init__(self, streamUrl):
        self.StreamUrl = streamUrl
        
class Radios:
    def __init__(self):
        self.Radios = {}
        self.loadRadios()
        
    def loadRadios(self):
        self.Radios["OE3"] = Radio("mms://apasf.apa.at/OE3_Live_Audio")
        self.Radios["FM4"] = Radio("http://mp3stream1.apasf.apa.at:8000")

class ListRadios(tornado.web.RequestHandler):
    def get(self):
        radios = Radios()
        self.write(SnakeberryJSON().encode(radios))
        
class PlayRadio(tornado.web.RequestHandler):
    def get(self, radioId):
        radios = Radios()
        mplayer = MplayerProcess("Radio", radioId, radios.Radios[radioId].StreamUrl)
        Mplayer.play(mplayer)
        msg = Message(-1, "Radio", "Now playing " + radioId)
        self.write(SnakeberryJSON().encode(msg))
        
class StopRadio(tornado.web.RequestHandler):
    def get(self):
        Mplayer.stop()
        msg = Message(-1, "Radio", "Stopped")
        self.write(SnakeberryJSON().encode(msg))