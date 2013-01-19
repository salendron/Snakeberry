from json import JSONEncoder

class SnakeberryJSON(JSONEncoder):
    def default(self, o):
        return o.__dict__  