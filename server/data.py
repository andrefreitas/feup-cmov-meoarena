from pymongo import MongoClient
from bson.objectid import ObjectId
from helpers import *
import datetime
from bson.json_util import dumps

db = MongoClient()['meoarena']

def createCustomer(name, email, password, nif, creditCard):
  if (db.customers.find_one({"email": email})):
    return False
  else:
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
    dateInstance = parseDate(dateString,"%d/%m/%Y")
    doc = { "name" : name,
            "date" : dateInstance,
            "price" : price,
            "seats" : seats}
    showID = db.shows.insert(doc)
    return {"id" : showID}

def getShows():
    cursor = db.shows.find()
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["date"] = "{:%d/%m/%Y}".format(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)


def deleteShow(id):
    return db.shows.remove({"_id" : ObjectId(id)})

def login(email, password):
    customer = db.customers.find_one({"email": email, "password": encryptPassword(password)})
    if (customer):
        return customer
    else:
        return False

def createProduct(name, price):
    doc = { "name" : name,
            "price" : price}
    productID = db.products.insert(doc)
    return {"id" : productID}

def getProducts():
    return dumps(db.products.find())

def deleteProduct(id):
    return db.products.remove({"_id" : ObjectId(id)})