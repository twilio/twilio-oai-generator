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

import { inspect, InspectOptions } from "util";
import Page from "../../../base/Page";
import Response from "../../../http/response";
import V2010 from "../V2010";
const deserialize = require("../../../base/deserialize");
const serialize = require("../../../base/serialize");
import { CallListInstance } from "./account/call";
import { PhoneNumberCapabilities } from "../../../interfaces";

export class TestResponseObjectTestArrayOfObjects {
  "count"?: number;
  "description"?: string;
}

export class TestResponseObjectTestObject {
  "fax"?: boolean;
  "mms"?: boolean;
  "sms"?: boolean;
  "voice"?: boolean;
}

type TestStatus =
  | "in-progress"
  | "paused"
  | "stopped"
  | "processing"
  | "completed"
  | "absent";

/**
 * Options to pass to update a AccountInstance
 *
 * @property { TestStatus } status
 * @property { string } [pauseBehavior]
 */
export interface AccountContextUpdateOptions {
  status: TestStatus;
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
  xTwilioWebhookEnabled?: "true" | "false";
  recordingStatusCallback?: string;
  recordingStatusCallbackEvent?: Array<string>;
}
/**
 * Options to pass to each
 *
 * @property { Date } [dateCreated]
 * @property { Date } [date.test]
 * @property { Date } [dateCreatedBefore]
 * @property { Date } [dateCreatedAfter]
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
export interface AccountListInstanceEachOptions {
  dateCreated?: Date;
  "date.test"?: Date;
  dateCreatedBefore?: Date;
  dateCreatedAfter?: Date;
  pageSize?: number;
  callback?: (item: AccountInstance, done: (err?: Error) => void) => void;
  done?: Function;
  limit?: number;
}

/**
 * Options to pass to list
 *
 * @property { Date } [dateCreated]
 * @property { Date } [date.test]
 * @property { Date } [dateCreatedBefore]
 * @property { Date } [dateCreatedAfter]
 * @property { number } [pageSize]
 * @property { number } [limit] -
 *                         Upper limit for the number of records to return.
 *                         list() guarantees never to return more than limit.
 *                         Default is no limit
 */
export interface AccountListInstanceOptions {
  dateCreated?: Date;
  "date.test"?: Date;
  dateCreatedBefore?: Date;
  dateCreatedAfter?: Date;
  pageSize?: number;
  limit?: number;
}

/**
 * Options to pass to page
 *
 * @property { Date } [dateCreated]
 * @property { Date } [date.test]
 * @property { Date } [dateCreatedBefore]
 * @property { Date } [dateCreatedAfter]
 * @property { number } [pageSize]
 * @property { number } [pageNumber] - Page Number, this value is simply for client state
 * @property { string } [pageToken] - PageToken provided by the API
 */
export interface AccountListInstancePageOptions {
  dateCreated?: Date;
  "date.test"?: Date;
  dateCreatedBefore?: Date;
  dateCreatedAfter?: Date;
  pageSize?: number;
  pageNumber?: number;
  pageToken?: string;
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
  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean>;

