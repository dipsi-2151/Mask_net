import mongoengine
import pickle
from mongoengine import signals
import globals


class UserModel(mongoengine.Document):
    @classmethod
    def post_save(cls, sender, document, **kwargs):
        globals.add_to_embeddings(username= document.user_name, encoding=pickle.loads(document.encoding))
    user_name = mongoengine.StringField(required=True)
    image = mongoengine.FileField(required=True)
    encoding = mongoengine.BinaryField(required=True)
    meta = {
        'db_alias': 'core',
        'collection': 'users'
    }   
signals.post_save.connect(UserModel.post_save, sender=UserModel)