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
import Page from '../../../base/Page';
import V2010 from '../V2010';
import { CallListInstance } from './Accounts/Calls';


/**
 * Options to pass to update a AccountInstance
 *
 * @property { string } status 
 * @property { string } [pauseBehavior] 
 */
export interface AccountContextUpdateOptions {
    status: string;
    pauseBehavior?: string;
}

/**
 * Options to pass to create a AccountInstance
 *
 * @property { 'true' | 'false' } [xTwilioWebhookEnabled] 
 * @property { string } [recordingStatusCallback] 
 * @property { Array<string> } [recordingStatusCallbackEvent] 
 */
export interface AccountListInstanceCreateOptions {
    xTwilioWebhookEnabled?: 'true' | 'false';
    recordingStatusCallback?: string;
    recordingStatusCallbackEvent?: Array<string>;
}
/**
 * Options to pass to page a AccountInstance
 *
 * @property { Date } [dateCreated] 
 * @property { string } [dateTest] 
 * @property { Date } [dateCreated2] 
 * @property { Date } [dateCreated3] 
 * @property { number } [pageSize] 
 */
export interface AccountListInstancePageOptions {
    dateCreated?: Date;
    dateTest?: string;
    dateCreated2?: Date;
    dateCreated3?: Date;
    pageSize?: number;
}

export interface AccountContext {

    calls: CallListInstance;