  /**
   * Fetch a AccountInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AccountInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance>;

  /**
   * Update a AccountInstance
   *
   * @param { AccountContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AccountInstance
   */
  update(
    params: AccountContextUpdateOptions,
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance>;
  update(params: any, callback?: any): Promise<AccountInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface AccountContextSolution {
  sid?: string;
}

export class AccountContextImpl implements AccountContext {
  protected _solution: AccountContextSolution;
  protected _uri: string;

  protected _calls?: CallListInstance;

  constructor(protected _version: V2010, sid: string) {
    this._solution = { sid };
    this._uri = `/Accounts/${sid}.json`;
  }

  get calls(): CallListInstance {
    this._calls =
      this._calls || CallListInstance(this._version, this._solution.sid);
    return this._calls;
  }

  remove(callback?: any): Promise<boolean> {
    let operationVersion = this._version,
      operationPromise = operationVersion.remove({
        uri: this._uri,
        method: "delete",
      });

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  }

  fetch(callback?: any): Promise<AccountInstance> {
    let operationVersion = this._version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new AccountInstance(operationVersion, payload, this._solution.sid)
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  }

  update(params: any, callback?: any): Promise<AccountInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (params["status"] === null || params["status"] === undefined) {
      throw new Error("Required parameter \"params['status']\" missing.");
    }

    let data: any = {};

    if (params["pauseBehavior"] !== undefined)
      data["PauseBehavior"] = params["pauseBehavior"];

    data["Status"] = params["status"];

    const headers: any = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";

    let operationVersion = this._version,
      operationPromise = operationVersion.update({
        uri: this._uri,
        method: "post",
        data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new AccountInstance(operationVersion, payload, this._solution.sid)
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
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

interface AccountPayload extends AccountResource, Page.TwilioResponsePayload {}

interface AccountResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
  test_object?: PhoneNumberCapabilities | null;
  test_date_time?: string | null;
  test_number?: number | null;
  price_unit?: string | null;
  test_number_float?: number | null;
  test_enum?: TestStatus;
  a2p_profile_bundle_sid?: string | null;
  test_array_of_integers?: Array<number>;
  test_array_of_array_of_integers?: Array<Array<number>>;
  test_array_of_objects?: Array<TestResponseObjectTestArrayOfObjects> | null;
  test_array_of_enum?: Array<TestStatus> | null;
}

export class AccountInstance {
  protected _solution: AccountContextSolution;
  protected _context?: AccountContext;

  constructor(
    protected _version: V2010,
    payload: AccountPayload,
    sid?: string
  ) {
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
    this.a2pProfileBundleSid = payload.a2p_profile_bundle_sid;
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
  testObject?: PhoneNumberCapabilities | null;
  testDateTime?: string | null;
  testNumber?: number | null;
  priceUnit?: string | null;
  testNumberFloat?: number | null;
  testEnum?: TestStatus;
  /**
   * A2P Messaging Profile Bundle BundleSid
   */
  a2pProfileBundleSid?: string | null;
  testArrayOfIntegers?: Array<number>;
  testArrayOfArrayOfIntegers?: Array<Array<number>>;
  testArrayOfObjects?: Array<TestResponseObjectTestArrayOfObjects> | null;
  /**
   * Permissions authorized to the app
   */
  testArrayOfEnum?: Array<TestStatus> | null;

  private get _proxy(): AccountContext {
    this._context =
      this._context ||
      new AccountContextImpl(this._version, this._solution.sid);
    return this._context;
  }

  /**
   * Remove a AccountInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean> {
    return this._proxy.remove(callback);
  }

  /**
   * Fetch a AccountInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AccountInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance> {
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
  update(
    params: AccountContextUpdateOptions,
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance>;
  update(params: any, callback?: any): Promise<AccountInstance> {
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
      a2pProfileBundleSid: this.a2pProfileBundleSid,
      testArrayOfIntegers: this.testArrayOfIntegers,
      testArrayOfArrayOfIntegers: this.testArrayOfArrayOfIntegers,
      testArrayOfObjects: this.testArrayOfObjects,
      testArrayOfEnum: this.testArrayOfEnum,
    };
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
  create(
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance>;
  /**
   * Create a AccountInstance
   *
   * @param { AccountListInstanceCreateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AccountInstance
   */
  create(
    params: AccountListInstanceCreateOptions,
    callback?: (error: Error | null, item?: AccountInstance) => any
  ): Promise<AccountInstance>;
  create(params?: any, callback?: any): Promise<AccountInstance>;

  /**
   * Streams AccountInstance records from the API.
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
  each(
    callback?: (item: AccountInstance, done: (err?: Error) => void) => void
  ): void;
  /**
   * Streams AccountInstance records from the API.
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
   * @param { AccountListInstanceEachOptions } [params] - Options for request
   * @param { function } [callback] - Function to process each record
   */
  each(
    params?: AccountListInstanceEachOptions,
    callback?: (item: AccountInstance, done: (err?: Error) => void) => void
  ): void;
  each(params?: any, callback?: any): void;
  /**
   * Retrieve a single target page of AccountInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  getPage(
    callback?: (error: Error | null, items: AccountPage) => any
  ): Promise<AccountPage>;
  /**
   * Retrieve a single target page of AccountInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { string } [targetUrl] - API-generated URL for the requested results page
   * @param { function } [callback] - Callback to handle list of records
   */
  getPage(
    targetUrl?: string,
    callback?: (error: Error | null, items: AccountPage) => any
  ): Promise<AccountPage>;
  getPage(params?: any, callback?: any): Promise<AccountPage>;
  /**
   * Lists AccountInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    callback?: (error: Error | null, items: AccountInstance[]) => any
  ): Promise<AccountInstance[]>;
  /**
   * Lists AccountInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AccountListInstanceOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    params?: AccountListInstanceOptions,
    callback?: (error: Error | null, items: AccountInstance[]) => any
  ): Promise<AccountInstance[]>;
  list(params?: any, callback?: any): Promise<AccountInstance[]>;
  /**
   * Retrieve a single page of AccountInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    callback?: (error: Error | null, items: AccountPage) => any
  ): Promise<AccountPage>;
  /**
   * Retrieve a single page of AccountInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AccountListInstancePageOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    params: AccountListInstancePageOptions,
    callback?: (error: Error | null, items: AccountPage) => any
  ): Promise<AccountPage>;
  page(params?: any, callback?: any): Promise<AccountPage>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface AccountSolution {}

interface AccountListInstanceImpl extends AccountListInstance {}
class AccountListInstanceImpl implements AccountListInstance {
  _version?: V2010;
  _solution?: AccountSolution;
  _uri?: string;
}

export function AccountListInstance(version: V2010): AccountListInstance {
  const instance = ((sid) => instance.get(sid)) as AccountListInstanceImpl;

  instance.get = function get(sid): AccountContext {
    return new AccountContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Accounts.json`;

  instance.create = function create(
    params?: any,
    callback?: any
  ): Promise<AccountInstance> {
    if (typeof params === "function") {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["recordingStatusCallback"] !== undefined)
      data["RecordingStatusCallback"] = params["recordingStatusCallback"];
    if (params["recordingStatusCallbackEvent"] !== undefined)
      data["RecordingStatusCallbackEvent"] = serialize.map(
        params["recordingStatusCallbackEvent"],
        (e) => e
      );

    const headers: any = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";
    if (params["xTwilioWebhookEnabled"] !== undefined)
      headers["X-Twilio-Webhook-Enabled"] = params["xTwilioWebhookEnabled"];

    let operationVersion = version,
      operationPromise = operationVersion.create({
        uri: this._uri,
        method: "post",
        data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) => new AccountInstance(operationVersion, payload)
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  };

  instance.page = function page(
    params?: any,
    callback?: any
  ): Promise<AccountPage> {
    if (typeof params === "function") {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["dateCreated"] !== undefined)
      data["DateCreated"] = serialize.iso8601DateTime(params["dateCreated"]);
    if (params["date.test"] !== undefined)
      data["Date.Test"] = serialize.iso8601Date(params["date.test"]);
    if (params["dateCreatedBefore"] !== undefined)
      data["DateCreated<"] = serialize.iso8601DateTime(
        params["dateCreatedBefore"]
      );
    if (params["dateCreatedAfter"] !== undefined)
      data["DateCreated>"] = serialize.iso8601DateTime(
        params["dateCreatedAfter"]
      );
    if (params["pageSize"] !== undefined) data["PageSize"] = params["pageSize"];

    if (params.page !== undefined) data["Page"] = params.pageNumber;
    if (params.pageToken !== undefined) data["PageToken"] = params.pageToken;

    const headers: any = {};

    let operationVersion = version,
      operationPromise = operationVersion.page({
        uri: this._uri,
        method: "get",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) => new AccountPage(operationVersion, payload, this._solution)
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  };
  instance.each = instance._version.each;
  instance.list = instance._version.list;

  instance.getPage = function getPage(
    targetUrl?: any,
    callback?: any
  ): Promise<AccountPage> {
    let operationPromise = this._version._domain.twilio.request({
      method: "get",
      uri: targetUrl,
    });

    operationPromise = operationPromise.then(
      (payload) => new AccountPage(this._version, payload, this._solution)
    );
    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  };

  instance.toJSON = function toJSON() {
    return this._solution;
  };

  instance[inspect.custom] = function inspectImpl(
    _depth: any,
    options: InspectOptions
  ) {
    return inspect(this.toJSON(), options);
  };

  return instance;
}

export class AccountPage extends Page<
  V2010,
  AccountPayload,
  AccountResource,
  AccountInstance
> {
  /**
   * Initialize the AccountPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(
    version: V2010,
    response: Response<string>,
    solution: AccountSolution
  ) {
    super(version, response, solution);
  }

  /**
   * Build an instance of AccountInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: AccountPayload): AccountInstance {
    return new AccountInstance(this._version, payload);
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
