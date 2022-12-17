const fs = require("fs");
const path = require("path");
const FabricCAServices = require("fabric-ca-client");

const buildCCP = (orgName) => {
  const folderName = orgName+".example.com"
  const connectionProfile = "connection-"+orgName+".json"
  console.log(folderName)
  console.log(connectionProfile)
  try {
    // load the network configuration
    const ccpPath = path.resolve(
      __dirname,
      "..",
      "..",
      "fabric-samples",
      "test-network",
      "organizations",
      "peerOrganizations",
      folderName,
      connectionProfile
    );
    const ccp = JSON.parse(fs.readFileSync(ccpPath, "utf8"));
    return ccp;
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};
const buildWallet = async (Wallets) => {
  try {
    const walletPath = path.join(process.cwd(), "wallet");
    const wallet = await Wallets.newFileSystemWallet(walletPath);
    console.log(`Wallet path: ${walletPath}`);
    return wallet;
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};
const buildCAClient = (ccp, orgName) => {
  try {
    caName = "ca."+orgName+".example.com"
    const caInfo = ccp.certificateAuthorities[caName];
    const caTLSCACerts = caInfo.tlsCACerts.pem;
    const ca = new FabricCAServices(
      caInfo.url,
      { trustedRoots: caTLSCACerts, verify: false },
      caInfo.caName
    );
    return ca;
  } catch (error) {
    return res.send(`Failed to submit transaction: ${error}`);
  }
};

module.exports = { buildCCP, buildWallet, buildCAClient };