    /**
     * Remove a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<boolean>
;
    /**
     * Fetch a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    fetch(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>
;
    /**
     * Update a AccountInstance
     *
     * @param { AccountContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    update(params: AccountContextUpdateOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    update(params: any, callback?: any): Promise<AccountInstance>
;
    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class AccountContextImpl implements AccountContext {
    protected _solution: any;
    protected _uri: string;

    protected _calls?: CallListInstance;

    constructor(protected _version: V2010, sid: string) {
        this._solution = { sid };
        this._uri = `/2010-04-01/Accounts/${sid}.json`;
    }

    get calls(): CallListInstance {
        this._calls = this._calls || CallListInstance(this._version, this._solution.sid);
        return this._calls;
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

    fetch(callback?: any): Promise<AccountInstance> {

        let operationVersion = this._version,
            operationPromise = operationVersion.fetch({ uri: this._uri, method: 'GET' });

        operationPromise = operationPromise.then(payload => new AccountInstance(operationVersion, payload, this._solution.sid));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return operationPromise;

    }

    update(params: any, callback?: any): Promise<AccountInstance> {
        if (params === null || params === undefined) {
            throw new Error('Required parameter "params" missing.');
        }

        if (params.status === null || params.status === undefined) {
            throw new Error('Required parameter "params.status" missing.');
        }

        const data: any = {};

        if (params.pauseBehavior !== undefined) data['PauseBehavior'] = params.pauseBehavior;
        data['Status'] = params.status;

        const headers: any = {};
        headers['Content-Type'] = 'application/x-www-form-urlencoded'


        let operationVersion = this._version,
            operationPromise = operationVersion.update({ uri: this._uri, method: 'POST', data, headers });

        operationPromise = operationPromise.then(payload => new AccountInstance(operationVersion, payload, this._solution.sid));

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

export type AccountTestEnum = 'DialVerb'|'Trunking';

interface AccountPayload extends AccountResource, Page.TwilioResponsePayload {
}

interface AccountResource {
    account_sid?: string | null;
    sid?: string | null;
    test_string?: string | null;
    test_integer?: number | null;
    test_object?: TestResponseObjectTestObject | null;
    test_date_time?: string | null;
    test_number?: number | null;
    price_unit?: string | null;
    test_number_float?: number | null;
    test_enum?: AccountTestEnum;
    test_array_of_integers?: Array<number>;
    test_array_of_array_of_integers?: Array<Array<number>>;
    test_array_of_objects?: Array<TestResponseObjectTestArrayOfObjects> | null;
}

export class AccountInstance {
    protected _solution: any;
    protected _context?: AccountContext;

    constructor(protected _version: V2010, payload: AccountPayload, sid?: string) {
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

    private get _proxy(): AccountContext {
        this._context = this._context || new AccountContextImpl(this._version, this._solution.sid);
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
    testEnum?: AccountTestEnum;
    testArrayOfIntegers?: Array<number>;
    testArrayOfArrayOfIntegers?: Array<Array<number>>;
    testArrayOfObjects?: Array<TestResponseObjectTestArrayOfObjects> | null;

    /**
     * Remove a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed boolean
     */
    remove(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<boolean>
 {
        return this._proxy.remove(callback);
    }

    /**
     * Fetch a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    fetch(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>
 {
        return this._proxy.fetch(callback);
    }

    /**
     * Update a AccountInstance
     *
     * @param { AccountContextUpdateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    update(params: AccountContextUpdateOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    update(params: any, callback?: any): Promise<AccountInstance>
 {
        return this._proxy.update(params, callback);
    }

    /**
     * Access the calls.
     */
    calls(): CallListInstance {
        return this._proxy.calls;
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


export interface AccountListInstance {
    (sid: string): AccountContext;
    get(sid: string): AccountContext;


    /**
     * Create a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    create(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    /**
     * Create a AccountInstance
     *
     * @param { AccountListInstanceCreateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    create(params: AccountListInstanceCreateOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    create(params?: any, callback?: any): Promise<AccountInstance>
;
    /**
     * Page a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    page(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    /**
     * Page a AccountInstance
     *
     * @param { AccountListInstancePageOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    page(params: AccountListInstancePageOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    page(params?: any, callback?: any): Promise<AccountInstance>
;
    /**
     * Provide a user-friendly representation
     */
    toJSON(): any;
    [inspect.custom](_depth: any, options: InspectOptions): any;
}


interface AccountListInstanceImpl extends AccountListInstance {}
class AccountListInstanceImpl implements AccountListInstance {
    _version?: V2010;
    _solution?: any;
    _uri?: string;

}

export function AccountListInstance(version: V2010): AccountListInstance {
    const instance = ((sid) => instance.get(sid)) as AccountListInstanceImpl;

    instance.get = function get(sid): AccountContext {
        return new AccountContextImpl(version, sid);
    }

    instance._version = version;
    instance._solution = {  };
    instance._uri = `/2010-04-01/Accounts.json`;

    instance.create = function create(params?: any, callback?: any): Promise<AccountInstance> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.recordingStatusCallback !== undefined) data['RecordingStatusCallback'] = params.recordingStatusCallback;
        if (params.recordingStatusCallbackEvent !== undefined) data['RecordingStatusCallbackEvent'] = params.recordingStatusCallbackEvent;

        const headers: any = {};
        headers['Content-Type'] = 'application/x-www-form-urlencoded'

        if (params.xTwilioWebhookEnabled !== undefined) headers['X-Twilio-Webhook-Enabled'] = params.xTwilioWebhookEnabled;

        let operationVersion = version,
            operationPromise = operationVersion.create({ uri: this._uri, method: 'POST', data, headers });

        operationPromise = operationPromise.then(payload => new AccountInstance(operationVersion, payload));

        if (typeof callback === 'function') {
            operationPromise = operationPromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return operationPromise;

    }

    instance.page = function page(params?: any, callback?: any): Promise<AccountInstance> {
        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.dateCreated !== undefined) data['DateCreated'] = params.dateCreated;
        if (params.dateTest !== undefined) data['Date.Test'] = params.dateTest;
        if (params.dateCreated2 !== undefined) data['DateCreated<'] = params.dateCreated2;
        if (params.dateCreated3 !== undefined) data['DateCreated>'] = params.dateCreated3;
        if (params.pageSize !== undefined) data['PageSize'] = params.pageSize;

        const headers: any = {};


        let operationVersion = version,
            operationPromise = operationVersion.page({ uri: this._uri, method: 'GET', data, headers });

        operationPromise = operationPromise.then(payload => new AccountInstance(operationVersion, payload));

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

