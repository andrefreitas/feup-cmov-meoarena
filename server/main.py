from bottle import route, run, request, response
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
    credit_card = {
        "ccType": request.params.get("ccType"),
        "ccNumber": request.params.get("ccNumber"),
        "ccValidity": request.params.get("ccValidity")
    }
    response.content_type = 'application/json'
    answer = data.create_customer(name, email, password, nif, credit_card)
    if answer:
        return answer
    else:
        response.status = 400


@route('/api/shows', method="GET")
def shows_all():
    response.content_type = 'application/json'
    return data.get_shows()

@route('/api/login', method="POST")
def login():
    email = request.params.get("email")
    password = request.params.get("password")
    response.content_type = 'application/json'
    result = data.login(email, password)
    if result:
        return {"id": str(result["_id"])}
    else:
        response.status = 400

@route('/api/products', method="GET")
def products_all():
    response.content_type = 'application/json'
    return data.get_products()

@route('/api/tickets', method="POST")
def tickets_create():
    customer_id = request.params.get("customerID")
    show_id = request.params.get("showID")
    pin = request.params.get("pin")
    quantity = request.params.get("quantity")
    response.content_type = 'application/json'
    answer = data.buy_ticket(customer_id, show_id, pin, quantity)
    if answer:
        return answer
    else:
        response.status = 400

@route('/api/tickets', method="GET")
def tickets_all():
    customer_id = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.get_tickets(customer_id)

@route('/api/vouchers', method="GET")
def tickets_all():
    customer_id = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.get_vouchers(customer_id)

@route('/api/transactions', method="GET")
def tickets_all():
    customer_id = request.params.get("customerID")
    response.content_type = 'application/json'
    return data.get_transactions(customer_id)


run(host='localhost', port=8080, reloader=True, server='cherrypy')