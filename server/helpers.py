import hashlib
import random
import datetime


def encrypt_password(password):
    return hashlib.md5(password.encode()).hexdigest()


def generate_pin():
    return random.randint(1000, 9999)


def parse_date(date_str, date_format):
    return datetime.datetime.strptime(date_str, date_format)


def format_date(date_instance):
    return "{:%d/%m/%Y}".format(date_instance)