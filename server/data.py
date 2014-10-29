from pymongo import MongoClient
from bson.objectid import ObjectId
from helpers import *
import datetime
from bson.json_util import dumps

db = MongoClient()['meoarena']


def drop_data_base():
    MongoClient().drop_database('meoarena')


def create_customer(name, email, password, nif, credit_card):
    if db.customers.find_one({"email": email}):
        return False
    else:
        pin = generate_pin()
        doc = {"name": name,
               "email": email,
               "password": encrypt_password(password),
               "nif": nif,
               "pin": pin
               }
        customer_id = db.customers.insert(doc)
        add_credit_card(customer_id, credit_card['ccType'], credit_card['ccNumber'], credit_card['ccValidity'])
        return {"id": str(customer_id), "pin": pin}


def add_credit_card(customer_id, card_type, number, validity):
    doc = {
        "type": card_type,
        "number": number,
        "validity": validity
    }
    db.customers.update({"_id": ObjectId(customer_id)}, {"$set": {"creditCard": doc}})


def get_customer(customer_id):
    return db.customers.find_one({"_id": ObjectId(customer_id)})


def delete_customer(customer_id):
    return db.customers.remove({"_id": ObjectId(customer_id)})


def create_show(name, date_string, price, seats):
    # dateString in format (DD/MM/YYYY)
    date_instance = parse_date(date_string, "%d/%m/%Y")
    doc = {
        "name": name,
        "date": date_instance,
        "price": price,
        "seats": seats
    }
    show_id = db.shows.insert(doc)
    return {"id": str(show_id)}


def get_shows():
    cursor = db.shows.find()
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["date"] = format_date(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)


def delete_show(show_id):
    return db.shows.remove({"_id": ObjectId(show_id)})


def login(email, password):
    customer = db.customers.find_one({"email": email, "password": encrypt_password(password)})
    if customer:
        return customer
    else:
        return False


def create_product(description, name, price):
    doc = {
        "description": description,
        "name": name,
        "price": price
    }
    product_id = db.products.insert(doc)
    return {"id": str(product_id)}


def get_products():
    return dumps(db.products.find())


def delete_product(id):
    return db.products.remove({"_id": ObjectId(id)})


def buy_ticket(customer_id, show_id, pin, quantity):
    customer = db.customers.find_one({"_id": ObjectId(customer_id), "pin": int(pin)})
    show = db.shows.find_one({"_id": ObjectId(show_id)})

    if customer and show:
        ticket = db.tickets.find({"showID": ObjectId(show_id)}).sort("seat", -1).limit(1)
        if ticket.count() > 0:
            if int(ticket[0]["seat"]) + int(quantity) > int(show["seats"]):
                return False
            else:
                seat = int(ticket[0]["seat"]) + 1
        else:
            seat = 1

        tickets = []
        for i in range(seat, seat + int(quantity), 1):
            ticket_id = create_ticket(i, show_id, customer_id)
            create_free_voucher(customer_id)
            ticket = db.tickets.find_one({"_id": ObjectId(ticket_id)})
            ticket["id"] = str(ticket["_id"])
            ticket["showID"] = str(ticket["showID"])
            ticket["date"] = format_date(ticket["date"])
            del ticket["_id"]
            tickets.append(ticket)
        create_transaction(customer_id, float(show["price"]), int(quantity), show["name"])
        return dumps(tickets)
    else:
        return "no customer or show"


def create_ticket(seat, show_id, customer_id):
    doc = {
        "seat": seat,
        "customerID": ObjectId(customer_id),
        "showID": ObjectId(show_id),
        "status": "unused",
        "date": datetime.datetime.today()
    }
    ticket_id = db.tickets.insert(doc)
    return ticket_id


def get_tickets(customer_id):
    cursor = db.tickets.find({"customerID": ObjectId(customer_id)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        doc["showID"] = str(doc["showID"])
        doc["date"] = format_date(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)


def delete_tickets(show_id, customer_id):
    return db.tickets.remove({"showID": ObjectId(show_id), "customerID": ObjectId(customer_id)})


def create_free_voucher(customer_id):
    number = random.randint(0, 1)
    if number == 1:
        product = "coffee"
    else:
        product = "popcorn"

    voucher = {
        "product": product,
        "status": "unused",
        "discount": 1,
        "customerID": ObjectId(customer_id)
    }

    db.vouchers.insert(voucher)


def get_vouchers(customer_id):
    cursor = db.vouchers.find({"customerID": ObjectId(customer_id)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)


def delete_vouchers(customer_id):
    return db.vouchers.remove({"customerID": ObjectId(customer_id)})


def create_transaction(customer_id, ticket_price, quantity, name):
    transaction = {
        "description": str(quantity) + "  bilhetes para " + name,
        "amount": quantity * ticket_price,
        "date": datetime.datetime.today(),
        "customerID": ObjectId(customer_id)
    }
    db.transactions.insert(transaction)


def get_transactions(customer_id):
    cursor = db.transactions.find({"customerID": ObjectId(customer_id)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        doc["date"] = format_date(doc["date"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)


def delete_transactions(customer_id):
    return db.transactions.remove({"customerID": ObjectId(customer_id)})