/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { inspect, InspectOptions } from 'util';
import Page from '../../../../base/Page';
import Response from '../../../../http/response';
import V2010 from '../../V2010';
const deserialize = require('../../../../base/deserialize');
const serialize = require('../../../../base/serialize');


/**
 * Options to pass to update a AWSInstance
 *
 * @property { string } [testString] 
 * @property { boolean } [testBoolean] 
 */
export interface AWSContextUpdateOptions {
    testString?: string;
    testBoolean?: boolean;
}

/**
* Options to pass to each
*
* @property { number } [pageSize] 
* @property { Function } [callback] -
*                         Function to process each record. If this and a positional
*                         callback are passed, this one will be used
* @property { Function } [done] - Function to be called upon completion of streaming
* @property { number } [limit] -
*                         Upper limit for the number of records to return.
*                         each() guarantees never to return more than limit.
*                         Default is no limit
*/
export interface AWSListInstanceEachOptions {
    pageSize?: number;
    callback?: (item: AWSInstance, done: (err?: Error) => void) => void;
    done?: Function;
    limit?: number;
}

/**
* Options to pass to list
*
* @property { number } [pageSize] 
* @property { number } [limit] -
*                         Upper limit for the number of records to return.
*                         list() guarantees never to return more than limit.
*                         Default is no limit
*/
export interface AWSListInstanceOptions {
    pageSize?: number;
    limit?: number;
}

/**
* Options to pass to page
*
* @property { number } [pageSize] 
* @property { number } [pageNumber] - Page Number, this value is simply for client state
* @property { string } [pageToken] - PageToken provided by the API
*/
export interface AWSListInstancePageOptions {
    pageSize?: number;
    pageNumber?: number;
    pageToken?: string;
}



export interface AWSContext {


    /**
     * Remove a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<boolean>


    /**
     * Fetch a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    fetch(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>


    /**
     * Update a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    update(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>;
    /**
     * Update a AWSInstance
     *
     * @param { AWSContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    update(params: AWSContextUpdateOptions, callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>;
    update(params?: any, callback?: any): Promise<AWSInstance>


    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class AWSContextImpl implements AWSContext {
    protected _solution: AWSSolution;
    protected _uri: string;


    constructor(protected _version: V2010, sid: string) {
        this._solution = { sid };
        this._uri = `/v1/Credentials/AWS/${sid}`;
    }

    remove(callback?: any): Promise<boolean> {

        let operationVersion = this._version,
            operationPromise = operationVersion.remove({ uri: this._uri, method: 'DELETE' });
        

        operationPromise = operationVersion.setPromiseCallback(operationPromise,callback);
        return operationPromise;



    }

    fetch(callback?: any): Promise<AWSInstance> {

        let operationVersion = this._version,
            operationPromise = operationVersion.fetch({ uri: this._uri, method: 'GET' });
        
        operationPromise = operationPromise.then(payload => new AWSInstance(operationVersion, payload, this._solution.sid));
        

        operationPromise = operationVersion.setPromiseCallback(operationPromise,callback);
        return operationPromise;



    }

    update(params?: any, callback?: any): Promise<AWSInstance> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.testString !== undefined) data['TestString'] = params.testString;
        if (params.testBoolean !== undefined) data['TestBoolean'] = serialize.bool(params.testBoolean);

        const headers: any = {};
        headers['Content-Type'] = 'application/x-www-form-urlencoded'

        let operationVersion = this._version,
            operationPromise = operationVersion.update({ uri: this._uri, method: 'POST', params: data, headers });
        
        operationPromise = operationPromise.then(payload => new AWSInstance(operationVersion, payload, this._solution.sid));
        

        operationPromise = operationVersion.setPromiseCallback(operationPromise,callback);
        return operationPromise;



    }

    /**
     * Provide a user-friendly representation
     *
     * @returns Object
     */
    toJSON() {
        return this._solution;
    }

    [inspect.custom](_depth: any, options: InspectOptions) {
        return inspect(this.toJSON(), options);
    }
}

export interface AWSSolution {
    sid?: string;
}

export class AWSPage extends Page<V2010, AWSPayload, AWSResource, AWSInstance> {
/**
* Initialize the AWSPage
*
* @param version - Version of the resource
* @param response - Response from the API
* @param solution - Path solution
*/
    constructor(version: V2010, response: Response<string>, solution: AWSSolution) {
        super(version, response, solution);
    }

    /**
    * Build an instance of AWSInstance
    *
    * @param payload - Payload response from the API
    */
    getInstance(payload: AWSPayload): AWSInstance {
        return new AWSInstance(
            this._version,
            payload,
            this._solution.sid,
        );
    }

