// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ShowSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	name: String,
	date: Date,
	ticketPrice: Double
});

module.exports = mongoose.model('Show', ShowSchema);
