import requests

url = "http://127.0.0.1:8000/predict/"

payload = {'location': 'hall'}
files = [
  ('file', open(image,'rb'))
]


response = requests.request("POST", url, data = payload, files = files)

response = str(response.text.encode('utf8'))
print(response)

""" response can be "Mask", "suspicious", "No Mask" """
