// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var TransactionSchema   = new Schema({
	description: String,
	date: Date,
	amount: Number
});

module.exports = mongoose.model('Transaction', TransactionSchema);
