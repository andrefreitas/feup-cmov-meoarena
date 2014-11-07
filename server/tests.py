import unittest
import requests
import helpers
import datetime
from bson.objectid import ObjectId
import data


class TestApi(unittest.TestCase):
    def setUp(self):
        data.drop_data_base()
        self.customer1 = {
            "name": "Carlos Andrade",
            "email": "carlos.andrade@gmail.com",
            "password": "j6r763f76d2567d2",
            "nif": "242445678",
            "ccType": "Visa",
            "ccNumber": "4024007149497504",
            "ccValidity": "02/17"
        }

    def test_create_customer(self):
        payload = self.customer1
        r = requests.post("http://localhost:8080/api/customers", params=payload)
        answer = r.json()
        customer = data.get_customer(answer["id"])
        self.assertEqual(customer["name"], payload["name"])
        self.assertEqual(customer["email"], payload["email"])
        self.assertEqual(customer["password"], helpers.encrypt_password(payload["password"]))
        self.assertEqual(customer["nif"], payload["nif"])
        self.assertEqual(customer["creditCard"]["type"], payload["ccType"])
        self.assertEqual(customer["creditCard"]["number"], payload["ccNumber"])
        self.assertEqual(customer["creditCard"]["validity"], payload["ccValidity"])
        self.assertEqual(customer["_id"], ObjectId(answer["id"]))
        self.assertEqual(customer["pin"], answer["pin"])

        r = requests.post("http://localhost:8080/api/customers", params=payload)
        self.assertEqual(r.status_code, 400)
        data.delete_customer(answer["id"])

    def test_login(self):
        answer = requests.post("http://localhost:8080/api/customers", params=self.customer1).json()
        payload = {"email": self.customer1["email"], "password": self.customer1["password"]}
        login_answer = requests.post("http://localhost:8080/api/login", params=payload)
        self.assertEqual(login_answer.status_code, 200)
        login_answer = login_answer.json()
        self.assertEqual(answer['id'], login_answer["id"])
        payload = {"email": self.customer1["email"], "password": "wrongpassword"}
        login_answer = requests.post("http://localhost:8080/api/login", params=payload)
        self.assertEqual(login_answer.status_code, 400)
        data.delete_customer(answer["id"])

    def test_listing_shows(self):
        show1 = data.create_show("Tony Carreira", "31/10/2014", 22.50, 100)
        show2 = data.create_show("John Legend", "08/11/2014", 12, 300)
        show3 = data.create_show("Paulo Gonzo", "14/11/2014", 3, 70)
        answer = requests.get("http://localhost:8080/api/shows").json()
        results = list(filter(lambda show: show["name"] == "Tony Carreira" and show["price"] == 22.50
                                           and show["seats"] == 100 and show["date"] == "31/10/2014"
                                            and show["available"] == 100, answer))
        self.assertTrue(len(results) == 1)
        results = list(filter(lambda show: show["name"] == "John Legend" and show["price"] == 12
                                           and show["seats"] == 300 and show["date"] == "08/11/2014"
                                            and show["available"] == 300, answer))
        self.assertTrue(len(results) == 1)
        results = list(filter(lambda show: show["name"] == "Paulo Gonzo" and show["price"] == 3
                                           and show["seats"] == 70 and show["date"] == "14/11/2014"
                                           and show["available"] == 70, answer))
        self.assertTrue(len(results) == 1)
        data.delete_show(show1["id"])
        data.delete_show(show2["id"])
        data.delete_show(show3["id"])

    def test_buy_tickets(self):
        customer = requests.post("http://localhost:8080/api/customers", params=self.customer1).json()
        show1 = data.create_show("Tony Carreira", "31/10/2014", 22.50, 100)
        product1 = data.create_product("Cafe Expresso", "coffee", 2.3)
        product2 = data.create_product("Pipocas", "popcorn", 5.3)
        payload = {
            "customerID" : customer["id"],
            "showID" : show1["id"],
            "pin" : int(customer["pin"]),
            "quantity" : 3
        }
        today_date = helpers.format_date(datetime.date.today())

        # Buy tickets valid
        answer = requests.post("http://localhost:8080/api/tickets", params=payload)
        self.assertEqual(answer.status_code, 200)

        #Buy tickets invalid
        payload["quantity"] = 99
        answer = requests.post("http://localhost:8080/api/tickets", params=payload)
        self.assertEqual(answer.status_code, 400)

        # Check if available tickets are updated
        answer = requests.get("http://localhost:8080/api/shows").json()
        results = list(filter(lambda show: show["name"] == "Tony Carreira" and show["price"] == 22.50
                                           and show["seats"] == 100 and show["date"] == "31/10/2014"
                                            and show["available"] == 97, answer))
        self.assertTrue(len(results) == 1)

        # Get purchased tickets
        tickets = requests.get("http://localhost:8080/api/tickets", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda ticket: "id" in ticket and "seat" in ticket
                                             and ticket["status"] == "unused" and ticket["date"] == today_date
                                             and ticket["showID"] == show1["id"]
                                             and ticket["name"] == "Tony Carreira"
                                             and ticket["date_show"] == "31/10/2014", tickets))
        self.assertTrue(len(results) == 3)

        # Get vouchers
        vouchers = requests.get("http://localhost:8080/api/vouchers", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda voucher: "id" in voucher and (voucher["product"] == "coffee" or voucher["product"] == "popcorn") and voucher["discount"] == 1 and voucher["status"] == "unused", vouchers))
        self.assertTrue(len(results) == 3)

        # Check that the transactions were recorded
        get_params = {"customerID": customer["id"]}
        transactions = requests.get("http://localhost:8080/api/transactions", params=get_params).json()
        results = list(filter(lambda transaction: transaction["date"] == today_date and transaction["amount"] == 67.5
                                                  and "3" in transaction["description"]
                                                  and "Tony Carreira" in transaction["description"], transactions))
        self.assertTrue(len(results) == 1)

        #Buy tickets for free discount vouchers
        payload["quantity"] = 5
        answer = requests.post("http://localhost:8080/api/tickets", params=payload)
        self.assertEqual(answer.status_code, 200)
        vouchers = requests.get("http://localhost:8080/api/vouchers", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda voucher: "id" in voucher and (voucher["product"] == "all" or voucher["product"] == "popcorn" or voucher["product"] == "coffee")
                                              and (voucher["discount"] == 1 or voucher["discount"] == 0.05) and voucher["status"] == "unused", vouchers))
        self.assertTrue(len(vouchers) == 9)

        data.delete_tickets(show1["id"], customer["id"])
        data.delete_customer(customer["id"])
        data.delete_show(show1["id"])
        data.delete_product(product1["id"])
        data.delete_product(product2["id"])
        data.delete_transactions(customer["id"])
        data.delete_vouchers(customer["id"])

    def test_listing_products(self):
        product1 = data.create_product("Cafe Expresso", "coffee", 0.50)
        product2 = data.create_product("Pipocas", "popcorn", 2.75)
        product3 = data.create_product("Sumo Laranja", "juice", 1)
        answer = requests.get("http://localhost:8080/api/products").json()
        results = list(filter(lambda product: product["description"] == "Cafe Expresso" and product["name"] == "coffee"
                                              and product["price"] == 0.50, answer))
        self.assertTrue(len(results) == 1)
        results = list(filter(lambda product: product["description"] == "Pipocas" and product["name"] == "popcorn"
                                              and product["price"] == 2.75, answer))
        self.assertTrue(len(results) == 1)
        results = list(filter(lambda product: product["description"] == "Sumo Laranja" and product["name"] == "juice"
                                              and product["price"] == 1, answer))
        self.assertTrue(len(results) == 1)
        data.delete_product(product1["id"])
        data.delete_product(product2["id"])
        data.delete_product(product3["id"])

    def test_validate_tickets(self):
        customer = requests.post("http://localhost:8080/api/customers", params=self.customer1).json()
        show1 = data.create_show("Tony Carreira", "31/10/2014", 22.50, 100)
        product1 = data.create_product("Cafe Expresso", "coffee", 2.3)
        product2 = data.create_product("Pipocas", "popcorn", 5.3)
        payload = {
            "customerID": customer["id"],
            "showID": show1["id"],
            "pin": int(customer["pin"]),
            "quantity": 3
        }
        helpers.format_date(datetime.date.today())
        requests.post("http://localhost:8080/api/tickets", params=payload)

        # START

        # Get purchased tickets
        tickets = requests.get("http://localhost:8080/api/tickets", params={"customerID": customer["id"]}).json()

        tickets_ids = []
        for tick in tickets[:2]:
            tickets_ids.append(tick["id"])
        tickets_string = ",".join(tickets_ids)

        # Test that the tickets are valid
        validation_params = {"customerID": customer["id"], "tickets": tickets_string}
        answer = requests.get("http://localhost:8080/api/validateTickets", params=validation_params)
        self.assertEqual(answer.status_code, 200)

        # Test that the status changed to used
        tickets = requests.get("http://localhost:8080/api/tickets", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda ticket: ticket["id"] in tickets_ids and ticket["status"] == "used", tickets))
        self.assertTrue(len(results) == 2)

        # Test that used tickets can't be validated again
        answer = requests.get("http://localhost:8080/api/validateTickets", params=validation_params)
        self.assertEqual(answer.status_code, 400)


        # END

        data.delete_tickets(show1["id"], customer["id"])
        data.delete_customer(customer["id"])
        data.delete_show(show1["id"])
        data.delete_product(product1["id"])
        data.delete_product(product2["id"])
        data.delete_transactions(customer["id"])
        data.delete_vouchers(customer["id"])

    def test_validate_order(self):
        customer = requests.post("http://localhost:8080/api/customers", params=self.customer1).json()
        product1 = data.create_product("Cafe Expresso", "coffee", 2.3)
        product2 = data.create_product("Pipocas", "popcorn", 5.3)
        voucher1 = data.create_free_voucher(customer["id"])
        voucher2 = data.create_discount_voucher(customer["id"])

        payload = {
            "customerID": customer["id"],
            "pin": int(customer["pin"]),
            "vouchers": voucher1["id"]+","+voucher2["id"],
            "products": product1["id"]+","+product2["id"],
            "quantity": "3,2",
            "price": 23
        }

        answer = requests.post("http://localhost:8080/api/orders", params=payload)
        self.assertEqual(answer.status_code, 200)

        orders = requests.get("http://localhost:8080/api/orders", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda order: order["customerID"] == customer["id"] and len(order["vouchers"]) == 2
                                              and len(order["products"]) == 2, orders))
        self.assertTrue(len(results) == 1)

        transactions = requests.get("http://localhost:8080/api/transactions", params={"customerID": customer["id"]}).json()
        results = list(filter(lambda transaction: transaction["customerID"] == customer["id"]
                                              and transaction["description"] == "Compra cafetaria", transactions))
        self.assertTrue(len(results) == 1)

        data.drop_data_base()


if __name__ == '__main__':
    unittest.main()