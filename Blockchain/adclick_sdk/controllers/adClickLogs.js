const { Gateway, Wallets } = require("fabric-network");
const fs = require("fs");
const path = require("path");
const satelize = require("satelize");
const appUtil = require("../utils/appUtil.js");


const store = async (req, res) => {
  try {
    var logDataJsonArrayBuffer = req.files.file.data;
    var logDataJsonArrayString = logDataJsonArrayBuffer.toString();
    console.log(JSON.parse(logDataJsonArrayString).length);
    
    var userId =req.body.userId;
    var orgName= req.body.orgName;

    console.log(userId);
    console.log(orgName);
    //console.log(logDataJsonString);
    
     let ccp = appUtil.buildCCP(orgName);

    // //console.log(Math.floor(Date.now() / 1000)); //3

    // // Create a new file system based wallet for managing identities.
     const wallet = await appUtil.buildWallet(Wallets);

    // //console.log(Math.floor(Date.now() / 1000)); //4

    // // Check to see if we've already enrolled the user.
     const identity = await wallet.get(userId);

    // /*
    // Here remember res.send will send response but the code after it will continue its execution
    // whereas return res.send will send response and will also return from the code and the line
    // of code after the return statement wont execute. In our case if we use res.send and let say 
    // the idntity doesnt exist, then it will send response that the identity doesnt exist but it 
    // will go on to execute further lines of code and there you will get error and the app will crash
    // and you will have to manually start it. so use return statement
    // */
    if (!identity) {
      return res.send(
        `An identity for the user ${userId} does not exist in the wallet.`
      );
    }

    // //console.log(Math.floor(Date.now() / 1000)); //5

    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, {
      wallet,
      identity: userId,
      discovery: { enabled: true, asLocalhost: true },
      eventHandlerOptions: {
        strategy: null,
      },
    });

    //console.log(Math.floor(Date.now() / 1000)); //6

    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork("mychannel");

    // Get the contract from the network.
    const contract = network.getContract("adClick");

    // //console.log(Math.floor(Date.now() / 1000)); //7

    
    // //Value return by chaincode is always a buffer object
    const adClickLogsStoreMessage = await contract.submitTransaction(
      "adClickLogsStore",
      logDataJsonArrayString
    );

    // //console.log(Math.floor(Date.now() / 1000)); //8
     res.send(adClickLogsStoreMessage.toString());

    // //console.log(Math.floor(Date.now() / 1000));
    // // Disconnect from the gateway.
     await gateway.disconnect();

    //console.log(Math.floor(Date.now() / 1000));
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};

const fetch = async (req, res) => {
  try {
    var col1 = req.body.col1;
    var col2 = req.body.col2;
    var timestamp = req.body.timestamp;
    
    var userId =req.body.userId;
    var orgName= req.body.orgName;

    console.log(userId);
    console.log(orgName);
    //console.log(logDataJsonString);
    
     let ccp = appUtil.buildCCP(orgName);

    // //console.log(Math.floor(Date.now() / 1000)); //3

    // // Create a new file system based wallet for managing identities.
     const wallet = await appUtil.buildWallet(Wallets);

    // //console.log(Math.floor(Date.now() / 1000)); //4

    // // Check to see if we've already enrolled the user.
     const identity = await wallet.get(userId);

    // /*
    // Here remember res.send will send response but the code after it will continue its execution
    // whereas return res.send will send response and will also return from the code and the line
    // of code after the return statement wont execute. In our case if we use res.send and let say 
    // the idntity doesnt exist, then it will send response that the identity doesnt exist but it 
    // will go on to execute further lines of code and there you will get error and the app will crash
    // and you will have to manually start it. so use return statement
    // */
    if (!identity) {
      return res.send(
        `An identity for the user ${userId} does not exist in the wallet.`
      );
    }

    // //console.log(Math.floor(Date.now() / 1000)); //5

    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, {
      wallet,
      identity: userId,
      discovery: { enabled: true, asLocalhost: true },
      eventHandlerOptions: {
        strategy: null,
      },
    });

    //console.log(Math.floor(Date.now() / 1000)); //6

    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork("mychannel");

    // Get the contract from the network.
    const contract = network.getContract("adClick");

    // //console.log(Math.floor(Date.now() / 1000)); //7

    
    // //Value return by chaincode is always a buffer object
    const adClickLog = await contract.submitTransaction(
      "adClickLogsFetch",
      col1,
      col2,
      timestamp

    );

    // //console.log(Math.floor(Date.now() / 1000)); //8
     res.send(adClickLog.toString());

    // //console.log(Math.floor(Date.now() / 1000));
    // // Disconnect from the gateway.
     await gateway.disconnect();

    //console.log(Math.floor(Date.now() / 1000));
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};

