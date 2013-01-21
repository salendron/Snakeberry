## audioSystem.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.

import subprocess, signal, os

#Representing a running mplayer process
#Author: Bruno Hautzenberger
class MplayerProcess():
    def __init__(self, mediaType, description, mediaURI):
        self.PID = -1
        self.MediaType = mediaType
        self.Description = description
        self.MediaURI = mediaURI

#Holds the current MplayerProcess and has methods to start and stop mplayer
#Author: Bruno Hautzenberger
class Mplayer():
    #the current running mplayer process - none if no mplayer process is running
    currentProcess = None
    
    #starts mplayer with a given media URI
    #stops the running mplayer process
    #Author: Bruno Hautzenberger
    @staticmethod
    def play(process):
        Mplayer.stop()
        Mplayer.currentProcess = process
        pipe = subprocess.Popen(['mplayer',Mplayer.currentProcess.MediaURI])
        Mplayer.currentProcess.PID = pipe.pid
    
    #stops mplayer
    #Author: Bruno Hautzenberger
    @staticmethod
    def stop():
        if(Mplayer.currentProcess != None):
            os.kill(Mplayer.currentProcess.PID, signal.SIGKILL)
            Mplayer.currentProcess = None
    
    

