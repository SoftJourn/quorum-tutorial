# Quorum Java Tutorial

### Prerequisites

  - [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
  - 2-node Quorum network - could be created with [Quorum Maker](https://github.com/synechron-finlabs/quorum-maker)
  - [Solidity](https://github.com/ethereum/solidity) compiler & [web3j](https://github.com/web3j/web3j) installed

### Create Quorum network

  - Follow the [Creating the node configuration for Development use](https://github.com/synechron-finlabs/quorum-maker#user-content-creating-the-node-configuration-for-development-use)
  - [Running the nodes for Development](https://github.com/synechron-finlabs/quorum-maker#user-content-running-the-nodes-for-development)

##### Step by step

Clone maker and run setup:
```sh
git clone https://github.com/synechron-finlabs/quorum-maker.git
cd quorum maker
./setup.sh
```

Let's create two node network:
```sh
Would you like to use this with docker-compose support? [y/N] y
Please enter a project name: testnet0
Please enter the start port number [Default:22000]:
Please enter node name: node1
Generating public and private keys for  node1, Please enter password or leave blank
Lock key pair node1 with password [none]:
Generating public and private backup keys for  node1, Please enter password or leave blank
Lock key pair node1a with password [none]:
Is this a Block Maker Node? [y/N]: y
Is this the only block maker ? [y/N]: y
Is this a Voter Node? [y/N]: y
Do you wish to add more nodes? [y/N]: y
Please enter node name: node2
Generating public and private keys for  node2, Please enter password or leave blank
Lock key pair node2 with password [none]:
Generating public and private backup keys for  node2, Please enter password or leave blank
Lock key pair node2a with password [none]:
Is this a Voter Node? [y/N]: y
Do you wish to add more nodes? [y/N]:
I0426 16:18:50.059183 ethdb/database.go:81] Allotted 128MB cache and 1024 file handles to /quorum-maker/testnet0/node1/qdata/geth/chaindata
I0426 16:18:50.093249 ethdb/database.go:174] closed db:/quorum-maker/testnet0/node1/qdata/geth/chaindata
I0426 16:18:50.112327 ethdb/database.go:81] Allotted 128MB cache and 1024 file handles to /quorum-maker/testnet0/node1/qdata/geth/chaindata
I0426 16:18:50.191758 cmd/geth/main.go:232] successfully wrote genesis block and/or chain rule set: 0fd0704d82fbccd8ced4f162aaf63faf85ae535d7730eb9bead7f1874fdbd168
I0426 16:18:50.418379 ethdb/database.go:81] Allotted 128MB cache and 1024 file handles to /quorum-maker/testnet0/node2/qdata/geth/chaindata
I0426 16:18:50.447966 ethdb/database.go:174] closed db:/quorum-maker/testnet0/node2/qdata/geth/chaindata
I0426 16:18:50.455189 ethdb/database.go:81] Allotted 128MB cache and 1024 file handles to /quorum-maker/testnet0/node2/qdata/geth/chaindata
I0426 16:18:50.548635 cmd/geth/main.go:232] successfully wrote genesis block and/or chain rule set: 0fd0704d82fbccd8ced4f162aaf63faf85ae535d7730eb9bead7f1874fdbd168
Successfully created project: testnet0
Please use following public address for private transactions between nodes
--------------------------------------------------------------------------
node1 ZxWPGlDLPBPzYwG9+IM9kAlteRI9M8qlMVMHWk3Stjc=
node2 UmhuUF2UFrpUhRmPdyc/qealcLS13McOv+tReGx7fhE=
--------------------------------------------------------------------------
```

Note the node *public keys* and run the net:
```sh
cd testnet0
docker-compose up
```
You should get the following:
```sh
Creating network "testnet0_vpcbr" with driver "bridge"
Creating bootnode ... done
Creating node2    ... done
Creating node1    ... done
Attaching to bootnode, node2, node1
bootnode    | [*] Starting bootnode
node1       | [*] Starting Constellation on node1
node1       | [*] Started Constellation on node1
node1       | [*] Starting Quorum on node1
node1       | [*] Started Quorum on node1
node2       | [*] Starting Constellation on node2
node2       | [*] Started Constellation on node2
node2       | [*] Starting Quorum on node2
node2       | [*] Started Quorum on node2
```
Now enter the node1 in another shell:
```sh
docker exec -it node1 bash
```
To attach to the node run (type ``exit`` to leave node shell):
```sh
geth attach qdata/geth.ipc
```
Now you can load the smart contract, put the following script script1.js into ``quorum-maker/testnet0/node1`` (**privateFor** is the **node2** public key we got from ``./setup.sh`` script):
```javascript
a = eth.accounts[0]
web3.eth.defaultAccount = a;

// abi and bytecode generated from simplestorage.sol:
// > solcjs --bin --abi simplestorage.sol
var abi = [{"constant":true,"inputs":[],"name":"storedData","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"x","type":"uint256"}],"name":"set","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"get","outputs":[{"name":"retVal","type":"uint256"}],"payable":false,"type":"function"},{"inputs":[{"name":"initVal","type":"uint256"}],"payable":false,"type":"constructor"}];

var bytecode = "0x6060604052341561000f57600080fd5b604051602080610149833981016040528080519060200190919050505b806000819055505b505b610104806100456000396000f30060606040526000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680632a1afcd914605157806360fe47b11460775780636d4ce63c146097575b600080fd5b3415605b57600080fd5b606160bd565b6040518082815260200191505060405180910390f35b3415608157600080fd5b6095600480803590602001909190505060c3565b005b341560a157600080fd5b60a760ce565b6040518082815260200191505060405180910390f35b60005481565b806000819055505b50565b6000805490505b905600a165627a7a72305820d5851baab720bba574474de3d09dbeaabc674a15f4dd93b974908476542c23f00029";

var simpleContract = web3.eth.contract(abi);
var simple = simpleContract.new(42, {from:web3.eth.accounts[0], data: bytecode, gas: 0x47b760, privateFor: ["UmhuUF2UFrpUhRmPdyc/qealcLS13McOv+tReGx7fhE="]}, function(e, contract) {
if (e) {
	console.log("err creating contract", e);
} else {
	if (!contract.address) {
		console.log("Contract transaction send: TransactionHash: " + contract.transactionHash + " waiting to be mined...");
	} else {
		console.log("Contract mined! Address: " + contract.address);
		console.log(contract);
	}
}
});
```
Then run while inside the node1:
```sh
geth --exec "loadScript(\"script1.js\")" attach ipc:qdata/geth.ipc
```
You should get something like(please note the *tranaction ID*):
```sh
Contract transaction send: TransactionHash: 0x8a2cdbee6fa215d85d4df7724b1844362f0e494418cdaae57f655312299dfc54 waiting to be mined...
```
Check if transaction loaded:
```sh
geth attach qdata/geth.ipc
eth.getTransaction("0x8a2cdbee6fa215d85d4df7724b1844362f0e494418cdaae57f655312299dfc54")
```
You should get something like:
```javascript
{
blockHash: "0x95fcbf3bb61aa7524dd2155cd1743f9932a67c63e0148ae3bae0fbafd1a9b376",
blockNumber: 5887,
from: "0x90ceaa13ef954dcdee1c23255b9bd5947d0afa4b",
gas: 4700000,
gasPrice: 0,
hash: "0x8a2cdbee6fa215d85d4df7724b1844362f0e494418cdaae57f655312299dfc54",
input: "0xfa2fdbd486d49a71300c4f81ec5c0d57afe9d04d6767672563a538a0505de618954a9d5691019eb4ad42c9a8ddf45c7bc02b0bc2bcb9c4d8d9dd7eec9e98365b",
nonce: 0,
r: "0xa7a082675d45ff46a53e25540908e8b32c0e15fd972663719f04495d91414f80",
s: "0x57fe304a7e5a711d872510b87257692a6f40569d09e9d6562974f865288b5441",
to: null,
transactionIndex: 2,
v: "0x25",
value: 0
}
```
Get transaction receipt:
```sh
eth.getTransactionReceipt("0x8a2cdbee6fa215d85d4df7724b1844362f0e494418cdaae57f655312299dfc54")
```
You should get something like(note contractAddress):
```javascript
{
blockHash: "0x95fcbf3bb61aa7524dd2155cd1743f9932a67c63e0148ae3bae0fbafd1a9b376",
blockNumber: 5887,
contractAddress: "0xf4ef34a9a7490f063e2577c323cf02f3bcc8cb68",
cumulativeGasUsed: 123351,
from: "0x90ceaa13ef954dcdee1c23255b9bd5947d0afa4b",
gasUsed: 0,
logs: [],
logsBloom: "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
root: "0x60599534d1132e34ae156e65a2e09ee56c13266721bd0e1d124136f0d2e79a19",
to: null,
transactionHash: "0x8a2cdbee6fa215d85d4df7724b1844362f0e494418cdaae57f655312299dfc54",
transactionIndex: 2
}
```
Note contractAddress and define it into variable (**undefined** - means OK :):
```javascript
var address = "0xf4ef34a9a7490f063e2577c323cf02f3bcc8cb68";
```
Define contract ABI (**undefined** - means OK :):
```javascript
var abi = [{"constant":true,"inputs":[],"name":"storedData","outputs":[{"name":"","type":"uint256"}],"payable":false,"type":"function"},{"constant":false,"inputs":[{"name":"x","type":"uint256"}],"name":"set","outputs":[],"payable":false,"type":"function"},{"constant":true,"inputs":[],"name":"get","outputs":[{"name":"retVal","type":"uint256"}],"payable":false,"type":"function"},{"inputs":[{"name":"initVal","type":"uint256"}],"type":"constructor"}];
```
Reference smart contract (**undefined** - means OK :):
```javascript
var private = eth.contract(abi).at(address)
```
Call the methods on the contract:
```javascript
private.get()
```
You should get ``42``

### Build web3j
```bash
git clone https://github.com/web3j/web3j.git
cd web3j
./gradlew
./gradlew check
```
All quorum libraries with dependendecies are bundled in ``./console/build/libs/console-3.4.0-all.jar``

Libraies list:

  - ./crypto/build/libs/crypto-3.4.0.jar
  - ./geth/build/libs/geth-3.4.0.jar
  - ./core/build/libs/core-3.4.0.jar
  - ./infura/build/libs/infura-3.4.0.jar
  - ./tuples/build/libs/tuples-3.4.0.jar
  - ./parity/build/libs/parity-3.4.0.jar
  - ./utils/build/libs/utils-3.4.0.jar
  - ./codegen/build/libs/codegen-3.4.0.jar
  - ./rlp/build/libs/rlp-3.4.0.jar
  - ./abi/build/libs/abi-3.4.0.jar
  - ./console/build/libs/console-3.4.0.jar

### Build web3j-quorum
```bash
git clone https://github.com/web3j/quorum.git
cd quorum
./gradlew 
./gradlew jar
```
A web3j Quorum plugin is at ``./build/libs/quorum-0.8.0.jar``

### Solidity smart contract wrappers

https://docs.web3j.io/smart_contracts.html#solidity-smart-contract-wrappers
> web3j supports the auto-generation of smart contract function wrappers in Java from Solidity ABI files.

To re-generate Solidity smart contract wrappers:
```sh
web3j solidity generate simplestorage.bin simplestorage.abi -o ../src/ -p com.softjourn.dev.quorumtutorial
```

#### Building for source

Download [IntelliJ IDEA community edition](https://www.jetbrains.com/idea/download/#section=mac)

Get the sources:
```sh
git clone https://github.com/SoftJourn/quorum-tutorial.git
cd quorum-tutorial/quorum-java-tutorial
```

Copy ``console-3.4.0-all.jar``	and ``quorum-0.8.0.jar`` from the previous steps above to *quorum-tutorial/quorum-java-tutorial/libs*

Open the project in the IDE and change the public keys with the one generated by ``./setup.sh`` above:
```java
    private static String node1 = "J14OtqQK1lhaxV90XdbfZkzispEDK2FpyZ4sLFaAUgw=";
    private static String node2 = "Lm2d5O+d43WIxxY0aUt4IGiss0jnwC0qtrkH/Wpvilk=";
```

Run the project and you should get something like the following:

```sh
Quorum tutorial
0xea2d0f160d6a4d307e3778c87c2ce3f632571015
Contract address: 0xa382023dbd253e728482b21bf2ccd5a3b720b2cd
get: 42
set(x): TransactionReceipt{transactionHash='0xde350779b8cf05625869da076b9d5fbaed50e09ca235230b3c1c9f0894d5ce91', transactionIndex='0x1', blockHash='0xc11f1cc4d9d83cf0f02f1a8ac85ae8cf8b489d32263b8aeb32ca2f0d5312f60c', blockNumber='0x200', cumulativeGasUsed='0x1671c', gasUsed='0x0', contractAddress='null', root='0xa80e0619d05d8ea26b23599b8a267fa01542e3f7dedf9cfafbcddd8c7dd522df', status='null', from='0xea2d0f160d6a4d307e3778c87c2ce3f632571015', to='0xa382023dbd253e728482b21bf2ccd5a3b720b2cd', logs=[], logsBloom='0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000'}
get(): 5
```

### Todos

 - Write MORE test function calls
 - More complex example like ERC20 token

License
----

MIT


