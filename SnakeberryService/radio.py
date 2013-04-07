## radio.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.

import tornado.web, csv
from snakeberryJSON import *
from common import *
from mplayerInterface import *

base_dir = os.path.dirname(__file__) # should better be relative to config file

#Representing a radio station
#Author: Bruno Hautzenberger
class Radio:
    def __init__(self, radioId, displayName, streamUrl):
        self.RadioId = radioId
        self.DisplayName = displayName
        self.StreamUrl = streamUrl

#Representing a list of radio stations
#Author: Bruno Hautzenberger
class Radios:
    def __init__(self):
        self.Radios = []
        self.loadRadios()

    def loadRadios(self):
        filename = os.path.join(base_dir,'radio.csv')
        with open(filename, 'rb') as csvfile:
            radioreader = csv.reader(csvfile, delimiter=';', quotechar='\"')

            count = 0
            for row in radioreader:
                self.Radios.append(Radio(str(count), row[0], row[1]))
                count = count + 1

#Webservice requesthandler to recieve list radio stations
#Author: Bruno Hautzenberger
class ListRadios(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            radios = Radios()
            rObject = radios
        except Exception, err:
            errMsg = str(err)
            errNum = errNumListRadioStationsFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')

#Webservice requesthandler to play radio station with given id
#Author: Bruno Hautzenberger
class PlayRadio(tornado.web.RequestHandler):
    def get(self, radioId):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            streamUrl = None
            description = None
            for radio in Radios().Radios:
                if(radio.RadioId == radioId):
                    streamUrl = radio.StreamUrl
                    description = radio.DisplayName
                    break

            if(streamUrl == None):
                errNum = errNumRadioStationIdDoesNotExist
                errMsg = errMsgRadioStationIdDoesNotExist
            else:
                mplayer = MplayerProcess("Radio", description, streamUrl)
                Mplayer.play(mplayer)
        except Exception, err:
            errMsg = str(err)
            errNum = errNumPlayRadioStationFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')

#Webservice requesthandler to stop radio
#Author: Bruno Hautzenberger
class StopRadio(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            Mplayer.stop()
        except Exception, err:
            errMsg = str(err)
            errNum = errNumStopRadioStationFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')

#Webservice requesthandler to recieve what information about what the radio is playing
#Author: Bruno Hautzenberger
class RadioNowPlaying(tornado.web.RequestHandler):
    def get(self):
        rObject = None
        errNum = errNumOk
        errMsg = errMsgOk

        try:
            rObject = Mplayer.currentProcess
            if(rObject == None):
                rObject = MplayerProcess("Radio", "---", "")
        except Exception, err:
            errMsg = str(err)
            errNum = errNumStopRadioStationFailed

        self.write(SnakeberryJSON().encode(Response(errNum, errMsg, rObject)))
        self.set_header('Content-Type','application/json')
