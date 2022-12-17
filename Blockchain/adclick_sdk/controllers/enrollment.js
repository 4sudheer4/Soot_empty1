const { Wallets } = require("fabric-network");
const appUtil = require("../utils/appUtil.js");

const enrollAdmin = async (req, res) => {
  try {
    const adminId = req.body.adminId;
    const orgName = req.body.orgName;
    const enrollmentID = req.body.enrollmentID;
    const enrollmentSecret = req.body.enrollmentSecret;
    const mspID = orgName+"MSP";

    

    const firstLetter = mspID.charAt(0)
    const firstLetterCap = firstLetter.toUpperCase()
    const remainingLetters = mspID.slice(1)

     const mspIDCapital= firstLetterCap + remainingLetters

    console.log(adminId);
    console.log(orgName);
    console.log(enrollmentID);
    console.log(enrollmentSecret);
    console.log(mspID)
    console.log(mspIDCapital);

    // load the network configuration
    const ccp = appUtil.buildCCP(orgName);

    console.log(ccp)

    // Create a new CA client for interacting with the CA.

    const ca = appUtil.buildCAClient(ccp, orgName);

    // Create a new file system based wallet for managing identities.

    const wallet = await appUtil.buildWallet(Wallets);
    console.log(wallet);

    // Check to see if we've already enrolled the admin user.
    const identity = await wallet.get(adminId);
    if (identity) {
      console.log(
        `An identity for the admin user ${adminId} already exists in the wallet`
      );
      return res.send(`An identity for the admin user ${adminId} already exists in the wallet`);
    }

    // Enroll the admin user, and import the new identity into the wallet.
    const enrollment = await ca.enroll({
      enrollmentID: enrollmentID,
      enrollmentSecret: enrollmentSecret,
    });
    const x509Identity = {
      credentials: {
        certificate: enrollment.certificate,
        privateKey: enrollment.key.toBytes(),
      },
      mspId: mspIDCapital,
      type: "X.509",
    };
    await wallet.put(adminId, x509Identity);
    console.log(
      `Successfully enrolled admin ${adminId} and imported it into the wallet`
    );
    res.send(
      `Successfully enrolled admin ${adminId} and imported it into the wallet`
    );
  } catch (error) {
    return res.send(`Failed to enroll admin user "admin": ${error}`);
  }
};

const registerUser = async (req, res) => {
  try {
    const userId = req.body.userId;
    const adminId = req.body.adminId;
    const orgName = req.body.orgName;

    const mspID = orgName + "MSP";
    const firstLetter = mspID.charAt(0)
    const firstLetterCap = firstLetter.toUpperCase()
    const remainingLetters = mspID.slice(1)

     const mspIDCapital= firstLetterCap + remainingLetters
    console.log(userId);
    console.log(adminId);
    console.log(mspID);
    console.log(mspIDCapital)
    // load the network configuration
    const ccp = appUtil.buildCCP(orgName);
    console.log(ccp);
    // Create a new CA client for interacting with the CA.
    const ca = appUtil.buildCAClient(ccp, orgName);
    // Create a new file system based wallet for managing identities.

    const wallet = await appUtil.buildWallet(Wallets);

    // Check to see if we've already enrolled the user.
    const userIdentity = await wallet.get(userId);
    if (userIdentity) {
      return res.send(
        `An identity for the user ${userId} already exists in the wallet`
      );
    }

    // Check to see if we've already enrolled the admin user.
    const adminIdentity = await wallet.get(adminId);
    if (!adminIdentity) {
      return res.send(
        `An identity for the admin ${adminId} does not exist in the wallet. Hit the enrollAdmin api and try again.`
      );
    }
  
    // build a user object for authenticating with the CA
    const provider = wallet
      .getProviderRegistry()
      .getProvider(adminIdentity.type);
    const adminUser = await provider.getUserContext(adminIdentity, adminId);
    
    /*
    For attribute based access control you only need the below 2 changes which are commented with abac written*/
    // Register the user, enroll the user, and import the new identity into the wallet.
    const secret = await ca.register(
      {
        affiliation: "org1.department1",
        enrollmentID: userId,
        role: "client",
        //attrs: [{ name: "Kshitij", value: "Patil", ecert: true }], abac
      },
      adminUser
    );
    const enrollment = await ca.enroll({
      enrollmentID: userId,
      enrollmentSecret: secret,
      //attr_reqs: [{ name: "Kshitij", optional: false }], abac
    });
    const x509Identity = {
      credentials: {
        certificate: enrollment.certificate,
        privateKey: enrollment.key.toBytes(),
      },
      mspId: mspIDCapital,
      type: "X.509",
    };
    await wallet.put(userId, x509Identity);
    res.send(
      `Successfully registered and enrolled user ${userId} and imported it into the wallet`
    );
  } catch (error) {
    return res.send(`Failed to register user: ${error}`);
  }
};

module.exports = {
  enrollAdmin,
  registerUser,
};
