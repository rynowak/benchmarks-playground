from flask import Flask, jsonify, make_response
import os
import requests

app = Flask(__name__)

uri = os.environ.get("FORECAST_SERVICE_URI", "http://localhost:8080")
uri = uri.rstrip("/")
uri = uri + "/forecast"

@app.route("/")
def weather():
    forecast = requests.get(uri).json()
    return jsonify(location="Seattle", forecast=forecast["weather"])

if __name__ == '__main__':
    app.run(host='0.0.0.0', port= 5000)
