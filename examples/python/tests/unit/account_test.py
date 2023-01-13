from twilio.rest import Client

accountSid = "AC12345678123456781234567812345678"
authToken = "CR12345678123456781234567812345678"
twilio = Client(accountSid, authToken)

def func(x):
    return x + 1

# Placeholder test
def test_answer():
    assert func(3) == 4
