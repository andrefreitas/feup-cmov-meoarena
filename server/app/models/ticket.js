var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TicketSchema   = new Schema({
	status: String
});

module.exports = mongoose.model('Ticket', TicketSchema);
