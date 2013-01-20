import tornado.web
from snakeberryJSON import *
from common import Message
from mplayerInterface import *

class Radio:
    def __init__(self, radioId, streamUrl):
        self.RadioId = radioId
        self.StreamUrl = streamUrl
        
class Radios:
    def __init__(self):
        self.Radios = []
        self.loadRadios()
        
    def loadRadios(self):
        self.Radios.append(Radio("OE3", "mms://apasf.apa.at/OE3_Live_Audio"))
        self.Radios.append(Radio("FM4", "http://mp3stream1.apasf.apa.at:8000"))
        
class RadioInfo:
    def __init__(self, streamUrl):
        self.StreamUrl = streamUrl

class ListRadios(tornado.web.RequestHandler):
    def get(self):
        radios = Radios()
        self.write(SnakeberryJSON().encode(radios))
        
class PlayRadio(tornado.web.RequestHandler):
    def get(self, radioId):
        streamUrl = None
        for radio in Radios().Radios:
            if(radio.RadioId == radioId):
                streamUrl = radio.StreamUrl
                break
        
        mplayer = MplayerProcess("Radio", radioId, streamUrl)
        Mplayer.play(mplayer)
        msg = Message(-1, "Radio", "Now playing " + radioId)
        self.write(SnakeberryJSON().encode(msg))
        
class StopRadio(tornado.web.RequestHandler):
    def get(self):
        Mplayer.stop()
        msg = Message(-1, "Radio", "Stopped")
        self.write(SnakeberryJSON().encode(msg))
        
class RadioNowPlaying(tornado.web.RequestHandler):
    def get(self):
        mplayer = Mplayer.currentProcess
        if(mplayer == None):
            mplayer = MplayerProcess("Radio", "---", "")
        
        self.write(SnakeberryJSON().encode(mplayer))