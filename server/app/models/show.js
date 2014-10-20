var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ShowSchema   = new Schema({
	name: String,
	date: Date,
	ticketPrice: Number
});

module.exports = mongoose.model('Show', ShowSchema);
