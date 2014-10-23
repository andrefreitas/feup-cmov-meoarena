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
    return {"id" : str(showID)}

def getShows():
    cursor = db.shows.find()
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["date"] = formatDate(doc["date"])
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

def createProduct(description, name, price):
    doc = { "description" : description,
            "name" : name,
            "price" : price}
    productID = db.products.insert(doc)
    return {"id" : str(productID)}

def getProducts():
    return dumps(db.products.find())

def deleteProduct(id):
    return db.products.remove({"_id" : ObjectId(id)})


def buyTicket(customerID, showID, pin, quantity):
    customer = db.customers.find_one({"_id": ObjectId(customerID), "pin": int(pin)})
    show = db.shows.find_one({"_id": ObjectId(showID)})

    if (customer and show):
        ticket = db.tickets.find({"showID": ObjectId(showID)}).sort("seat", -1).limit(1)
        if (ticket.count() > 0):
            if (int(ticket[0]["seat"]) + int(quantity) > int(show["seats"])):
                return False
            else:
                seat = int(ticket[0]["seat"]) + 1
        else:
            seat = 1

        tickets = []
        for i in range (seat, seat + int(quantity), 1):
            ticketID = createTicket(i, showID, customerID)
            createFreeVoucher(customerID)
            ticket = db.tickets.find_one({"_id": ObjectId(ticketID)})
            ticket["id"] =  str(ticket["_id"])
            ticket["showID"] = str(ticket["showID"])
            ticket["date"] = formatDate(ticket["date"])
            del ticket["_id"]
            tickets.append(ticket)
        createTransaction(customerID, float(show["price"]), int(quantity), show["name"])
        return dumps(tickets)
    else:
        return "no customer or show"


def createTicket(seat, showID, customerID):
    doc = { "seat" : seat,
            "customerID": ObjectId(customerID),
            "showID" : ObjectId(showID),
            "status" : "unused",
            "date" : datetime.datetime.today()
            }
    ticketID = db.tickets.insert(doc)
    return ticketID

def getTickets(customerID):
    cursor = db.tickets.find({"customerID": ObjectId(customerID)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        doc["showID"] = str(doc["showID"])
        doc["date"] = formatDate(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)

def deleteTickets(showID, customerID):
    return db.tickets.remove({"showID": ObjectId(showID), "customerID": ObjectId(customerID)})

def createFreeVoucher(customerID):
    number = random.randint(0, 1)
    if (number == 1):
        product = "coffee"
    else:
        product = "popcorn"

    voucher = {
        "product": product,
        "status": "unused",
        "discount": 1,
        "customerID": ObjectId(customerID)
    }

    db.vouchers.insert(voucher)

def getVouchers(customerID):
    cursor = db.vouchers.find({"customerID": ObjectId(customerID)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)

def deleteVouchers(customerID):
    return db.vouchers.remove({"customerID": ObjectId(customerID)})

def createTransaction(customerID, ticketPrice, quantity, name):
    transaction = {
        "description": str(quantity) + "  bilhetes para " + name,
        "amount": quantity * ticketPrice,
        "date": datetime.datetime.today(),
        "customerID": ObjectId(customerID)
    }

    db.transactions.insert(transaction)

def getTransactions(customerID):
    cursor = db.transactions.find({"customerID": ObjectId(customerID)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        doc["date"] = formatDate(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)

def deleteTransactions(customerID):
    return db.transactions.remove({"customerID": ObjectId(customerID)})