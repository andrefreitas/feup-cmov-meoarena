// server.js

// BASE SETUP
// =============================================================================

// call the packages we need
var express    = require('express'); 		// call express
var app        = express(); 				// define our app using express
var bodyParser = require('body-parser');

var mongoose   = require('mongoose');
mongoose.connect('mongodb://localhost/meoarena');
var Customer     = require('./app/models/customer');

// configure app to use bodyParser()
// this will let us get the data from a POST
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var port = process.env.PORT || 8080; 		// set our port

// ROUTES FOR OUR API
// =============================================================================
var router = express.Router(); 				// get an instance of the express Router

// middleware to use for all requests
router.use(function(req, res, next) {
	// do logging
	console.log("Something is happening");
	next(); // make sure we go to the next routes and don't stop here
});

// test route to make sure everything is working (accessed at GET http://localhost:8080/api)
router.get('/', function(req, res) {
	res.json({ message: 'hooray! weleeeecome to our api!' });
});

// Customers route
router.route('/customers')

	// Create a customer
	.post(function(req, res){
		var customer = new Customer();
		customer.name = req.body.name;
		customer.nif = req.body.nif;
		customer.email = req.body.email;
		customer.password = req.body.password;
		customer.pin = Math.floor(Math.random() * 9000) + 1000;;

		customer.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: customer.id, pin: customer.pin });
		});
	})

	.get(function(req, res) {
		Customer.find(function(err, customers) {
			if (err)
				res.send(err);

			res.json(customers);
		});
	});

// Customer ID route
router.route('/customers/:customer_id')

	// get the customer with that id
	.get(function(req, res) {
		Customer.findById(req.params.customer_id, function(err, customer) {
			if (err)
				res.send(err);
			res.json(customer);
		});
	});


// Shows route
router.route('/shows')

	// Create a show
	.post(function(req, res){
		var show = new Show();
		show.name = req.body.name;
		show.date = req.body.date;
		show.ticketprice = req.body.ticketprice;

		show.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: show.id});
		});
	})

	.get(function(req, res) {
		Show.find(function(err, shows) {
			if (err)
				res.send(err);

			res.json(shows);
		});
	});

// Show ID route
router.route('/shows/:show_id')

	// get the show with that id
	.get(function(req, res) {
		Show.findById(req.params.show_id, function(err, show) {
			if (err)
				res.send(err);
			res.json(show);
		});
	});

// Products route
router.route('/products')

	// Create a product
	.post(function(req, res){
		var product = new Product();
		product.name = req.body.name;
		product.price = req.body.price;

		product.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: product.id});
		});
	})

	.get(function(req, res) {
		Product.find(function(err, products) {
			if (err)
				res.send(err);

			res.json(products);
		});
	});

// Product ID route
router.route('/products/:product_id')

	// get the product with that id
	.get(function(req, res) {
		Product.findById(req.params.product_id, function(err, product) {
			if (err)
				res.send(err);
			res.json(product);
		});
	});

// Creditcards route
router.route('/creditcards')

	// Create a creditcard
	.post(function(req, res){
		var creditcard = new Creditcard();
		creditcard.type = req.body.type;
		creditcard.number = req.body.number;
		creditcard.validity = req.body.validity;

		creditcard.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: creditcard.id});
		});
	})

	.get(function(req, res) {
		Creditcard.find(function(err, creditcards) {
			if (err)
				res.send(err);

			res.json(creditcards);
		});
	});

// Creditcard ID route
router.route('/creditcards/:creditcard_id')

	// get the creditcard with that id
	.get(function(req, res) {
		Creditcard.findById(req.params.creditcard_id, function(err, creditcard) {
			if (err)
				res.send(err);
			res.json(creditcard);
		});
	});

