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
import Page from "../../../../../base/Page";
import Response from "../../../../../http/response";
import V2010 from "../../../V2010";
const deserialize = require("../../../../../base/deserialize");
const serialize = require("../../../../../base/serialize");

/**
 * Options to pass to update a FeedbackCallSummaryInstance
 *
 * @property { string } endDate
 * @property { string } startDate
 */
export interface FeedbackCallSummaryContextUpdateOptions {
  endDate: string;
  startDate: string;
}

export interface FeedbackCallSummaryListInstance {
  (sid: string): FeedbackCallSummaryContext;
  get(sid: string): FeedbackCallSummaryContext;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface FeedbackCallSummaryListInstanceImpl
  extends FeedbackCallSummaryListInstance {}
class FeedbackCallSummaryListInstanceImpl
  implements FeedbackCallSummaryListInstance
{
  _version?: V2010;
  _solution?: FeedbackCallSummarySolution;
  _uri?: string;
}

export function FeedbackCallSummaryListInstance(
  version: V2010,
  accountSid: string
): FeedbackCallSummaryListInstance {
  const instance = ((sid) =>
    instance.get(sid)) as FeedbackCallSummaryListInstanceImpl;

  instance.get = function get(sid): FeedbackCallSummaryContext {
    return new FeedbackCallSummaryContextImpl(version, accountSid, sid);
  };

  instance._version = version;
  instance._solution = { accountSid };
  instance._uri = `/Accounts/${accountSid}/Calls/FeedbackSummary.json`;

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

export interface FeedbackCallSummaryContext {
  /**
   * Update a FeedbackCallSummaryInstance
   *
   * @param { FeedbackCallSummaryContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed FeedbackCallSummaryInstance
   */
  update(
    params: FeedbackCallSummaryContextUpdateOptions,
    callback?: (error: Error | null, item?: FeedbackCallSummaryInstance) => any
  ): Promise<FeedbackCallSummaryInstance>;
  update(params: any, callback?: any): Promise<FeedbackCallSummaryInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class FeedbackCallSummaryContextImpl
  implements FeedbackCallSummaryContext
{
  protected _solution: FeedbackCallSummarySolution;
  protected _uri: string;

  constructor(protected _version: V2010, accountSid: string, sid: string) {
    this._solution = { accountSid, sid };
    this._uri = `/Accounts/${accountSid}/Calls/FeedbackSummary/${sid}.json`;
  }

  update(params: any, callback?: any): Promise<FeedbackCallSummaryInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (params.endDate === null || params.endDate === undefined) {
      throw new Error('Required parameter "params.endDate" missing.');
    }

    if (params.startDate === null || params.startDate === undefined) {
      throw new Error('Required parameter "params.startDate" missing.');
    }

    const data: any = {};

    data["EndDate"] = serialize.iso8601Date(params.endDate);
    data["StartDate"] = serialize.iso8601Date(params.startDate);

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
        new FeedbackCallSummaryInstance(
          operationVersion,
          payload,
          this._solution.accountSid,
          this._solution.sid
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

interface FeedbackCallSummaryPayload
  extends FeedbackCallSummaryResource,
    Page.TwilioResponsePayload {}

interface FeedbackCallSummaryResource {
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
  test_array_of_integers?: Array<number>;
  test_array_of_array_of_integers?: Array<Array<number>>;
  test_array_of_objects?: Array<object> | null;
  test_array_of_enum?: Array<object> | null;
}

export class FeedbackCallSummaryInstance {
  protected _solution: FeedbackCallSummarySolution;
  protected _context?: FeedbackCallSummaryContext;

  constructor(
    protected _version: V2010,
    payload: FeedbackCallSummaryPayload,
    accountSid: string,
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
    this.testNumberDecimal = payload.test_number_decimal;
    this.testEnum = payload.test_enum;
    this.testArrayOfIntegers = payload.test_array_of_integers;
    this.testArrayOfArrayOfIntegers = payload.test_array_of_array_of_integers;
    this.testArrayOfObjects = payload.test_array_of_objects;
    this.testArrayOfEnum = payload.test_array_of_enum;

    this._solution = { accountSid, sid: sid || this.sid };
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
  testArrayOfIntegers?: Array<number>;
  testArrayOfArrayOfIntegers?: Array<Array<number>>;
  testArrayOfObjects?: Array<object> | null;
  /**
   * Permissions authorized to the app
   */
  testArrayOfEnum?: Array<object> | null;

  private get _proxy(): FeedbackCallSummaryContext {
    this._context =
      this._context ||
      new FeedbackCallSummaryContextImpl(
        this._version,
        this._solution.accountSid,
        this._solution.sid
      );
    return this._context;
  }

  /**
   * Update a FeedbackCallSummaryInstance
   *
   * @param { FeedbackCallSummaryContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed FeedbackCallSummaryInstance
   */
  update(
    params: FeedbackCallSummaryContextUpdateOptions,
    callback?: (error: Error | null, item?: FeedbackCallSummaryInstance) => any
  ): Promise<FeedbackCallSummaryInstance>;
  update(params: any, callback?: any): Promise<FeedbackCallSummaryInstance> {
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
export interface FeedbackCallSummarySolution {
  accountSid?: string;
  sid?: string;
}

export class FeedbackCallSummaryPage extends Page<
  V2010,
  FeedbackCallSummaryPayload,
  FeedbackCallSummaryResource,
  FeedbackCallSummaryInstance
> {
  /**
   * Initialize the FeedbackCallSummaryPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(
    version: V2010,
    response: Response<string>,
    solution: FeedbackCallSummarySolution
  ) {
    super(version, response, solution);
  }

  /**
   * Build an instance of FeedbackCallSummaryInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(
    payload: FeedbackCallSummaryPayload
  ): FeedbackCallSummaryInstance {
    return new FeedbackCallSummaryInstance(
      this._version,
      payload,
      this._solution.accountSid,
      this._solution.sid
    );
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
