// app/models/show.js

var mongoose     = require('mongoose');
var Schema       = mongoose.Schema;

var OrderedproductsSchema   = new Schema({
	//id: automaticamente atribuído pelo mongodb?!
	price: Number
});

module.exports = mongoose.model('Orderedproducts', OrderedproductsSchema);
