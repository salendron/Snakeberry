## common.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.


#Return Value of every webservice method
#containing a numeric error code (0 if no error occured),
#and error message and a generic response data object.
#The response data object contains the data returned by the called method.
#Author: Bruno Hautzenberger
class Response:
    def __init__(self, errorCode, errorMessage, responseData):
        self.ErrorCode = errorCode
        self.ErrorMessage = errorMessage
        self.ResponseData = responseData
        
        
##ERRORCODES
errNumOk = 0
errMsgOk = ""

#services
errNumListServicesFailed = 1

#radio
errNumListRadioStationsFailed = 2

errNumRadioStationIdDoesNotExist = 3
errMsgRadioStationIdDoesNotExist = "Radio station with given id not found!"

errNumPlayRadioStationFailed = 4

errNumStopRadioStationFailed = 5

errNumSetVolumeFailed = 6
errNumGetVolumeFailed = 7

errNumGetMacFailed = 8
