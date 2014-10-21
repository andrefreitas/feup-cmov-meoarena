from bottle import route, run, template, request
import data

@route('/api/customers', method="POST")
def customers_create():
    name = request.params.get("name")
    email = request.params.get("email")
    password = request.params.get("password")
    nif = request.params.get("nif")
    creditCard = { "ccType" : request.params.get("ccType"),
                   "ccNumber" : request.params.get("ccNumber"),
                   "ccValidity" : request.params.get("ccValidity")}
    return data.createCustomer(name, email, password, nif, creditCard)

@route('/api/login', method="POST")
def login():
    email = request.params.get("email")
    password = request.params.get("password")
    return "Hello World!"
    #return data.login(email,password)

run(host='localhost', port=8080, reloader=True)
