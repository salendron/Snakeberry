import tornado.web, subprocess
from snakeberryJSON import *
from common import Message

class SetVolume(tornado.web.RequestHandler):
    def get(self,volume):
        subprocess.call(['amixer', 'cset', 'numid=3', '--', volume])
        msg = Message(-1, "SetVolumne", "Volume " + volume)
        self.write(SnakeberryJSON().encode(msg))