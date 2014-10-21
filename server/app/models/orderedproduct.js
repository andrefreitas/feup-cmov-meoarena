// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var OrderedproductSchema   = new Schema({
	price: Number
});

module.exports = mongoose.model('Orderedproduct', OrderedproductSchema);
