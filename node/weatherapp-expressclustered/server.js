const cluster = require('cluster')
const os = require('os')
const express = require('express')

if (cluster.isMaster) {
    const cpuCount = os.cpus().length
    for (let i = 0; i < cpuCount; i++) {
        cluster.fork()
    }
}
else {
    const app = express()
    const port = 5000
    
    app.get('/', (req, res) => 
    {
        res.json({ location: "Seattle", weather: "Cloudy" });
    });
    
    app.listen(port, () => console.log(`app listening on port ${port}!`))
}

cluster.on('exit', (worker) => {
    console.log('mayday! mayday! worker', worker.id, ' is no more!')
    cluster.fork()
})
