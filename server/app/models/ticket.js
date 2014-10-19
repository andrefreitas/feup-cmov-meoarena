// app/models/ticket.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TicketSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	status: String
});

module.exports = mongoose.model('Ticket', TicketSchema);
