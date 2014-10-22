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

def testListingProdutcs(self):
    



if __name__ == '__main__':
  unittest.main()
