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
    data.deleteCustomer(answer["id"])


  def testListingShows(self):
    data.createShow("Tony Carreira", "31/10/2014", 22.50, 100)
    data.createShow("John Legend", "08/11/2014", 12, 300)
    data.createShow("PAULO GONZO - DUETOS AO VIVO", "14/11/2014", 3.00, 70)
    answer = requests.get("http://localhost:8080/api/shows").json()
    results = filter(lambda show: show["name"] == "Tony Carreira" and show["price"] == 22.50 and show["seats"] == 100 and show["date"] == "31/10/2014", answer)
    self.assertTrue(len(results) > 0)

"""

  def testLogin(self):
    answer = requests.post("http://localhost:8080/api/customers", params = self.customer1).json()
    payload = { "email" : self.customer1["email"], "password" : self.customer1["password"] }
    loginAnswer = requests.get("http://localhost:8080/api/login", params = payload).json()
    self.assertEqual(loginAnswer.status_code, 200)
    self.assertEqual(answer['id'], loginAnswer["id"])
    payload = { "email" : self.customer1["email"], "password" : "wrongpassword" }
    loginAnswer = requests.get("http://localhost:8080/api/login", params = payload).json()
    self.assertEqual(loginAnswer.status_code, 400)
    data.deleteCustomer(answer["id"])
"""




if __name__ == '__main__':
  unittest.main()
