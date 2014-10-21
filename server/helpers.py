import hashlib
import random
import datetime

def encryptPassword(password):
  return hashlib.md5(password.encode()).hexdigest()

def generatePIN():
  return random.randint(1000, 9999)

def parseDate(dateString, format):
    return datetime.datetime.strptime(dateString, format)
