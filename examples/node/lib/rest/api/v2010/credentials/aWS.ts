/**
 * Twilio - Accounts
 * This is the public Twilio REST API.
 *
 * The version of the OpenAPI document: 1.11.0
 * Contact: support@twilio.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { inspect, InspectOptions } from 'util';
import Page from '../../../../base/Page';
import V2010 from '../../V2010';


/**
 * Options to pass to create a AWInstance
 *
 * @property { string } testString 
 * @property { boolean } [testBoolean] 
 * @property { number } [testInteger] 
 * @property { number } [testNumber] 
 * @property { number } [testNumberFloat] 
 * @property { number } [testNumberDouble] 
 * @property { number } [testNumberInt32] 
 * @property { number } [testNumberInt64] 
 * @property { object } [testObject] 
 * @property { Date } [testDateTime] 
 * @property { string } [testDate] 
 * @property { string } [testEnum] 
 * @property { Array<object> } [testObjectArray] 
 * @property { any } [testAnyType] 
 */
export interface AWListInstanceCreateOptions {
    testString: string;
    testBoolean?: boolean;
    testInteger?: number;
    testNumber?: number;
    testNumberFloat?: number;
    testNumberDouble?: number;
    testNumberInt32?: number;
    testNumberInt64?: number;
    testObject?: object;
    testDateTime?: Date;
    testDate?: string;
    testEnum?: string;
    testObjectArray?: Array<object>;
    testAnyType?: any;
}
/**
 * Options to pass to page a AWInstance
 *
 * @property { number } [pageSize] 
 */
export interface AWListInstancePageOptions {
    pageSize?: number;
}

/**
 * Options to pass to update a AWInstance
 *
 * @property { string } [testString] 
 * @property { Array<string> } [permissions] A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;.
 */
export interface AWContextUpdateOptions {
    testString?: string;
    permissions?: Array<string>;
}

export interface AWListInstance {
    (sid: string): AWContext;
    get(sid: string): AWContext;


