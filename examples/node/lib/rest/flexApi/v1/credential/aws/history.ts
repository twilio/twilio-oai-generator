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
import V1 from "../../../V1";
const deserialize = require("../../../../../base/deserialize");
const serialize = require("../../../../../base/serialize");
import { isValidPathParam } from "../../../../../base/utility";

/**
 * Options to pass to fetch a HistoryInstance
 *
 * @property { object } [addOnsData]
 */
export interface HistoryContextFetchOptions {
  addOnsData?: object;
}

export interface HistoryContext {
  /**
   * Fetch a HistoryInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed HistoryInstance
   */
  fetch(
    callback?: (error: Error | null, item?: HistoryInstance) => any
  ): Promise<HistoryInstance>;
  /**
   * Fetch a HistoryInstance
   *
   * @param { HistoryContextFetchOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed HistoryInstance
   */
  fetch(
    params: HistoryContextFetchOptions,
    callback?: (error: Error | null, item?: HistoryInstance) => any
  ): Promise<HistoryInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface HistoryContextSolution {
  sid?: string;
}

export class HistoryContextImpl implements HistoryContext {
  protected _solution: HistoryContextSolution;
  protected _uri: string;

  constructor(protected _version: V1, sid: string) {
    if (!isValidPathParam(sid)) {
      throw new Error("Parameter 'sid' is not valid.");
    }

    this._solution = { sid };
    this._uri = `/Credentials/AWS/${sid}/History`;
  }

  fetch(
    params?:
      | HistoryContextFetchOptions
      | ((error: Error | null, item?: HistoryInstance) => any),
    callback?: (error: Error | null, item?: HistoryInstance) => any
  ): Promise<HistoryInstance> {
    if (typeof params === "function") {
      callback = params as (error: Error | null, item?: HistoryInstance) => any;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["addOnsData"] !== undefined)
      data = {
        ...data,
        ...serialize.prefixedCollapsibleMap(params["addOnsData"], "AddOns"),
      };

    const headers: any = {};

    let operationVersion = this._version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new HistoryInstance(operationVersion, payload, this._solution.sid)
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

interface HistoryPayload extends HistoryResource {}

interface HistoryResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
}

export class HistoryInstance {
  protected _solution: HistoryContextSolution;
  protected _context?: HistoryContext;

  constructor(protected _version: V1, payload: HistoryResource, sid: string) {
    this.accountSid = payload.account_sid;
    this.sid = payload.sid;
    this.testString = payload.test_string;
    this.testInteger = deserialize.integer(payload.test_integer);

    this._solution = { sid };
  }

  accountSid?: string | null;
  sid?: string | null;
  testString?: string | null;
  testInteger?: number | null;

  private get _proxy(): HistoryContext {
    this._context =
      this._context ||
      new HistoryContextImpl(this._version, this._solution.sid);
    return this._context;
  }

  /**
   * Fetch a HistoryInstance
   *
   * @param { HistoryContextFetchOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed HistoryInstance
   */
  fetch(
    params?: HistoryContextFetchOptions,
    callback?: (error: Error | null, item?: HistoryInstance) => any
  ): Promise<HistoryInstance> {
    return this._proxy.fetch(params, callback);
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

export interface HistoryListInstance {
  (): HistoryContext;
  get(): HistoryContext;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface HistorySolution {
  sid?: string;
}

interface HistoryListInstanceImpl extends HistoryListInstance {}
class HistoryListInstanceImpl implements HistoryListInstance {
  _version?: V1;
  _solution?: HistorySolution;
  _uri?: string;
}

export function HistoryListInstance(
  version: V1,
  sid: string
): HistoryListInstance {
  if (!isValidPathParam(sid)) {
    throw new Error("Parameter 'sid' is not valid.");
  }

  const instance = (() => instance.get()) as HistoryListInstanceImpl;

  instance.get = function get(): HistoryContext {
    return new HistoryContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = { sid };
  instance._uri = ``;

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
