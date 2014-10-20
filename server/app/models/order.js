// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var OrderSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	date: Date,
	total: Number
});

module.exports = mongoose.model('Order', OrderSchema);
