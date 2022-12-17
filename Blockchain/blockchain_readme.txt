Firstly install all the required dependencies to run Hyperledger Fabric on your system.
A detailed steps on installation can be found at https://hyperledger-fabric.readthedocs.io/en/release-2.2/install.html
Follow the steps and you will end up with a fabric samples folder on your system. 
Inside fabric samples you will find a "bin" folder.Just add the path of bin variable to your path variable in bashrc file

Now navigate inside blockchain folder. 
Inside blockchain folder we have 3 subfolders
1) adclick_chaincode consists of our chaincode
2) adclick_sdk consists of our node sdk
3) fabric-samples consists of your script files required to setup a network.

Setting up the network
Step 1) Navigate to /blockchain/fabric-samples/test-network

Now from inside the test-network hit the following commands
./network.sh up -ca -s couchdb
./network.sh createChannel
./network.sh deployCC

This will setup our network and it will deploy the chaincode on the network

Step 2) Now navigate to /blockchain/adclick_sdk and execute npm start
This will start our nodejs application on port 5000

You can see the swagger at http://localhost:5000/api-docs

Now your setup is complete and you can play with the code.
