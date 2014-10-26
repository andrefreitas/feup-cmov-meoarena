from bottle import route, run, template, request, response
import data

@route('/', method="GET")
def home():
    return "Dev MeoArena"

@route('/api/customers', method="POST")
def customers_create():
    name = request.params.get("name")
    email = request.params.get("email")
    password = request.params.get("password")
    nif = request.params.get("nif")
    creditCard = { "ccType" : request.params.get("ccType"),
                   "ccNumber" : request.params.get("ccNumber"),
                   "ccValidity" : request.params.get("ccValidity")}
    response.content_type = 'application/json'
    answer = data.createCustomer(name, email, password, nif, creditCard)
    if (answer):
        return answer
    else:
        response.status = 400


@route('/api/shows', method="GET")
def shows_all():
    response.content_type = 'application/json'
    return data.getShows()

@route('/api/login', method="POST")
def login():
    email = request.params.get("email")
    password = request.params.get("password")
    response.content_type = 'application/json'
    result = data.login(email,password)
    if(result):
        return {"id" : str(result["_id"])}
    else:
        response.status = 400

@route('/api/products', method="GET")
def products_all():
    response.content_type = 'application/json'
    return data.getProducts()

@route('/api/tickets', method="POST")
def tickets_create():
    customerID = request.params.get("customerID")
    showID = request.params.get("showID")
    pin = request.params.get("pin")
    quantity = request.params.get("quantity")
    response.content_type = 'application/json'
    answer = data.buyTicket(customerID, showID, pin, quantity)
    if(answer):
        return answer
    else:
        response.status = 400

@route('/api/tickets', method="GET")
def tickets_all():
    customerID = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.getTickets(customerID)

@route('/api/vouchers', method="GET")
def tickets_all():
    customerID = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.getVouchers(customerID)

@route('/api/transactions', method="GET")
def tickets_all():
    customerID = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.getTransactions(customerID)


run(host='localhost', port=8080, reloader=True, server='cherrypy')