    [inspect.custom](depth: any, options: InspectOptions) {
        return inspect(this.toJSON(), options);
    }
}

interface AWSPayload extends AWSResource, Page.TwilioResponsePayload {
}

interface AWSResource {
    account_sid?: string | null;
    sid?: string | null;
    test_string?: string | null;
    test_integer?: number | null;
    test_object?: object | null;
    test_date_time?: string | null;
    test_number?: number | null;
    price_unit?: string | null;
    test_number_float?: number | null;
    test_enum?: object;
    test_array_of_integers?: Array<number>;
    test_array_of_array_of_integers?: Array<Array<number>>;
    test_array_of_objects?: Array<object> | null;
    test_array_of_enum?: Array<object> | null;
}

export class AWSInstance {
    protected _solution: any;
    protected _context?: AWSContext;

    constructor(protected _version: V2010, payload: AWSPayload, sid?: string) {
        this.accountSid = payload.account_sid;
        this.sid = payload.sid;
        this.testString = payload.test_string;
        this.testInteger = deserialize.integer(payload.test_integer);
        this.testObject = payload.test_object;
        this.testDateTime = deserialize.rfc2822DateTime(payload.test_date_time);
        this.testNumber = payload.test_number;
        this.priceUnit = payload.price_unit;
        this.testNumberFloat = payload.test_number_float;
        this.testEnum = payload.test_enum;
        this.testArrayOfIntegers = payload.test_array_of_integers;
        this.testArrayOfArrayOfIntegers = payload.test_array_of_array_of_integers;
        this.testArrayOfObjects = payload.test_array_of_objects;
        this.testArrayOfEnum = payload.test_array_of_enum;

        this._solution = { sid: sid || this.sid };
    }

    accountSid?: string | null;
    sid?: string | null;
    testString?: string | null;
    testInteger?: number | null;
    testObject?: object | null;
    testDateTime?: string | null;
    testNumber?: number | null;
    priceUnit?: string | null;
    testNumberFloat?: number | null;
    testEnum?: object;
    testArrayOfIntegers?: Array<number>;
    testArrayOfArrayOfIntegers?: Array<Array<number>>;
    testArrayOfObjects?: Array<object> | null;
    /**
     * Permissions authorized to the app
     */
    testArrayOfEnum?: Array<object> | null;

    private get _proxy(): AWSContext {
        this._context = this._context || new AWSContextImpl(this._version, this._solution.sid);
        return this._context;
    }

    /**
     * Remove a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<boolean>
    {
        return this._proxy.remove(callback);
    }

    /**
     * Fetch a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    fetch(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>
    {
        return this._proxy.fetch(callback);
    }

    /**
     * Update a AWSInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    update(callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>;
    /**
     * Update a AWSInstance
     *
     * @param { AWSContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWSInstance
     */
    update(params: AWSContextUpdateOptions, callback?: (error: Error | null, item?: AWSInstance) => any): Promise<AWSInstance>;
    update(params?: any, callback?: any): Promise<AWSInstance>
    {
        return this._proxy.update(params, callback);
    }

    /**
     * Provide a user-friendly representation
     *
     * @returns Object
     */
    toJSON() {
        return {
            accountSid: this.accountSid, 
            sid: this.sid, 
            testString: this.testString, 
            testInteger: this.testInteger, 
            testObject: this.testObject, 
            testDateTime: this.testDateTime, 
            testNumber: this.testNumber, 
            priceUnit: this.priceUnit, 
            testNumberFloat: this.testNumberFloat, 
            testEnum: this.testEnum, 
            testArrayOfIntegers: this.testArrayOfIntegers, 
            testArrayOfArrayOfIntegers: this.testArrayOfArrayOfIntegers, 
            testArrayOfObjects: this.testArrayOfObjects, 
            testArrayOfEnum: this.testArrayOfEnum
        }
    }

    [inspect.custom](_depth: any, options: InspectOptions) {
        return inspect(this.toJSON(), options);
    }
}


export interface AWSListInstance {
    (sid: string): AWSContext;
    get(sid: string): AWSContext;



