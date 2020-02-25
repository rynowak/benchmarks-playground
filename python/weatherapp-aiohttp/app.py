from aiohttp import web
import aiohttp
import asyncio 
import os

uri = os.environ.get("FORECAST_SERVICE_URI", "http://localhost:8080")
uri = uri.rstrip("/")
uri = uri + "/forecast"

session = aiohttp.ClientSession()

async def weather(request):
    async with session.get(uri) as response:
        forecast = await response.json()
    return web.json_response( { 'location': 'Seattle', 'forecast': forecast["weather"] })

async def run():
    app = web.Application()
    app.router.add_get('/', weather)
    return app

if __name__ == '__main__':
    web.run_app(run)
