## audioSystem.py
## This file is part of Snakeberry by Bruno Hautzenberger (http://the-engine.at)
## Dual-licensed under the MIT (http://www.opensource.org/licenses/mit-license.php)
## and the Beerware (http://en.wikipedia.org/wiki/Beerware) license.

from json import JSONEncoder

#Helper class to serialize objects to JSON
#Author: Bruno Hautzenberger
class SnakeberryJSON(JSONEncoder):
    def default(self, o):
        return o.__dict__  