// Tickets route
router.route('/tickets')

	// Create a ticket
	.post(function(req, res){
		var ticket = new Ticket();
		ticket.status = req.body.status;
		ticket.dateCreated = req.body.dateCreated;
		ticket.seat = req.body.seat;

		ticket.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: ticket.id});
		});
	})

	.get(function(req, res) {
		Ticket.find(function(err, tickets) {
			if (err)
				res.send(err);

			res.json(tickets);
		});
	});

// Ticket ID route
router.route('/tickets/:ticket_id')

	// get the ticket with that id
	.get(function(req, res) {
		Ticket.findById(req.params.ticket_id, function(err, ticket) {
			if (err)
				res.send(err);
			res.json(ticket);
		});
	});

	.put(function(req, res) {
		// use our ticket model to find the ticket we want
		Ticket.findById(req.params.ticket_id, function(err, ticket) {

			if (err)
				res.send(err);

			ticket.status = req.body.status; 	// update the tickets info

			// save the ticket
			ticket.save(function(err) {
				if (err)
					res.send(err);

				res.json({ message: 'Ticket updated!' });
			});

		});
	});


// Orders route
router.route('/orders')

	// Create a order
	.post(function(req, res){
		var order = new Order();
		order.date = req.body.date;
		order.total = req.body.total;

		order.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: order.id});
		});
	})

	.get(function(req, res) {
		Orderedproduct.find(function(err, orders) {
			if (err)
				res.send(err);

			res.json(orders);
		});
	});

// order ID route
router.route('/orders/:order_id')

	// get the order with that id
	.get(function(req, res) {
		Order.findById(req.params.order_id, function(err, order) {
			if (err)
				res.send(err);
			res.json(order);
		});
	});


// Orderedproducts route
router.route('/orderedproducts')

	// Create a order
	.post(function(req, res){
		var orderedproduct = new Orderedproduct();
		orderedproduct.price = req.body.price;

		orderedproduct.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: orderedproduct.id});
		});
	})

	.get(function(req, res) {
		Orderedproduct.find(function(err, orderedproducts) {
			if (err)
				res.send(err);

			res.json(orderedproducts);
		});
	});

// Orderedproduct ID route
router.route('/orderedproducts/:orderedproduct_id')

	// get the orderedproduct with that id
	.get(function(req, res) {
		Orderedproduct.findById(req.params.orderedproduct_id, function(err, orderedproduct) {
			if (err)
				res.send(err);
			res.json(orderedproduct);
		});
	});

// Transactions route
router.route('/transactions')

	// Create a order
	.post(function(req, res){
		var transaction = new Transaction();
		transaction.price = req.body.price;

		transaction.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: transaction.id});
		});
	})

	.get(function(req, res) {
		Transaction.find(function(err, transactions) {
			if (err)
				res.send(err);

			res.json(transactions);
		});
	});

// transaction ID route
router.route('/transactions/:transaction_id')

	// get the transaction with that id
	.get(function(req, res) {
		Transaction.findById(req.params.transaction_id, function(err, transaction) {
			if (err)
				res.send(err);
			res.json(transaction);
		});
	});

// Vouchers route
router.route('/vouchers')

	// Create a order
	.post(function(req, res){
		var voucher = new Voucher();
		voucher.price = req.body.price;

		voucher.save(function(err){
			if(err)
				res.send(err);
				res.json({ id: voucher.id});
		});
	})

	.get(function(req, res) {
		Voucher.find(function(err, vouchers) {
			if (err)
				res.send(err);

			res.json(vouchers);
		});
	});

// voucher ID route
router.route('/vouchers/:voucher_id')

	// get the voucher with that id
	.get(function(req, res) {
		Voucher.findById(req.params.voucher_id, function(err, voucher) {
			if (err)
				res.send(err);
			res.json(voucher);
		});
	});
// more routes for our API will happen here

// REGISTER OUR ROUTES -------------------------------
// all of our routes will be prefixed with /api
app.use('/api', router);

// START THE SERVER
// =============================================================================
app.listen(port);
console.log('Magic happens on port ' + port);
