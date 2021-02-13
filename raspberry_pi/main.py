#!/usr/bin/env python
# -*- coding: utf-8 -*- 
import requests
import RPi.GPIO as GPIO
import time
import subprocess
import simpleaudio as sa


GPIO.setmode(GPIO.BOARD)

TRIG = 16
ECHO = 18
i = 0

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)
GPIO.setup(12, GPIO.OUT)
GPIO.setup(11, GPIO.OUT)
GPIO.output(TRIG, False)


time.sleep(2)

url = "http://52.188.166.61:7000/predict/"


try:
    print("System Started")
    while True:
        GPIO.output(TRIG, True)
        time.sleep(0.00001)
        GPIO.output(TRIG, False)

        while GPIO.input(ECHO) == 0:
            pulse_start = time.time()

        while GPIO.input(ECHO) == 1:
            pulse_end = time.time()

        pulse_duration = pulse_end - pulse_start

        distance = pulse_duration * 17150

        distance = round(distance+1.15, 2)

        if distance <= 45:
            print("in distance")
            subprocess.call(["fswebcam", "-r", "640×480", "image2.jpg"])
            files = [
                ('file', open("image2.jpg", 'rb'))
            ]
            print("pic captured")
            payload = {'location': 'hall'}
            response = requests.request("POST", url, data = payload, files = files)
            """ response can be "Mask", "suspicious", "No Mask" """
            print("response sent")
            result = str(response.text)
            result = result[1:-1]
            print(result)
            if result == "Mask":
                """Mased person"""
                GPIO.output(11, GPIO.HIGH)
                print("green light high")
                wave_obj = sa.WaveObject.from_wave_file('ok.wav')
                play_obj = wave_obj.play()
                play_obj.wait_done()
                GPIO.output(11, GPIO.LOW)
                print("green light low")
            elif result == "suspicious":
                """Suspicous person """
                GPIO.output(12, GPIO.HIGH)
                print("red light high")
                wave_obj = sa.WaveObject.from_wave_file('susp.wav')
                play_obj = wave_obj.play()
                play_obj.wait_done()
                GPIO.output(12, GPIO.LOW)
                print("red light low")
            else:
                """No masked Person"""
                GPIO.output(12, GPIO.HIGH)
                print("red light high")
                wave_obj = sa.WaveObject.from_wave_file('no_mask.wav')
                play_obj = wave_obj.play()
                play_obj.wait_done()
                GPIO.output(12, GPIO.LOW)
                print("red light low")

except KeyboardInterrupt:
    GPIO.cleanup()
