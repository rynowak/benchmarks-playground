import multiprocessing
import os

bind = "0.0.0.0:5000"
keepalive = 120
workers = 3
errorlog = '-'
pidfile = 'gunicorn.pid'
worker_class = 'aiohttp.GunicornWebWorker'