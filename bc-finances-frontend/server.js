const express = require('express');
const app = express();

app.use(express.static(__dirname + '/dist/bc-finances-frontend'));

app.get('/*', function (req, res){
  res.sendFile(__dirname + '/dist/bc-finances-frontend/index.html');
});

app.listen(process.env.PORT || 4200);

