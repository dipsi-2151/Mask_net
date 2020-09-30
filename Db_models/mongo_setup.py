import mongoengine

def global_init():
    mongoengine.register_connection(
        db="mask_net",
        alias='core'
    )
    mongoengine.connect(
        db="mask_net"
    )

