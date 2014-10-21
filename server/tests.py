import unittest
import data
import requests
import helpers
from bson.objectid import ObjectId

class TestApi(unittest.TestCase):
  def setUp(self):
    self.customer1 = { "name" : "Carlos Andrade",
                       "email" : "carlos.andrade@gmail.com",
                       "password" : "j6r763f76d2567d2",
                       "nif" : "242445678",
                       "ccType" : "Visa",
                       "ccNumber" : "4024007149497504",
                       "ccValidity" : "02/17"
                      }

  def testCreateCustomer(self):
    payload = self.customer1
    r = requests.post("http://localhost:8080/api/customers", params = payload)
    answer = r.json()
    customer = data.getCustomer(answer["id"])
    self.assertEqual(customer["name"], payload["name"])
    self.assertEqual(customer["email"], payload["email"])
    self.assertEqual(customer["password"], helpers.encryptPassword(payload["password"]))
    self.assertEqual(customer["nif"], payload["nif"])
    self.assertEqual(customer["creditCard"]["type"], payload["ccType"])
    self.assertEqual(customer["creditCard"]["number"], payload["ccNumber"])
    self.assertEqual(customer["creditCard"]["validity"], payload["ccValidity"])
    self.assertEqual(customer["_id"], ObjectId(answer["id"]))

    r = requests.post("http://localhost:8080/api/customers", params = payload)
    self.assertEqual(r.status_code, 400)

    data.deleteCustomer(answer["id"])

  def testLogin(self):
    answer = requests.post("http://localhost:8080/api/customers", params = self.customer1).json()
    payload = { "email" : self.customer1["email"], "password" : self.customer1["password"] }
    loginAnswer = requests.post("http://localhost:8080/api/login", params = payload)
    self.assertEqual(loginAnswer.status_code, 200)
    loginAnswer = loginAnswer.json()
    self.assertEqual(answer['id'], loginAnswer["id"])
    payload = { "email" : self.customer1["email"], "password" : "wrongpassword" }
    loginAnswer = requests.post("http://localhost:8080/api/login", params = payload)
    self.assertEqual(loginAnswer.status_code, 400)
    data.deleteCustomer(answer["id"])

  def testListingShows(self):
    show1 = data.createShow("Tony Carreira", "31/10/2014", 22.50, 100)
    show2 = data.createShow("John Legend", "08/11/2014", 12, 300)
    show3 = data.createShow("Paulo Gonzo", "14/11/2014", 3, 70)
    answer = requests.get("http://localhost:8080/api/shows").json()
    results = list(filter(lambda show: show["name"] == "Tony Carreira" and show["price"] == 22.50 and show["seats"] == 100 and show["date"] == "31/10/2014", answer))
    self.assertTrue(len(results) > 0)
    results = list(filter(lambda show: show["name"] == "John Legend" and show["price"] == 12 and show["seats"] == 300 and show["date"] == "08/11/2014", answer))
    self.assertTrue(len(results) > 0)
    results = list(filter(lambda show: show["name"] == "Paulo Gonzo" and show["price"] == 3 and show["seats"] == 70 and show["date"] == "14/11/2014", answer))
    self.assertTrue(len(results) > 0)
    data.deleteShow(show1["id"])
    data.deleteShow(show2["id"])
    data.deleteShow(show3["id"])

if __name__ == '__main__':
  unittest.main()
