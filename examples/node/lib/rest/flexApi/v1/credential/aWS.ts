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
import V1 from "../../V1";
const deserialize = require("../../../../base/deserialize");
const serialize = require("../../../../base/serialize");

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
 * Options to pass to page a AWSInstance
 *
 * @property { number } [pageSize]
 */
export interface AWSListInstancePageOptions {
  pageSize?: number;
}

export interface AWSContext {
  /**
   * Remove a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<boolean>;
  /**
   * Fetch a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  /**
   * Update a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  update(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  /**
   * Update a AWSInstance
   *
   * @param { AWSContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  update(
    params: AWSContextUpdateOptions,
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  update(params?: any, callback?: any): Promise<AWSInstance>;
  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export class AWSContextImpl implements AWSContext {
  protected _solution: any;
  protected _uri: string;

  constructor(protected _version: V1, sid: string) {
    this._solution = { sid };
    this._uri = `/Credentials/AWS/${sid}`;
  }

  remove(callback?: any): Promise<boolean> {
    let operationVersion = this._version,
      operationPromise = operationVersion.remove({
        uri: this._uri,
        method: "delete",
      });

    if (typeof callback === "function") {
      operationPromise = operationPromise
        .then((value) => callback(null, value))
        .catch((error) => callback(error));
    }

    return operationPromise;
  }

  fetch(callback?: any): Promise<AWSInstance> {
    let operationVersion = this._version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new AWSInstance(operationVersion, payload, this._solution.sid)
    );

    if (typeof callback === "function") {
      operationPromise = operationPromise
        .then((value) => callback(null, value))
        .catch((error) => callback(error));
    }

    return operationPromise;
  }

  update(params?: any, callback?: any): Promise<AWSInstance> {
    if (typeof params === "function") {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    const data: any = {};

    if (params.testString !== undefined) data["TestString"] = params.testString;
    if (params.testBoolean !== undefined)
      data["TestBoolean"] = serialize.bool(params.testBoolean);

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
        new AWSInstance(operationVersion, payload, this._solution.sid)
    );

    if (typeof callback === "function") {
      operationPromise = operationPromise
        .then((value) => callback(null, value))
        .catch((error) => callback(error));
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

interface AWSPayload extends AWSResource, Page.TwilioResponsePayload {}

interface AWSResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
}

export class AWSInstance {
  protected _solution: any;
  protected _context?: AWSContext;

  constructor(protected _version: V1, payload: AWSPayload, sid?: string) {
    this.accountSid = payload.account_sid;
    this.sid = payload.sid;
    this.testString = payload.test_string;
    this.testInteger = deserialize.integer(payload.test_integer);

    this._solution = { sid: sid || this.sid };
  }

  accountSid?: string | null;
  sid?: string | null;
  testString?: string | null;
  testInteger?: number | null;

  private get _proxy(): AWSContext {
    this._context =
      this._context || new AWSContextImpl(this._version, this._solution.sid);
    return this._context;
  }

  /**
   * Remove a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<boolean> {
    return this._proxy.remove(callback);
  }

  /**
   * Fetch a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance> {
    return this._proxy.fetch(callback);
  }

  /**
   * Update a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  update(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  /**
   * Update a AWSInstance
   *
   * @param { AWSContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  update(
    params: AWSContextUpdateOptions,
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  update(params?: any, callback?: any): Promise<AWSInstance> {
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
    };
  }

  [inspect.custom](_depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}

export interface AWSListInstance {
  (sid: string): AWSContext;
  get(sid: string): AWSContext;

  /**
   * Page a AWSInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  page(
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  /**
   * Page a AWSInstance
   *
   * @param { AWSListInstancePageOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AWSInstance
   */
  page(
    params: AWSListInstancePageOptions,
    callback?: (error: Error | null, item?: AWSInstance) => any
  ): Promise<AWSInstance>;
  page(params?: any, callback?: any): Promise<AWSInstance>;
  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface AWSListInstanceImpl extends AWSListInstance {}
class AWSListInstanceImpl implements AWSListInstance {
  _version?: V1;
  _solution?: any;
  _uri?: string;
}

export function AWSListInstance(version: V1): AWSListInstance {
  const instance = ((sid) => instance.get(sid)) as AWSListInstanceImpl;

  instance.get = function get(sid): AWSContext {
    return new AWSContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Credentials/AWS`;

  instance.page = function page(
    params?: any,
    callback?: any
  ): Promise<AWSInstance> {
    if (typeof params === "function") {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    const data: any = {};

    if (params.pageSize !== undefined) data["PageSize"] = params.pageSize;

    const headers: any = {};

    let operationVersion = version,
      operationPromise = operationVersion.page({
        uri: this._uri,
        method: "get",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) => new AWSInstance(operationVersion, payload)
    );

    if (typeof callback === "function") {
      operationPromise = operationPromise
        .then((value) => callback(null, value))
        .catch((error) => callback(error));
    }

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