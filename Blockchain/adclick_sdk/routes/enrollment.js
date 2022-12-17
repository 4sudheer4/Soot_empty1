const express = require("express");
const router = express.Router();
const enrollmentController = require("../controllers/enrollment.js");

//tags seen below in swagger are used to give heading to group of apis.

/**
 * @swagger
 * tags:
 *   - name: Enrollment APIS
 *     description: "APIS to enroll admin and register user into the system"
 * components:
 *   schemas:
 *     Admin:
 *       type: object
 *       required:
 *         - adminId
 *         - orgName
 *         - enrollmentID
 *         - enrollmentSecret
 *       properties:
 *         adminId:
 *           type: string
 *           description: The id of admin user
 *         orgName:
 *           type: string
 *           description: The name of the organization
 *         enrollmentID:
 *           type: string
 *           description: The enrollment id of the admin user used while registering the admin 
 *         enrollmentSecret:
 *           type: string
 *           description: The enrollment secret used while registering the admin
 *     User:
 *       type: object
 *       required:
 *         - adminId
 *         - userId
 *         - orgName
 *       properties:
 *         adminId:
 *           type: string
 *           description: The id of the admin.
 *         userId:
 *           type: string
 *           description: The id of the user.
 *         orgName:
 *           type: string
 *           description: The name of the organization.
 */

/**
 * @swagger
 * /enrollment/enrollAdmin:
 *   post:
 *    summary: Api to enroll admin user in blockchain system.
 *    tags:
 *      - Enrollment APIS
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *           schema:
 *             $ref: '#/components/schemas/Admin'
 *    responses:
 *      200:
 *        description: Admin has been successfully enrolled
 */
router.post("/enrollAdmin", enrollmentController.enrollAdmin);

/**
 * @swagger
 * /enrollment/registerUser:
 *   post:
 *    tags:
 *      - Enrollment APIS
 *    summary: Api to register and enroll user in blockchain system.
 *    requestBody:
 *      required: true
 *      content:
 *        application/json:
 *           schema:
 *             $ref: '#/components/schemas/User'
 *    responses:
 *      200:
 *        description: User has been successfully register and enrolled.
 */
router.post("/registerUser", enrollmentController.registerUser);

module.exports = router;
