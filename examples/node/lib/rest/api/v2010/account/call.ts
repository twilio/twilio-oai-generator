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
import Page from "../../../../base/Page";
import Response from "../../../../http/response";
import V2010 from "../../V2010";
const deserialize = require("../../../../base/deserialize");
const serialize = require("../../../../base/serialize");
import { FeedbackCallSummaryListInstance } from "./call/feedbackCallSummary";

/**
 * Options to pass to create a CallInstance
 *
 * @property { string } requiredStringProperty
 * @property { Array<string> } [testArrayOfStrings]
 * @property { Array<string> } [testArrayOfUri]
 */
export interface CallListInstanceCreateOptions {
  requiredStringProperty: string;
  testArrayOfStrings?: Array<string>;
  testArrayOfUri?: Array<string>;
}

/**
 * Options to pass to update a CallInstance
 *
 * @property { string } testUri
 * @property { string } testMethod The HTTP method that we should use to request the &#x60;TestUri&#x60;.
 * @property { string } [requiredStringProperty]
 */
export interface CallContextUpdateOptions {
  testUri: string;
  testMethod: string;
  requiredStringProperty?: string;
}

export interface CallListInstance {
  (testInteger: number): CallContext;
  get(testInteger: number): CallContext;

  feedbackCallSummary: FeedbackCallSummaryListInstance;

