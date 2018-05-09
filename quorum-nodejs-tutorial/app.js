#!/usr/bin/env node

var Web3 = require('web3-quorum');
var web3 = new Web3();

web3.setProvider(new web3.providers.HttpProvider('http://localhost:22000'));

var coinbase = web3.eth.coinbase;
console.log(coinbase);

var balance = web3.eth.getBalance(coinbase);
console.log(balance.toString(10));

