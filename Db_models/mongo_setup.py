import mongoengine

def global_init():
    mongoengine.register_connection(
        host="mongodb",
        port=27017,
        db="mask_net",
        alias='core'
    )
    mongoengine.connect(
       host="mongodb",
        port=27017,
        db="mask_net"
    )

