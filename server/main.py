from bottle import route, run, template, request, response
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


run(host='localhost', port=8080, reloader=True)
