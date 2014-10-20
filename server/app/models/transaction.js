// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TransactionSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	description: String,
	date: Date,
	amount: Number
});

module.exports = mongoose.model('Transaction', TransactionSchema);
