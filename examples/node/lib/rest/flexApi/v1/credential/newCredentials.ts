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
import V1 from "../../V1";
const deserialize = require("../../../../base/deserialize");
const serialize = require("../../../../base/serialize");

/**
 * Options to pass to create a NewCredentialsInstance
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
 * @property { Date } [testDate]
 * @property { TestEnumStatus } [testEnum]
 * @property { Array<object> } [testObjectArray]
 * @property { any } [testAnyType]
 * @property { Array<string> } [permissions] A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;.
 */
export interface NewCredentialsListInstanceCreateOptions {
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
  testDate?: Date;
  testEnum?: TestEnumStatus;
  testObjectArray?: Array<object>;
  testAnyType?: any;
  permissions?: Array<string>;
}

export interface NewCredentialsListInstance {
  /**
   * Create a NewCredentialsInstance
   *
   * @param { NewCredentialsListInstanceCreateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed NewCredentialsInstance
   */
  create(
    params: NewCredentialsListInstanceCreateOptions,
    callback?: (error: Error | null, item?: NewCredentialsInstance) => any
  ): Promise<NewCredentialsInstance>;
  create(params: any, callback?: any): Promise<NewCredentialsInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface NewCredentialsListInstanceImpl extends NewCredentialsListInstance {}
class NewCredentialsListInstanceImpl implements NewCredentialsListInstance {
  _version?: V1;
  _solution?: NewCredentialsSolution;
  _uri?: string;
}

export function NewCredentialsListInstance(
  version: V1
): NewCredentialsListInstance {
  const instance = {} as NewCredentialsListInstanceImpl;

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Credentials/AWS`;

  instance.create = function create(
    params: any,
    callback?: any
  ): Promise<NewCredentialsInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (params.testString === null || params.testString === undefined) {
      throw new Error('Required parameter "params.testString" missing.');
    }

    const data: any = {};

    data["TestString"] = params.testString;
    if (params.testBoolean !== undefined)
      data["TestBoolean"] = serialize.bool(params.testBoolean);
    if (params.testInteger !== undefined)
      data["TestInteger"] = params.testInteger;
    if (params.testNumber !== undefined) data["TestNumber"] = params.testNumber;
    if (params.testNumberFloat !== undefined)
      data["TestNumberFloat"] = params.testNumberFloat;
    if (params.testNumberDouble !== undefined)
      data["TestNumberDouble"] = params.testNumberDouble;
    if (params.testNumberInt32 !== undefined)
      data["TestNumberInt32"] = params.testNumberInt32;
    if (params.testNumberInt64 !== undefined)
      data["TestNumberInt64"] = params.testNumberInt64;
    if (params.testObject !== undefined)
      data["TestObject"] = serialize.object(params.testObject);
    if (params.testDateTime !== undefined)
      data["TestDateTime"] = serialize.iso8601DateTime(params.testDateTime);
    if (params.testDate !== undefined)
      data["TestDate"] = serialize.iso8601Date(params.testDate);
    if (params.testEnum !== undefined) data["TestEnum"] = params.testEnum;
    if (params.testObjectArray !== undefined)
      data["TestObjectArray"] = serialize.map(params.testObjectArray, (e) => e);
    if (params.testAnyType !== undefined)
      data["TestAnyType"] = params.testAnyType;
    if (params.permissions !== undefined)
      data["Permissions"] = serialize.map(params.permissions, (e) => e);

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
      (payload) => new NewCredentialsInstance(operationVersion, payload)
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

interface NewCredentialsPayload
  extends NewCredentialsResource,
    Page.TwilioResponsePayload {}

interface NewCredentialsResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
}

export class NewCredentialsInstance {
  protected _solution: NewCredentialsSolution;
  protected _context?: NewCredentialsListInstance;

  constructor(protected _version: V1, payload: NewCredentialsPayload) {
    this.accountSid = payload.account_sid;
    this.sid = payload.sid;
    this.testString = payload.test_string;
    this.testInteger = deserialize.integer(payload.test_integer);

    this._solution = {};
  }

  accountSid?: string | null;
  sid?: string | null;
  testString?: string | null;
  testInteger?: number | null;

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
    };
  }

  [inspect.custom](_depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
export interface NewCredentialsSolution {}

export class NewCredentialsPage extends Page<
  V1,
  NewCredentialsPayload,
  NewCredentialsResource,
  NewCredentialsInstance
> {
  /**
   * Initialize the NewCredentialsPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(
    version: V1,
    response: Response<string>,
    solution: NewCredentialsSolution
  ) {
    super(version, response, solution);
  }

  /**
   * Build an instance of NewCredentialsInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: NewCredentialsPayload): NewCredentialsInstance {
    return new NewCredentialsInstance(this._version, payload);
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
