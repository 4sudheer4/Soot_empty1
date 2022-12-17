const express = require("express");
const router = express.Router();
const adClickController = require("../controllers/adClickLogs.js");

//tags seen below in swagger are used to give heading to group of apis.

/**
 * @swagger
 * tags:
 *   - name: AD CLICK LOG APIS
 *     description: "APIs to manage the ad click logs"
 * components:
 *   schemas:
 *     adClickLogsStore:
 *       type: object
 *       required:
 *         - userId
 *         - orgName
 *       properties:
 *         userId:
 *           type: string
 *           description: ID of the user
 *         orgName:
 *           type: string
 *           description: The name of the org to which the user belongs   
 *         file:
 *           type: string
 *           format: base64
 *     adClickLogsFetch:
 *       type: object
 *       required:
 *         - userId
 *         - orgName
 *         - col1
 *         - col2
 *         - timestamp
 *       properties:
 *         userId:
 *           type: string
 *           description: ID of the user
 *         orgName:
 *           type: string
 *           description: The name of the org to which the user belongs
 *         col1:
 *           type: string
 *           description: The col1 hash value
 *         col2:
 *           type: string
 *           description: The col2 hash value
 *         timestamp:
 *           type: string
 *           description: The timsetamp of the log
 *     adClickLogsFetchAll:
 *       type: object
 *       required:
 *         - userId
 *         - orgName
 *       properties:
 *         userId:
 *           type: string
 *           description: ID of the user
 *         orgName:
 *           type: string
 *           description: The name of the org to which the user belongs
 *     adClickLogsFetchCondition:
 *       type: object
 *       required:
 *         - userId
 *         - orgName
 *         - columnName
 *         - columnValue
 *       properties:
 *         userId:
 *           type: string
 *           description: ID of the user
 *         orgName:
 *           type: string
 *           description: The name of the org to which the user belongs
 *         columnName:
 *           type: string
 *           description: The name of the column
 *         columnValue:
 *           type: string
 *           description: The value of the column you want to query your data on 
 */


/**
 * @swagger
 * /adClickLogs/store:
 *   post:
 *    summary: Api used by to store the ad click logs on blockchain.
 *    tags:
 *      - AD CLICK LOG APIS
 *    requestBody:
 *      required: true
 *      content:
 *        multipart/form-data:
 *            schema:
 *               $ref: '#/components/schemas/adClickLogsStore'
 *    responses:
 *      200:
 *        description: Ad click logs successfully addded to blockchain
 */
router.post("/store", adClickController.store);

/**
 * @swagger
 * /adClickLogs/fetch:
 *   post:
 *    summary: API used to fetch a adClick log 
 *    tags:
 *      - AD CLICK LOG APIS
 *    requestBody:
 *      required: true
 *      content:
 *         application/json:
 *            schema:
 *              $ref: '#/components/schemas/adClickLogsFetch'
 *    responses:
 *      200:
 *        description: Ad Click Log fetched successfully
 */
router.post("/fetch", adClickController.fetch);

/**
 * @swagger
 * /adClickLogs/fetchAll:
 *   post:
 *    summary: API used to fetch all the adClick logs 
 *    tags:
 *      - AD CLICK LOG APIS
 *    requestBody:
 *      required: true
 *      content:
 *         application/json:
 *            schema:
 *              $ref: '#/components/schemas/adClickLogsFetchAll'
 *    responses:
 *      200:
 *        description: Ad Click Logs fetched successfully
 */
 router.post("/fetchAll", adClickController.fetchAll);

 /**
 * @swagger
 * /adClickLogs/fetchCondition:
 *   post:
 *    summary: API used to fetch logs based on a speific condition 
 *    tags:
 *      - AD CLICK LOG APIS
 *    requestBody:
 *      required: true
 *      content:
 *         application/json:
 *            schema:
 *              $ref: '#/components/schemas/adClickLogsFetchCondition'
 *    responses:
 *      200:
 *        description: Ad Click Logs fetched successfully
 */
  router.post("/fetchCondition", adClickController.fetchCondition);


// /**
//  * @swagger
//  * /purchaseOrder/viewAllManufacturer:
//  *   post:
//  *    summary: API used by manufacturer to view all the purchase Orders
//  *    tags:
//  *      - Manufacturer APIs
//  *    requestBody:
//  *      required: true
//  *      content:
//  *         application/json:
//  *            schema:
//  *              $ref: '#/components/schemas/poViewAllManufacturer'
//  *    responses:
//  *      200:
//  *        description: Purchase Orders fetched successfully
//  */
// router.post(
//   "/viewAllManufacturer",
//   purchaseOrderController.viewAllManufacturer
// );

// /**
//  * @swagger
//  * /purchaseOrder/viewAllSupplier:
//  *   post:
//  *    summary: API used by supplier to view all the purchase Orders
//  *    tags:
//  *      - Supplier APIS
//  *    requestBody:
//  *      required: true
//  *      content:
//  *         application/json:
//  *            schema:
//  *              $ref: '#/components/schemas/poViewAllSupplier'
//  *    responses:
//  *      200:
//  *        description: Purchase Orders fetched successfully
//  */
// router.post("/viewAllSupplier", purchaseOrderController.viewAllSupplier);

// /**
//  * @swagger
//  * /purchaseOrder/history:
//  *   post:
//  *    summary: API used by supplier/manufacturer to get an history of a  purchase Order
//  *    tags:
//  *      - Common APIs
//  *    requestBody:
//  *      required: true
//  *      content:
//  *         application/json:
//  *            schema:
//  *              $ref: '#/components/schemas/poHistory'
//  *    responses:
//  *      200:
//  *        description: Purchase Order History fetched successfully.
//  */
// router.post("/history", purchaseOrderController.history);

module.exports = router;
