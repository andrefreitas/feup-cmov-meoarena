// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var ProductSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	name: String,
	price: Number
});

module.exports = mongoose.model('Product', ProductSchema);