const fetchAll = async (req, res) => {
  try {
    var userId =req.body.userId;
    var orgName= req.body.orgName;

    console.log(userId);
    console.log(orgName);
    //console.log(logDataJsonString);
    
     let ccp = appUtil.buildCCP(orgName);

    // //console.log(Math.floor(Date.now() / 1000)); //3

    // // Create a new file system based wallet for managing identities.
     const wallet = await appUtil.buildWallet(Wallets);

    // //console.log(Math.floor(Date.now() / 1000)); //4

    // // Check to see if we've already enrolled the user.
     const identity = await wallet.get(userId);

    // /*
    // Here remember res.send will send response but the code after it will continue its execution
    // whereas return res.send will send response and will also return from the code and the line
    // of code after the return statement wont execute. In our case if we use res.send and let say 
    // the idntity doesnt exist, then it will send response that the identity doesnt exist but it 
    // will go on to execute further lines of code and there you will get error and the app will crash
    // and you will have to manually start it. so use return statement
    // */
    if (!identity) {
      return res.send(
        `An identity for the user ${userId} does not exist in the wallet.`
      );
    }

    // //console.log(Math.floor(Date.now() / 1000)); //5

    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, {
      wallet,
      identity: userId,
      discovery: { enabled: true, asLocalhost: true },
      eventHandlerOptions: {
        strategy: null,
      },
    });

    //console.log(Math.floor(Date.now() / 1000)); //6

    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork("mychannel");

    // Get the contract from the network.
    const contract = network.getContract("adClick");

    // //console.log(Math.floor(Date.now() / 1000)); //7

    
    // //Value return by chaincode is always a buffer object
    const adClickLogs = await contract.submitTransaction(
      "adClickLogsFetchAll"
    );
    console.log(typeof adClickLogs)
    // //console.log(Math.floor(Date.now() / 1000)); //8
     res.send(adClickLogs.toString());

    // //console.log(Math.floor(Date.now() / 1000));
    // // Disconnect from the gateway.
     await gateway.disconnect();

    //console.log(Math.floor(Date.now() / 1000));
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};

const fetchCondition = async (req, res) => {
  try {
    const userId = req.body.userId;
    const columnName = req.body.columnName;
    const columnValue = req.body.columnValue;
    const orgName = req.body.orgName;
    console.log(columnValue)
    // load the network configuration
    let ccp = appUtil.buildCCP(orgName);

    // //console.log(Math.floor(Date.now() / 1000)); //3

    // // Create a new file system based wallet for managing identities.
     const wallet = await appUtil.buildWallet(Wallets);

    // //console.log(Math.floor(Date.now() / 1000)); //4

    // // Check to see if we've already enrolled the user.
     const identity = await wallet.get(userId);

    // /*
    // Here remember res.send will send response but the code after it will continue its execution
    // whereas return res.send will send response and will also return from the code and the line
    // of code after the return statement wont execute. In our case if we use res.send and let say 
    // the idntity doesnt exist, then it will send response that the identity doesnt exist but it 
    // will go on to execute further lines of code and there you will get error and the app will crash
    // and you will have to manually start it. so use return statement
    // */
    if (!identity) {
      return res.send(
        `An identity for the user ${userId} does not exist in the wallet.`
      );
    }

    // //console.log(Math.floor(Date.now() / 1000)); //5

    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, {
      wallet,
      identity: userId,
      discovery: { enabled: true, asLocalhost: true },
      eventHandlerOptions: {
        strategy: null,
      },
    });

    //console.log(Math.floor(Date.now() / 1000)); //6

    // Get the network (channel) our contract is deployed to.
    const network = await gateway.getNetwork("mychannel");

    // Get the contract from the network.
    const contract = network.getContract("adClick");

  

    let getLogsSelectorQuery = `{\"selector\": {\"${columnName}\": {\"$eq\": \"${columnValue}\"}}}`;
    

    const getLogsListBuffer = await contract.submitTransaction(
      "executeQuery",
      getLogsSelectorQuery
    );

    //console.log(getLogsListBuffer.toString())
    /*
    Here purchaseOrder array is received after executing executeQuery and it is always a buffer object you need to convert it. 
    */

    res.send(getLogsListBuffer.toString());

    // Disconnect from the gateway.
    await gateway.disconnect();
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};



module.exports = {
  store,
  fetch,
  fetchAll,
  fetchCondition
};
