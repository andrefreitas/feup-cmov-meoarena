from pymongo import MongoClient
from bson.objectid import ObjectId
from helpers import *

db = MongoClient()['meoarena']

def createCustomer(name, email, password, nif, creditCard):
  pin = generatePIN()
  doc = {"name" : name,
         "email" : email,
         "password": encryptPassword(password),
         "nif" : nif,
         "pin" : pin
         }
  customerID = db.customers.insert(doc)
  addCreditCard(customerID, creditCard['ccType'], creditCard['ccNumber'], creditCard['ccValidity'])
  return { "id" : str(customerID), "pin" : pin}

def addCreditCard(customerID, type, number, validity):
  doc = {"type" : type,
         "number" : number,
         "validity" : validity }
  db.customers.update({"_id": ObjectId(customerID)}, {"$set" : {"creditCard" : doc}})
