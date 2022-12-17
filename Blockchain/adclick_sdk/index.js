const express = require("express");
const app = express();

const swaggerJsDoc = require("swagger-jsdoc");
const swaggerUi = require("swagger-ui-express");
var fileupload = require("express-fileupload");

//const userRoutes= require("./routes/userroutes");
app.use(express.json()); //This is used in order for req.body.abc statemnets to work fine.
app.use(fileupload());
const swaggerOptions = {
  definition: {
    openapi: "3.0.3",
    info: {
      title: "AdClick  blockchain system apis",
      version: "1.0.0",
      description: "Apis for managing the the logs in the adclick blockchain system",
    },
    servers: [
      {
        url: "http://localhost:5000",
      },
    ],
    components: {
      securitySchemes: {
        bearerAuth: {
          type: "apiKey",
          name: "Authorization",
          scheme: "bearer",
          bearerFormat: "JWT",
          in: "header",
        },
      },
    },

    securityDefinitions: {
      bearerAuth: {
        type: "apiKey",
        name: "Authorization",
        scheme: "bearer",
        in: "header",
      },
    },
  },
  apis: ["./routes/*.js"],
};

const swaggerDocs = swaggerJsDoc(swaggerOptions);
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocs));

const enrollmentRoutes = require("./routes/enrollment.js");
app.use("/enrollment", enrollmentRoutes);

const adClickLogRoutes = require("./routes/adClickLogs.js");
app.use("/adClickLogs", adClickLogRoutes);


//https://stackoverflow.com/questions/47797322/node-js-server-only-listening-on-ipv6
//That 127.0.0.1 is added because if you dont add and do netstat -tln it only shows ip6 equivalent and no ipv4
//. Also in ipv6 it shows unindentified addres. Though it listens on localhost but its specified as unidentified.
//If you do like below you will see it properly now. check with netstat -tln
// app.listen(5000,'127.0.0.1' () => {
//   console.log("app is listening on port 5000");
// });
app.listen(5000, () => {
  console.log("app is listening on port 5000");
});
