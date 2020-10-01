import mongoengine
import datetime

class PenaltyModel(mongoengine.Document):
    user_name = mongoengine.StringField(required=True)
    image= mongoengine.BinaryField(required=True)
    date = mongoengine.DateTimeField(default=datetime.datetime.now)
    location = mongoengine.StringField(required=True)
    meta={
        'db_alias': 'core',
        'collection': 'penalty'
    }