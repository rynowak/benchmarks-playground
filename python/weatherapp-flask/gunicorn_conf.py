import multiprocessing

workers = multiprocessing.cpu_count() * 3
bind = "0.0.0.0:5000"
keepalive = 120
errorlog = '-'
pidfile = 'gunicorn.pid'