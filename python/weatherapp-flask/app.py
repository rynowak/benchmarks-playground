from flask import Flask, jsonify, make_response
import requests

app = Flask(__name__)

@app.route("/")
def weather():
    forecast = requests.get("http://localhost:5000/forecast").text
    return jsonify(location="Seattle", forecast=forecast)

@app.route("/forecast")
def forecast():
    response = make_response("Cloudy", 200)
    response.mimetype = "text/plain"
    return response

if __name__ == '__main__':
    app.run(host='0.0.0.0', port= 5000)