    /**
     * Create a AWInstance
     *
     * @param { AWListInstanceCreateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    create(params: AWListInstanceCreateOptions, callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    create(params: any, callback?: any): Promise<AWInstance>
;
    /**
     * Page a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    page(callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    /**
     * Page a AWInstance
     *
     * @param { AWListInstancePageOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    page(params: AWListInstancePageOptions, callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    page(params?: any, callback?: any): Promise<AWInstance>
;
    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}


interface AWListInstanceImpl extends AWListInstance {}
class AWListInstanceImpl implements AWListInstance {
    _version?: V2010;
    _solution?: any;
    _uri?: string;

}

export function AWListInstance(version: V2010): AWListInstance {
    const instance = ((sid) => instance.get(sid)) as AWListInstanceImpl;

    instance.get = function get(sid): AWContext {
        return new AWContextImpl(version, sid);
    }

    instance._version = version;
    instance._solution = {  };
    instance._uri = `/v1/Credentials/AWS`;

    instance.create = function create(params: any, callback?: any): Promise<AWInstance> {
        if (params === null || params === undefined) {
            throw new Error('Required parameter "params" missing.');
        }

        if (params.testString === null || params.testString === undefined) {
            throw new Error('Required parameter "params.testString" missing.');
        }

        const data: any = {};

        data['TestString'] = params.testString;
        if (params.testBoolean !== undefined) data['TestBoolean'] = params.testBoolean;
        if (params.testInteger !== undefined) data['TestInteger'] = params.testInteger;
        if (params.testNumber !== undefined) data['TestNumber'] = params.testNumber;
        if (params.testNumberFloat !== undefined) data['TestNumberFloat'] = params.testNumberFloat;
        if (params.testNumberDouble !== undefined) data['TestNumberDouble'] = params.testNumberDouble;
        if (params.testNumberInt32 !== undefined) data['TestNumberInt32'] = params.testNumberInt32;
        if (params.testNumberInt64 !== undefined) data['TestNumberInt64'] = params.testNumberInt64;
        if (params.testObject !== undefined) data['TestObject'] = params.testObject;
        if (params.testDateTime !== undefined) data['TestDateTime'] = params.testDateTime;
        if (params.testDate !== undefined) data['TestDate'] = params.testDate;
        if (params.testEnum !== undefined) data['TestEnum'] = params.testEnum;
        if (params.testObjectArray !== undefined) data['TestObjectArray'] = params.testObjectArray;
        if (params.testAnyType !== undefined) data['TestAnyType'] = params.testAnyType;

        const headers: any = {};
        headers['Content-Type'] = 'application/x-www-form-urlencoded'


        let operationVersion = version,
            operationPromise = operationVersion.create({ uri: this._uri, method: 'POST', data, headers });

        operationPromise = operationPromise.then(payload => new AWInstance(operationVersion, payload));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return operationPromise;

    }

    instance.page = function page(params?: any, callback?: any): Promise<AWInstance> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.pageSize !== undefined) data['PageSize'] = params.pageSize;

        const headers: any = {};


        let operationVersion = version,
            operationPromise = operationVersion.page({ uri: this._uri, method: 'GET', data, headers });

        operationPromise = operationPromise.then(payload => new AWInstance(operationVersion, payload));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

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


export interface AWContext {


    /**
     * Remove a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AWInstance) => any): Promise<boolean>
;
    /**
     * Fetch a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    fetch(callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>
;
    /**
     * Update a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    update(callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    /**
     * Update a AWInstance
     *
     * @param { AWContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    update(params: AWContextUpdateOptions, callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    update(params?: any, callback?: any): Promise<AWInstance>
;
    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class AWContextImpl implements AWContext {
    protected _solution: any;
    protected _uri: string;


    constructor(protected _version: V2010, sid: string) {
        this._solution = { sid };
        this._uri = `/v1/Credentials/AWS/${sid}`;
    }

    remove(callback?: any): Promise<boolean> {

        let operationVersion = this._version,
            operationPromise = operationVersion.remove({ uri: this._uri, method: 'DELETE' });


        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return operationPromise;

    }

    fetch(callback?: any): Promise<AWInstance> {

        let operationVersion = this._version,
            operationPromise = operationVersion.fetch({ uri: this._uri, method: 'GET' });

        operationPromise = operationPromise.then(payload => new AWInstance(operationVersion, payload, this._solution.sid));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return operationPromise;

    }

    update(params?: any, callback?: any): Promise<AWInstance> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.testString !== undefined) data['TestString'] = params.testString;
        if (params.permissions !== undefined) data['Permissions'] = params.permissions;

        const headers: any = {};
        headers['Content-Type'] = 'application/x-www-form-urlencoded'


        let operationVersion = this._version,
            operationPromise = operationVersion.update({ uri: this._uri, method: 'POST', data, headers });

        operationPromise = operationPromise.then(payload => new AWInstance(operationVersion, payload, this._solution.sid));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

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

export type AWTestEnum = 'DialVerb'|'Trunking';

interface AWPayload extends AWResource, Page.TwilioResponsePayload {
}

interface AWResource {
    account_sid?: string | null;
    sid?: string | null;
    test_string?: string | null;
    test_integer?: number | null;
    test_object?: TestResponseObjectTestObject | null;
    test_date_time?: string | null;
    test_number?: number | null;
    price_unit?: string | null;
    test_number_float?: number | null;
    test_enum?: AWTestEnum;
    test_array_of_integers?: Array<number>;
    test_array_of_array_of_integers?: Array<Array<number>>;
    test_array_of_objects?: Array<TestResponseObjectTestArrayOfObjects> | null;
}

export class AWInstance {
    protected _solution: any;
    protected _context?: AWContext;

    constructor(protected _version: V2010, payload: AWPayload, sid?: string) {
        this.accountSid = payload.account_sid;
        this.sid = payload.sid;
        this.testString = payload.test_string;
        this.testInteger = payload.test_integer;
        this.testObject = payload.test_object;
        this.testDateTime = payload.test_date_time;
        this.testNumber = payload.test_number;
        this.priceUnit = payload.price_unit;
        this.testNumberFloat = payload.test_number_float;
        this.testEnum = payload.test_enum;
        this.testArrayOfIntegers = payload.test_array_of_integers;
        this.testArrayOfArrayOfIntegers = payload.test_array_of_array_of_integers;
        this.testArrayOfObjects = payload.test_array_of_objects;

        this._solution = { sid: sid || this.sid };
    }

    private get _proxy(): AWContext {
        this._context = this._context || new AWContextImpl(this._version, this._solution.sid);
        return this._context;
    }

    accountSid?: string | null;
    sid?: string | null;
    testString?: string | null;
    testInteger?: number | null;
    testObject?: TestResponseObjectTestObject | null;
    testDateTime?: string | null;
    testNumber?: number | null;
    priceUnit?: string | null;
    testNumberFloat?: number | null;
    testEnum?: AWTestEnum;
    testArrayOfIntegers?: Array<number>;
    testArrayOfArrayOfIntegers?: Array<Array<number>>;
    testArrayOfObjects?: Array<TestResponseObjectTestArrayOfObjects> | null;

    /**
     * Remove a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AWInstance) => any): Promise<boolean>
 {
        return this._proxy.remove(callback);
    }

    /**
     * Fetch a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    fetch(callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>
 {
        return this._proxy.fetch(callback);
    }

    /**
     * Update a AWInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    update(callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    /**
     * Update a AWInstance
     *
     * @param { AWContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AWInstance
     */
    update(params: AWContextUpdateOptions, callback?: (error: Error | null, item?: AWInstance) => any): Promise<AWInstance>;
    update(params?: any, callback?: any): Promise<AWInstance>
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
            testArrayOfObjects: this.testArrayOfObjects
        }
    }

    [inspect.custom](_depth: any, options: InspectOptions) {
        return inspect(this.toJSON(), options);
    }
}

