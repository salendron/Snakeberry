import subprocess, signal, os

class MplayerProcess():
    def __init__(self, mediaType, description, mediaURI):
        self.PID = -1
        self.MediaType = mediaType
        self.Description = description
        self.MediaURI = mediaURI

class Mplayer():
    currentProcess = None
    
    @staticmethod
    def play(process):
        Mplayer.stop()
        Mplayer.currentProcess = process
        pipe = subprocess.Popen(['mplayer',Mplayer.currentProcess.MediaURI])
        Mplayer.currentProcess.PID = pipe.pid
    
    @staticmethod
    def stop():
        if(Mplayer.currentProcess != None):
            os.kill(Mplayer.currentProcess.PID, signal.SIGKILL)
            Mplayer.currentProcess = None
    
    

