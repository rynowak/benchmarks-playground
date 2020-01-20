from flask import Flask, jsonify, make_response
import requests

app = Flask(__name__)

@app.route("/")
def weather():
    forecast = requests.get("http://localhost:5002/forecast").json()
    return jsonify(location="Seattle", forecast=forecast["weather"])

if __name__ == '__main__':
    app.run(host='0.0.0.0', port= 5000)
