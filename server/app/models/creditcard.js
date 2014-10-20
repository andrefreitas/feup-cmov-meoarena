var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var CreditcardSchema   = new Schema({
	type: String,
	number: Number,
	validity: Date
});

module.exports = mongoose.model('Creditcard', CreditcardSchema);
