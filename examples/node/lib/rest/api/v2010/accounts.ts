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

import { inspect } from 'util';
import V2010 from '../V2010';


/**
 * Options to pass to create a AccountInstance
 *
 * @property { 'true' | 'false' } [xTwilioWebhookEnabled] 
 * @property { string } [recordingStatusCallback] 
 * @property { Array<string> } [recordingStatusCallbackEvent] 
 */
export interface AccountInstanceCreateOptions {
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
export interface AccountInstancePageOptions {
    dateCreated?: Date;
    dateTest?: string;
    dateCreated2?: Date;
    dateCreated3?: Date;
    pageSize?: number;
}
/**
 * Options to pass to create a AccountInstance
 *
 * @property { string } status 
 * @property { string } [pauseBehavior] 
 */
export interface AccountInstanceCreateOptions {
    status: string;
    pauseBehavior?: string;
}

export class AccountListInstance {
    protected _solution: any;
    protected _uri: string;


    constructor(protected _version: V2010, sid: string) {
        this._solution = { sid };
        this._uri = `/2010-04-01/Accounts/${sid}.json`;
    }

    /**
     * Create a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async create(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    /**
     * Create a AccountInstance
     *
     * @param { AccountInstanceCreateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async create(params: AccountInstanceCreateOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    public async create(params?: any, callback?: any): Promise<AccountInstance> {

        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};

        if (params.recordingStatusCallback !== undefined) data['RecordingStatusCallback'] = params.recordingStatusCallback;
        if (params.recordingStatusCallbackEvent !== undefined) data['RecordingStatusCallbackEvent'] = params.recordingStatusCallbackEvent;

        const headers: any = {
            'Content-Type': 'application/x-www-form-urlencoded'
        };

        if (params.xTwilioWebhookEnabled !== undefined) headers['X-Twilio-Webhook-Enabled'] = params.xTwilioWebhookEnabled;

        const operationPromise = this._version.create({ uri: this._uri, method: 'POST', data, headers });

        let instancePromise = operationPromise.then(payload => new AccountInstance(this._version, payload, this._solution.sid));

        if (typeof callback === 'function') {
            instancePromise = instancePromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return instancePromise;
    }

    /**
     *  a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async (callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance> { 


        const operationPromise = this._version.({ uri: this._uri, method: 'DELETE' });

        let instancePromise = operationPromise.then(payload => new AccountInstance(this._version, payload, this._solution.sid));

        if (typeof callback === 'function') {
            instancePromise = instancePromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return instancePromise;
    }

    /**
     * Page a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async page(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance> { 


        const operationPromise = this._version.page({ uri: this._uri, method: 'GET' });

        let instancePromise = operationPromise.then(payload => new AccountInstance(this._version, payload, this._solution.sid));

        if (typeof callback === 'function') {
            instancePromise = instancePromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return instancePromise;
    }

    /**
     * Page a AccountInstance
     *
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async page(callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    /**
     * Page a AccountInstance
     *
     * @param { AccountInstancePageOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async page(params: AccountInstancePageOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    public async page(params?: any, callback?: any): Promise<AccountInstance> {

        if (typeof params === 'function') {
            callback = params;
            params = {};
        } else {
            params = params || {};
        }

        const data: any = {};


        const headers: any = {
            'Content-Type': 
        };


        const operationPromise = this._version.page({ uri: this._uri, method: 'GET', data, headers });

        let instancePromise = operationPromise.then(payload => new AccountInstance(this._version, payload, this._solution.sid));

        if (typeof callback === 'function') {
            instancePromise = instancePromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return instancePromise;
    }

    /**
     * Create a AccountInstance
     *
     * @param { AccountInstanceCreateOptions } params - Parameter for request
     * @param { function } [callback] - Callback to handle processed record
     *
     * @returns { Promise } Resolves to processed AccountInstance
     */
    public async create(params: AccountInstanceCreateOptions, callback?: (error: Error | null, item?: AccountInstance) => any): Promise<AccountInstance>;
    public async create(params: any, callback?: any): Promise<AccountInstance> {

        if (params === null || params === undefined) {
            throw new Error('Required parameter "params" missing.');
        }

        if (params.status === null || params.status === undefined) {
            throw new Error('Required parameter "params.status" missing.');
        }

        const data: any = {};

        if (params.pauseBehavior !== undefined) data['PauseBehavior'] = params.pauseBehavior;
        data['Status'] = params.status;

        const headers: any = {
            'Content-Type': 'application/x-www-form-urlencoded'
        };


        const operationPromise = this._version.create({ uri: this._uri, method: 'POST', data, headers });

        let instancePromise = operationPromise.then(payload => new AccountInstance(this._version, payload, this._solution.sid));

        if (typeof callback === 'function') {
            instancePromise = instancePromise
                .then(value => callback(null, value))
                .catch(error => callback(error));
        }

        return instancePromise;
    }

    /**
     * Provide a user-friendly representation
     *
     * @returns Object
     */
    toJSON() {
        return this._solution;
    }

    [inspect.custom](depth, options) {
        return inspect(this.toJSON(), options);
    }
}

