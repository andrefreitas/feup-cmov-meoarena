var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var CustomerSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	name: String,
	nif: { type: Number, min: 100000000, max: 999999999 },
	email: String,
	password: String,
	pin: { type: Number, min: 1000, max: 9999 }
});

module.exports = mongoose.model('Customer', CustomerSchema);
