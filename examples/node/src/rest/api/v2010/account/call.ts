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
import V2010 from "../../V2010";
const deserialize = require("../../../../base/deserialize");
const serialize = require("../../../../base/serialize");
import { isValidPathParam } from "../../../../base/utility";
import { FeedbackCallSummaryListInstance } from "./call/feedbackCallSummary";
import { PhoneNumberCapabilities } from "../../../../interfaces";

export type CallStatus =
  | "in-progress"
  | "paused"
  | "stopped"
  | "processing"
  | "completed"
  | "absent";

export class TestResponseObjectTestArrayOfObjects {
  "count"?: number;
  "description"?: string;
}

/**
 * Options to pass to create a CallInstance
 */
export interface CallListInstanceCreateOptions {
  /**  */
  requiredStringProperty: string;
  /** The HTTP method that we should use to request the `TestArrayOfUri`. */
  testMethod: string;
  /**  */
  testArrayOfStrings?: Array<string>;
  /**  */
  testArrayOfUri?: Array<string>;
}

export interface CallContext {
  /**
   * Remove a CallInstance
   *
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean>;

  /**
   * Fetch a CallInstance
   *
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed CallInstance
   */
  fetch(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface CallContextSolution {
  accountSid: string;
  testInteger: number;
}

export class CallContextImpl implements CallContext {
  protected _solution: CallContextSolution;
  protected _uri: string;

  constructor(
    protected _version: V2010,
    accountSid: string,
    testInteger: number
  ) {
    if (!isValidPathParam(accountSid)) {
      throw new Error("Parameter 'accountSid' is not valid.");
    }

    if (!isValidPathParam(testInteger)) {
      throw new Error("Parameter 'testInteger' is not valid.");
    }

    this._solution = { accountSid, testInteger };
    this._uri = `/Accounts/${accountSid}/Calls/${testInteger}.json`;
  }

  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean> {
    const instance = this;
    let operationVersion = instance._version,
      operationPromise = operationVersion.remove({
        uri: instance._uri,
        method: "delete",
      });

    operationPromise = instance._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  }

  fetch(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance> {
    const instance = this;
    let operationVersion = instance._version,
      operationPromise = operationVersion.fetch({
        uri: instance._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(
          operationVersion,
          payload,
          instance._solution.accountSid,
          instance._solution.testInteger
        )
    );

    operationPromise = instance._version.setPromiseCallback(
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

interface CallPayload extends CallResource {}

interface CallResource {
  account_sid: string;
  sid: string;
  test_string: string;
  test_integer: number;
  test_object: PhoneNumberCapabilities;
  test_date_time: Date;
  test_number: number;
  from: string;
  price_unit: string;
  test_number_float: number;
  test_number_decimal: number;
  test_enum: CallStatus;
  a2p_profile_bundle_sid: string;
  test_array_of_integers: Array<number>;
  test_array_of_array_of_integers: Array<Array<number>>;
  test_array_of_objects: Array<TestResponseObjectTestArrayOfObjects>;
  test_array_of_enum: Array<CallStatus>;
}

export class CallInstance {
  protected _solution: CallContextSolution;
  protected _context?: CallContext;

  constructor(
    protected _version: V2010,
    payload: CallResource,
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
    this.from = payload.from;
    this.priceUnit = payload.price_unit;
    this.testNumberFloat = payload.test_number_float;
    this.testNumberDecimal = deserialize.decimal(payload.test_number_decimal);
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

  accountSid: string;
  sid: string;
  testString: string;
  testInteger: number;
  testObject: PhoneNumberCapabilities;
  testDateTime: Date;
  testNumber: number;
  from: string;
  priceUnit: string;
  testNumberFloat: number;
  testNumberDecimal: number;
  testEnum: CallStatus;
  /**
   * A2P Messaging Profile Bundle BundleSid
   */
  a2pProfileBundleSid: string;
  testArrayOfIntegers: Array<number>;
  testArrayOfArrayOfIntegers: Array<Array<number>>;
  testArrayOfObjects: Array<TestResponseObjectTestArrayOfObjects>;
  /**
   * Permissions authorized to the app
   */
  testArrayOfEnum: Array<CallStatus>;

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
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean> {
    return this._proxy.remove(callback);
  }

  /**
   * Fetch a CallInstance
   *
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed CallInstance
   */
  fetch(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance> {
    return this._proxy.fetch(callback);
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
      from: this.from,
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
  accountSid: string;
}

export interface CallListInstance {
  _version: V2010;
  _solution: CallSolution;
  _uri: string;

  (testInteger: number): CallContext;
  get(testInteger: number): CallContext;

  _feedbackCallSummary?: FeedbackCallSummaryListInstance;
  feedbackCallSummary: FeedbackCallSummaryListInstance;

  /**
   * Create a CallInstance
   *
   * @param params - Parameter for request
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed CallInstance
   */
  create(
    params: CallListInstanceCreateOptions,
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export function CallListInstance(
  version: V2010,
  accountSid: string
): CallListInstance {
  if (!isValidPathParam(accountSid)) {
    throw new Error("Parameter 'accountSid' is not valid.");
  }

  const instance = ((testInteger) =>
    instance.get(testInteger)) as CallListInstance;

  instance.get = function get(testInteger): CallContext {
    return new CallContextImpl(version, accountSid, testInteger);
  };

  instance._version = version;
  instance._solution = { accountSid };
  instance._uri = `/Accounts/${accountSid}/Calls.json`;

  Object.defineProperty(instance, "feedbackCallSummary", {
    get: function feedbackCallSummary() {
      if (!instance._feedbackCallSummary) {
        instance._feedbackCallSummary = FeedbackCallSummaryListInstance(
          instance._version,
          instance._solution.accountSid
        );
      }
      return instance._feedbackCallSummary;
    },
  });

  instance.create = function create(
    params: CallListInstanceCreateOptions,
    callback?: (error: Error | null, items: CallInstance) => any
  ): Promise<CallInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (
      params["requiredStringProperty"] === null ||
      params["requiredStringProperty"] === undefined
    ) {
      throw new Error(
        "Required parameter \"params['requiredStringProperty']\" missing."
      );
    }

    if (params["testMethod"] === null || params["testMethod"] === undefined) {
      throw new Error("Required parameter \"params['testMethod']\" missing.");
    }

    let data: any = {};

    data["RequiredStringProperty"] = params["requiredStringProperty"];
    if (params["testArrayOfStrings"] !== undefined)
      data["TestArrayOfStrings"] = serialize.map(
        params["testArrayOfStrings"],
        (e: string) => e
      );
    if (params["testArrayOfUri"] !== undefined)
      data["TestArrayOfUri"] = serialize.map(
        params["testArrayOfUri"],
        (e: string) => e
      );

    data["TestMethod"] = params["testMethod"];

    const headers: any = {};
    headers["Content-Type"] = "application/x-www-form-urlencoded";

    let operationVersion = version,
      operationPromise = operationVersion.create({
        uri: instance._uri,
        method: "post",
        data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(
          operationVersion,
          payload,
          instance._solution.accountSid
        )
    );

    operationPromise = instance._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  };

  instance.toJSON = function toJSON() {
    return instance._solution;
  };

  instance[inspect.custom] = function inspectImpl(
    _depth: any,
    options: InspectOptions
  ) {
    return inspect(instance.toJSON(), options);
  };

  return instance;
}
