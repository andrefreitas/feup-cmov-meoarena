from pymongo import MongoClient
from bson.objectid import ObjectId
from helpers import *
import datetime
from bson.json_util import dumps

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
         "validity" : validity
        }
  db.customers.update({"_id": ObjectId(customerID)}, {"$set" : {"creditCard" : doc}})

def getCustomer(id):
  return db.customers.find_one({"_id" : ObjectId(id)})

def deleteCustomer(id):
  return db.customers.remove({"_id" : ObjectId(id)})



def createShow(name, dateString, price, seats):
    # dateString in format (DD/MM/YYYY)
    dateInstance = datetime.datetime.strptime(dateString,"%d/%m/%Y")
    doc = { "name" : name,
            "date" : dateInstance,
            "price" : price,
            "seats" : seats}
    showID = db.shows.insert(doc)
    return {"id" : showID}

def getShows():
    return dumps(db.shows.find())



