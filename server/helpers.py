import hashlib
import random

def encryptPassword(password):
  return hashlib.md5(password.encode()).hexdigest()

def generatePIN():
  return random.randint(1000, 9999)