    /**
    * Streams AWSInstance records from the API.
    *
    * This operation lazily loads records as efficiently as possible until the limit
    * is reached.
    *
    * The results are passed into the callback function, so this operation is memory
    * efficient.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { function } [callback] - Function to process each record
    */
    each(callback?: (item: AWSInstance, done: (err?: Error) => void) => void): void;
    /**
    * Streams AWSInstance records from the API.
    *
    * This operation lazily loads records as efficiently as possible until the limit
    * is reached.
    *
    * The results are passed into the callback function, so this operation is memory
    * efficient.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { AWSListInstanceEachOptions } [params] - Options for request
    * @param { function } [callback] - Function to process each record
    */
    each(params?: AWSListInstanceEachOptions, callback?: (item: AWSInstance, done: (err?: Error) => void) => void): void;
    each(params?: any, callback?: any): void;
    /**
    * Retrieve a single target page of AWSInstance records from the API.
    *
    * The request is executed immediately.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { function } [callback] - Callback to handle list of records
    */
    getPage(callback?: (error: Error | null, items: AWSPage) => any): Promise<AWSPage>;
    /**
    * Retrieve a single target page of AWSInstance records from the API.
    *
    * The request is executed immediately.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { string } [targetUrl] - API-generated URL for the requested results page
    * @param { function } [callback] - Callback to handle list of records
    */
    getPage(targetUrl?: string, callback?: (error: Error | null, items: AWSPage) => any): Promise<AWSPage>;
    getPage(params?: any, callback?: any): Promise<AWSPage>;
    /**
    * Lists AWSInstance records from the API as a list.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { function } [callback] - Callback to handle list of records
    */
    list(callback?: (error: Error | null, items: AWSInstance[]) => any): Promise<AWSInstance[]>;
    /**
    * Lists AWSInstance records from the API as a list.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { AWSListInstanceOptions } [params] - Options for request
    * @param { function } [callback] - Callback to handle list of records
    */
    list(params?: AWSListInstanceOptions, callback?: (error: Error | null, items: AWSInstance[]) => any): Promise<AWSInstance[]>;
    list(params?: any, callback?: any): Promise<AWSInstance[]>;
    /**
    * Retrieve a single page of AWSInstance records from the API.
    *
    * The request is executed immediately.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { function } [callback] - Callback to handle list of records
    */
    page(callback?: (error: Error | null, items: AWSPage) => any): Promise<AWSPage>;
    /**
    * Retrieve a single page of AWSInstance records from the API.
    *
    * The request is executed immediately.
    *
    * If a function is passed as the first argument, it will be used as the callback
    * function.
    *
    * @param { AWSListInstancePageOptions } [params] - Options for request
    * @param { function } [callback] - Callback to handle list of records
    */
    page(params: AWSListInstancePageOptions, callback?: (error: Error | null, items: AWSPage) => any): Promise<AWSPage>;
    page(params?: any, callback?: any): Promise<AWSPage>;

    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface AWSListInstanceImpl extends AWSListInstance {}
class AWSListInstanceImpl implements AWSListInstance {
    _version?: V2010;
    _solution?: AWSSolution;
    _uri?: string;

}

export function AWSListInstance(version: V2010): AWSListInstance {
    const instance = ((sid) => instance.get(sid)) as AWSListInstanceImpl;

    instance.get = function get(sid): AWSContext {
        return new AWSContextImpl(version, sid);
    }

    instance._version = version;
    instance._solution = {  };
    instance._uri = `/v1/Credentials/AWS`;

    instance.page = function page(params?: any, callback?: any): Promise<AWSPage> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.pageSize !== undefined) data['PageSize'] = params.pageSize;
        if (params.page !== undefined) data['Page'] = params.pageNumber;
        if (params.pageToken !== undefined) data['PageToken'] = params.pageToken;

        const headers: any = {};

        let operationVersion = version,
            operationPromise = operationVersion.page({ uri: this._uri, method: 'GET', params: data, headers });
        
        operationPromise = operationPromise.then(payload => new AWSPage(operationVersion, payload, this._solution));

        operationPromise = operationVersion.setPromiseCallback(operationPromise,callback);
        return operationPromise;

    }
    instance.each = instance._version.each;
    instance.list = instance._version.list;

    instance.getPage = function getPage(targetUrl?: any, callback?: any): Promise<AWSPage> {
        let operationPromise = this._version._domain.twilio.request({method: 'GET', uri: targetUrl});

        operationPromise = operationPromise.then(payload => new AWSPage(this._version, payload, this._solution));
        operationPromise = version.setPromiseCallback(operationPromise,callback);
        return operationPromise;
    }




    instance.toJSON = function toJSON() {
        return this._solution;
    }

    instance[inspect.custom] = function inspectImpl(_depth: any, options: InspectOptions) {
        return inspect(this.toJSON(), options);
    }

    return instance;
}

