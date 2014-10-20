// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ShowSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	name: String,
	date: Date,
	ticketPrice: Number
});

module.exports = mongoose.model('Show', ShowSchema);
