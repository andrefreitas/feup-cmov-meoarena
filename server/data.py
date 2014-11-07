from pymongo import MongoClient
from bson.objectid import ObjectId
from helpers import *
import datetime
from bson.json_util import dumps
import math

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
        ticket = db.tickets.find({"showID": doc["_id"]}).sort("seat", -1).limit(1)
        if ticket.count() > 0:
            doc["available"] = doc["seats"] - int(ticket[0]["seat"])
        else:
            doc["available"] = doc["seats"]
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
            ticket["customerID"] = str(ticket["customerID"])
            ticket["date"] = format_date(ticket["date"])
            ticket["date_show"] = (format_date(ticket["date_show"]))
            del ticket["_id"]
            tickets.append(ticket)
        total = get_total_ammount(customer_id) % 100
        transaction_ammount = create_transaction(customer_id, float(show["price"]), int(quantity), show["name"])
        remain = math.floor((total + transaction_ammount) / 100)
        if (remain > 0):
            for i in range(0, remain, 1):
                create_discount_voucher(customer_id)
        return dumps(tickets)
    else:
        return False


def create_ticket(seat, show_id, customer_id):
    show = db.shows.find_one({"_id": ObjectId(show_id)})
    doc = {
        "seat": seat,
        "customerID": ObjectId(customer_id),
        "showID": ObjectId(show_id),
        "status": "unused",
        "date": datetime.datetime.today(),
        "name": show["name"],
        "date_show": show["date"]
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
        doc["date_show"] = (format_date(doc["date_show"]))
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

    voucher_id = db.vouchers.insert(voucher)
    return {"id": str(voucher_id)}

def create_discount_voucher(customer_id):
    voucher = {
        "product": "all",
        "status": "unused",
        "discount": 0.05,
        "customerID": ObjectId(customer_id)
    }

    voucher_id = db.vouchers.insert(voucher)
    return {"id": str(voucher_id)}

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


def create_cafeteria_transaction(customer_id, total):
    transaction = {
        "description": "Compra cafetaria",
        "amount": str(total),
        "date": datetime.datetime.today(),
        "customerID": ObjectId(customer_id)
    }
    db.transactions.insert(transaction)

def create_transaction(customer_id, ticket_price, quantity, name):
    transaction = {
        "description": str(quantity) + "  bilhetes para " + name,
        "amount": quantity * ticket_price,
        "date": datetime.datetime.today(),
        "customerID": ObjectId(customer_id)
    }
    db.transactions.insert(transaction)
    return quantity*ticket_price


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

def get_total_ammount(customer_id):
    cursor = db.transactions.find({"customerID": ObjectId(customer_id)})
    total = 0
    for doc in cursor:
        total += int(doc["amount"])

    return total

def create_cafeteria_order(customerID, pin, vouchers, products, quantity, price):
    customer = db.customers.find_one({"_id": ObjectId(customerID), "pin": int(pin)})
    vouchers_list = vouchers.split(",")
    products_list = products.split(",")
    quantity_list = quantity.split(",")
    total_price = int(price)
    if customer:
        # Build vouchers document
        vouchers_doc = []
        # Copy quantity list to reduce number iteratively
        dumb_quantity_list = list(map(int, quantity_list))
        percent_voucher = False
        for i in range(0, len(vouchers_list), 1):
            # Finds each voucher for the given ids
            voucher = db.vouchers.find_one({"_id": ObjectId(vouchers_list[i])})
            if voucher:
                var = valid_voucher_product(i, vouchers_list, products_list, dumb_quantity_list, percent_voucher)
                if var is True:
                    voucher["status"] = "used"
                    db.vouchers.update({"_id": voucher["_id"]},{'$set':{"status": "used"}})
                    vouchers_doc.append(voucher)
                    # Update price
                    p = db.products.find_one({"_id": ObjectId(products_list[i])})
                    total_price -= p["price"]*voucher["discount"]
                if var == "percent_voucher":
                    voucher["status"] = "used"
                    db.vouchers.update({"_id": voucher["_id"]},{'$set':{"status": "used"}})
                    percent_voucher = True
                    vouchers_doc.append(voucher)
                    total_price = total_price - total_price*voucher["discount"]

        # Build products document
        products_doc = []
        for i in range(0, len(products_list), 1):
            # Finds each product for the given ids
            product = db.products.find_one({"_id": ObjectId(products_list[i])})
            if product:
                product["quantity"] = quantity_list[i]
                products_doc.append(product)

        # Build orders document
        doc = {
            "customerID": ObjectId(customerID),
            "vouchers": vouchers_doc,
            "products": products_doc,
            "price": total_price
        }

        order_id = db.orders.insert(doc)
        order = db.orders.find_one({"_id": order_id})
        order["id"] = str(order["_id"])
        order["customerID"] = str(order["customerID"])
        del order["_id"]
        create_cafeteria_transaction(customerID, total_price)
        return dumps(order)
    else:
        return False


def valid_voucher_product(i, vouchers, products, quantity, percent_voucher):
    voucher_id = vouchers[i]
    voucher = db.vouchers.find_one({"_id": ObjectId(voucher_id)})

    if voucher:
        if voucher["product"] == "all" and percent_voucher is False:
            return "percent_voucher"
        else:
            for i in range(0, len(products), 1):
                product_id = products[i]
                product = db.products.find_one({"_id": ObjectId(product_id)})
                if (product):
                    if ((voucher["product"] == product["name"]) and quantity[i] > 0 and voucher["status"] == "unused"):
                        quantity[i] -= 1
                        return True
            return False


def get_orders(customer_id):
    cursor = db.orders.find({"customerID": ObjectId(customer_id)})
    results = []
    for doc in cursor:
        doc["id"] = str(doc["_id"])
        doc["customerID"] = str(doc["customerID"])
        del doc["_id"]
        results.append(doc)
    return dumps(results)