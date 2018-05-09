# Quorum Node.js Tutorial

Web3 API Ethereum compatible JavaScript API which is extended to support the Quorum API.

 - https://www.npmjs.com/package/web3-quorum

```sh
git clone https://github.com/SoftJourn/quorum-tutorial.git
cd quorum-tutorial/quorum-nodejs-tutorial
npm install
node app.j
```

You should get something like:

```sh
0x1b714777b2f217304d7ea06b4f7a49c7f1acd497
1000009690000000000000000000
```

### Web3-quorum Installation

#### Node.js

```sh
npm install web3-quorum
```

##### Quorum functions

```javascript
web3.quorum.nodeInfo
web3.quorum.isBlockMaker(address)
web3.quorum.isVoter(address)
web3.quorum.canonicalHash(blockHash)
web3.quorum.makeBlock()
web3.quorum.vote()
web3.quorum.pauseBlockMaker()
web3.quorum.resumeBlockMaker()
```

