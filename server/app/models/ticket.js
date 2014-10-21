var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TicketSchema   = new Schema({
	status: String,
	dateCreated: Date,
	seat: Number
});

module.exports = mongoose.model('Ticket', TicketSchema);
