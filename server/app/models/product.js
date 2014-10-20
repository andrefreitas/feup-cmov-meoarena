// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ProductSchema   = new Schema({
	name: String,
	price: Number
});

module.exports = mongoose.model('Product', ProductSchema);
