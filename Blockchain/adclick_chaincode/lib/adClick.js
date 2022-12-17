/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

// Deterministic JSON.stringify()
const stringify  = require('json-stringify-deterministic');
const { Contract } = require('fabric-contract-api');

class AdClick extends Contract {

    
    async adClickLogsStore(ctx, logDataJsonArrayString) {
    
        const logDataJsonArray = JSON.parse(logDataJsonArrayString);
        console.log(logDataJsonArray.length)

        
        for(var logDataJson of logDataJsonArray)
        {
         var id = logDataJson.col1.toString() + logDataJson.col2.toString()+logDataJson.time;
         await ctx.stub.putState(id,Buffer.from(JSON.stringify(logDataJson)))    
        }
        return "All the logs are successfully added in the system"
    }

    
    async adClickLogsFetch(ctx, col1,col2,timestamp) {

        var id = col1+col2+timestamp;
        const adClickLogsJson = await ctx.stub.getState(id); // get the asset from chaincode state
        if (!adClickLogsJson) {
            throw new Error(`The log with  ${id} does not exist`);
        }
        return adClickLogsJson.toString();
    }

    
    async adClickLogsFetchAll(ctx) {
        const allLogs = [];
        // range query with empty string for startKey and endKey does an open-ended query of all assets in the chaincode namespace.
        const iterator = await ctx.stub.getStateByRange('', '');
        let result = await iterator.next();
        while (!result.done) {
            const strValue = Buffer.from(result.value.value.toString()).toString('utf8');
            let record = JSON.parse(strValue);
            allLogs.push(record);
            result = await iterator.next();
    }
    return JSON.stringify(allLogs);
    }

    async executeQuery(ctx, query) {
        let result;
        let resultList = [];
        let res = await ctx.stub.getQueryResult(query);
        while (true) {
            let temp = await res.next();
            console.log(temp)
            if (!temp || !temp.value || !temp.value.key) {
                break;
            }
            console.log(temp.value.value.toString())
            result = JSON.parse(temp.value.value.toString());
            resultList.push(result);
        }

        return resultList;
    }
    
}

module.exports = AdClick;
