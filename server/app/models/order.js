var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var OrderSchema   = new Schema({
	date: Date,
	total: Number
});

module.exports = mongoose.model('Order', OrderSchema);
