// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var VoucherSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	name: String,
	discountValue: Number,
	discountType: String
});

module.exports = mongoose.model('Voucher', VoucherSchema);
