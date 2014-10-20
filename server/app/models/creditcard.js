// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var CreditcardSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	type: String,
	number: Number,
	validity: Date
});

module.exports = mongoose.model('Creditcard', CreditcardSchema);
