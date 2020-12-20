
embeddings = []


def add_to_embeddings(username, encoding):
    interm = {
        "name": username,
        "encoding": encoding
    }
    embeddings.append(interm)