import multiprocessing
import os

workers = multiprocessing.cpu_count()
if "CPUS" in os.environ:
  workers = (int)(float(os.environ["CPUS"]))

bind = "0.0.0.0:5000"
keepalive = 120
errorlog = '-'
pidfile = 'gunicorn.pid'