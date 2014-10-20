var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ShowSchema   = new Schema({
	name: String,
	date: Date,
	ticketPrice: Double
});

module.exports = mongoose.model('Show', ShowSchema);