  /**
   * Create a CallInstance
   *
   * @param { CallListInstanceCreateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  create(
    params: CallListInstanceCreateOptions,
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;
  create(params: any, callback?: any): Promise<CallInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface CallListInstanceImpl extends CallListInstance {}
class CallListInstanceImpl implements CallListInstance {
  _version?: V2010;
  _solution?: CallSolution;
  _uri?: string;

  _feedbackCallSummary?: FeedbackCallSummaryListInstance;
}

export function CallListInstance(
  version: V2010,
  accountSid: string
): CallListInstance {
  const instance = ((testInteger) =>
    instance.get(testInteger)) as CallListInstanceImpl;

  instance.get = function get(testInteger): CallContext {
    return new CallContextImpl(version, accountSid, testInteger);
  };

  instance._version = version;
  instance._solution = { accountSid };
  instance._uri = `/Accounts/${accountSid}/Calls.json`;

  Object.defineProperty(instance, "feedbackCallSummary", {
    get: function feedbackCallSummary() {
      if (!this._feedbackCallSummary) {
        this._feedbackCallSummary = FeedbackCallSummaryListInstance(
          this._version,
          this._solution.accountSid
        );
      }
      return this._feedbackCallSummary;
    },
  });

  instance.create = function create(
    params: any,
    callback?: any
  ): Promise<CallInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (
      params.requiredStringProperty === null ||
      params.requiredStringProperty === undefined
    ) {
      throw new Error(
        'Required parameter "params.requiredStringProperty" missing.'
      );
    }

    const data: any = {};

    data["RequiredStringProperty"] = params.requiredStringProperty;
    if (params.testArrayOfStrings !== undefined)
      data["TestArrayOfStrings"] = serialize.map(
        params.testArrayOfStrings,
        (e) => e
      );
    if (params.testArrayOfUri !== undefined)
      data["TestArrayOfUri"] = serialize.map(params.testArrayOfUri, (e) => e);

    const headers: any = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";

    let operationVersion = version,
      operationPromise = operationVersion.create({
        uri: this._uri,
        method: "post",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(operationVersion, payload, this._solution.accountSid)
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

export interface CallContext {
  /**
   * Remove a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<boolean>;

  /**
   * Fetch a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  fetch(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;

  /**
   * Update a CallInstance
   *
   * @param { CallContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  update(
    params: CallContextUpdateOptions,
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;
  update(params: any, callback?: any): Promise<CallInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class CallContextImpl implements CallContext {
  protected _solution: CallSolution;
  protected _uri: string;

  constructor(
    protected _version: V2010,
    accountSid: string,
    testInteger: number
  ) {
    this._solution = { accountSid, testInteger };
    this._uri = `/Accounts/${accountSid}/Calls/${testInteger}.json`;
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

  fetch(callback?: any): Promise<CallInstance> {
    let operationVersion = this._version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(
          operationVersion,
          payload,
          this._solution.accountSid,
          this._solution.testInteger
        )
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  }

  update(params: any, callback?: any): Promise<CallInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (params.testUri === null || params.testUri === undefined) {
      throw new Error('Required parameter "params.testUri" missing.');
    }

    if (params.testMethod === null || params.testMethod === undefined) {
      throw new Error('Required parameter "params.testMethod" missing.');
    }

    const data: any = {};

    if (params.requiredStringProperty !== undefined)
      data["RequiredStringProperty"] = params.requiredStringProperty;
    data["TestUri"] = params.testUri;
    data["TestMethod"] = params.testMethod;

    const headers: any = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";

    let operationVersion = this._version,
      operationPromise = operationVersion.update({
        uri: this._uri,
        method: "post",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(
          operationVersion,
          payload,
          this._solution.accountSid,
          this._solution.testInteger
        )
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

interface CallPayload extends CallResource, Page.TwilioResponsePayload {}

interface CallResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
  test_object?: object | null;
  test_date_time?: string | null;
  test_number?: number | null;
  price_unit?: string | null;
  test_number_float?: number | null;
  test_number_decimal?: object | null;
  test_enum?: object;
  a2p_profile_bundle_sid?: string | null;
  test_array_of_integers?: Array<number>;
  test_array_of_array_of_integers?: Array<Array<number>>;
  test_array_of_objects?: Array<object> | null;
  test_array_of_enum?: Array<object> | null;
}

export class CallInstance {
  protected _solution: CallSolution;
  protected _context?: CallContext;

  constructor(
    protected _version: V2010,
    payload: CallPayload,
    accountSid: string,
    testInteger?: number
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
    this.testNumberDecimal = payload.test_number_decimal;
    this.testEnum = payload.test_enum;
    this.a2pProfileBundleSid = payload.a2p_profile_bundle_sid;
    this.testArrayOfIntegers = payload.test_array_of_integers;
    this.testArrayOfArrayOfIntegers = payload.test_array_of_array_of_integers;
    this.testArrayOfObjects = payload.test_array_of_objects;
    this.testArrayOfEnum = payload.test_array_of_enum;

    this._solution = {
      accountSid,
      testInteger: testInteger || this.testInteger,
    };
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
  testNumberDecimal?: object | null;
  testEnum?: object;
  /**
   * A2P Messaging Profile Bundle BundleSid
   */
  a2pProfileBundleSid?: string | null;
  testArrayOfIntegers?: Array<number>;
  testArrayOfArrayOfIntegers?: Array<Array<number>>;
  testArrayOfObjects?: Array<object> | null;
  /**
   * Permissions authorized to the app
   */
  testArrayOfEnum?: Array<object> | null;

  private get _proxy(): CallContext {
    this._context =
      this._context ||
      new CallContextImpl(
        this._version,
        this._solution.accountSid,
        this._solution.testInteger
      );
    return this._context;
  }

  /**
   * Remove a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<boolean> {
    return this._proxy.remove(callback);
  }

  /**
   * Fetch a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  fetch(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance> {
    return this._proxy.fetch(callback);
  }

  /**
   * Update a CallInstance
   *
   * @param { CallContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  update(
    params: CallContextUpdateOptions,
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;
  update(params: any, callback?: any): Promise<CallInstance> {
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
      testNumberDecimal: this.testNumberDecimal,
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
export interface CallSolution {
  accountSid?: string;
  testInteger?: number;
}

export class CallPage extends Page<
  V2010,
  CallPayload,
  CallResource,
  CallInstance
> {
  /**
   * Initialize the CallPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(
    version: V2010,
    response: Response<string>,
    solution: CallSolution
  ) {
    super(version, response, solution);
  }

  /**
   * Build an instance of CallInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: CallPayload): CallInstance {
    return new CallInstance(
      this._version,
      payload,
      this._solution.accountSid,
      this._solution.testInteger
    );
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
