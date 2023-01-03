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
import Page, { TwilioResponsePayload } from "../../../../base/Page";
import Response from "../../../../http/response";
import V1 from "../../V1";
const deserialize = require("../../../../base/deserialize");
const serialize = require("../../../../base/serialize");
import { isValidPathParam } from "../../../../base/utility";
import { HistoryListInstance } from "./aws/history";

/**
 * Options to pass to update a AwsInstance
 *
 * @property { string } [testString]
 * @property { boolean } [testBoolean]
 */
export interface AwsContextUpdateOptions {
  testString?: string;
  testBoolean?: boolean;
}
/**
 * Options to pass to each
 *
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
export interface AwsListInstanceEachOptions {
  pageSize?: number;
  callback?: (item: AwsInstance, done: (err?: Error) => void) => void;
  done?: Function;
  limit?: number;
}

/**
 * Options to pass to list
 *
 * @property { number } [pageSize]
 * @property { number } [limit] -
 *                         Upper limit for the number of records to return.
 *                         list() guarantees never to return more than limit.
 *                         Default is no limit
 */
export interface AwsListInstanceOptions {
  pageSize?: number;
  limit?: number;
}

/**
 * Options to pass to page
 *
 * @property { number } [pageSize]
 * @property { number } [pageNumber] - Page Number, this value is simply for client state
 * @property { string } [pageToken] - PageToken provided by the API
 */
export interface AwsListInstancePageOptions {
  pageSize?: number;
  pageNumber?: number;
  pageToken?: string;
}

export interface AwsContext {
  history: HistoryListInstance;

  /**
   * Remove a AwsInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean>;

  /**
   * Fetch a AwsInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AwsInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance>;

  /**
   * Update a AwsInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AwsInstance
   */
  update(
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance>;
  /**
   * Update a AwsInstance
   *
   * @param { AwsContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AwsInstance
   */
  update(
    params: AwsContextUpdateOptions,
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface AwsContextSolution {
  sid?: string;
}

export class AwsContextImpl implements AwsContext {
  protected _solution: AwsContextSolution;
  protected _uri: string;

  protected _history?: HistoryListInstance;

  constructor(protected _version: V1, sid: string) {
    if (!isValidPathParam(sid)) {
      throw new Error("Parameter 'sid' is not valid.");
    }

    this._solution = { sid };
    this._uri = `/Credentials/AWS/${sid}`;
  }

  get history(): HistoryListInstance {
    this._history =
      this._history || HistoryListInstance(this._version, this._solution.sid);
    return this._history;
  }

  remove(
    callback?: (error: Error | null, item?: boolean) => any
  ): Promise<boolean> {
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

  fetch(
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance> {
    let operationVersion = this._version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new AwsInstance(operationVersion, payload, this._solution.sid)
    );

    operationPromise = this._version.setPromiseCallback(
      operationPromise,
      callback
    );
    return operationPromise;
  }

  update(
    params?:
      | AwsContextUpdateOptions
      | ((error: Error | null, item?: AwsInstance) => any),
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance> {
    if (typeof params === "function") {
      callback = params as (error: Error | null, item?: AwsInstance) => any;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["testString"] !== undefined)
      data["TestString"] = params["testString"];
    if (params["testBoolean"] !== undefined)
      data["TestBoolean"] = serialize.bool(params["testBoolean"]);

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
        new AwsInstance(operationVersion, payload, this._solution.sid)
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

interface AwsPayload extends TwilioResponsePayload {
  credentials: AwsResource[];
}

interface AwsResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
}

export class AwsInstance {
  protected _solution: AwsContextSolution;
  protected _context?: AwsContext;

  constructor(protected _version: V1, payload: AwsResource, sid?: string) {
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

  private get _proxy(): AwsContext {
    this._context =
      this._context || new AwsContextImpl(this._version, this._solution.sid);
    return this._context;
  }

  /**
   * Remove a AwsInstance
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
   * Fetch a AwsInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AwsInstance
   */
  fetch(
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance> {
    return this._proxy.fetch(callback);
  }

  /**
   * Update a AwsInstance
   *
   * @param { AwsContextUpdateOptions } params - Parameter for request
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed AwsInstance
   */
  update(
    params?: AwsContextUpdateOptions,
    callback?: (error: Error | null, item?: AwsInstance) => any
  ): Promise<AwsInstance> {
    return this._proxy.update(params, callback);
  }

  /**
   * Access the history.
   */
  history(): HistoryListInstance {
    return this._proxy.history;
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

export interface AwsListInstance {
  (sid: string): AwsContext;
  get(sid: string): AwsContext;

  /**
   * Streams AwsInstance records from the API.
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
   * @param { AwsListInstanceEachOptions } [params] - Options for request
   * @param { function } [callback] - Function to process each record
   */
  each(
    callback?: (item: AwsInstance, done: (err?: Error) => void) => void
  ): void;
  each(
    params: AwsListInstanceEachOptions,
    callback?: (item: AwsInstance, done: (err?: Error) => void) => void
  ): void;
  /**
   * Retrieve a single target page of AwsInstance records from the API.
   *
   * The request is executed immediately.
   *
   * @param { string } [targetUrl] - API-generated URL for the requested results page
   * @param { function } [callback] - Callback to handle list of records
   */
  getPage(
    targetUrl: string,
    callback?: (error: Error | null, items: AwsPage) => any
  ): Promise<AwsPage>;
  /**
   * Lists AwsInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AwsListInstanceOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    callback?: (error: Error | null, items: AwsInstance[]) => any
  ): Promise<AwsInstance[]>;
  list(
    params: AwsListInstanceOptions,
    callback?: (error: Error | null, items: AwsInstance[]) => any
  ): Promise<AwsInstance[]>;
  /**
   * Retrieve a single page of AwsInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AwsListInstancePageOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    callback?: (error: Error | null, items: AwsPage) => any
  ): Promise<AwsPage>;
  page(
    params: AwsListInstancePageOptions,
    callback?: (error: Error | null, items: AwsPage) => any
  ): Promise<AwsPage>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface AwsSolution {}

interface AwsListInstanceImpl extends AwsListInstance {}
class AwsListInstanceImpl implements AwsListInstance {
  _version?: V1;
  _solution?: AwsSolution;
  _uri?: string;
}

export function AwsListInstance(version: V1): AwsListInstance {
  const instance = ((sid) => instance.get(sid)) as AwsListInstanceImpl;

  instance.get = function get(sid): AwsContext {
    return new AwsContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Credentials/AWS`;

  instance.page = function page(
    params?:
      | AwsListInstancePageOptions
      | ((error: Error | null, item?: AwsPage) => any),
    callback?: (error: Error | null, item?: AwsPage) => any
  ): Promise<AwsPage> {
    if (typeof params === "function") {
      callback = params as (error: Error | null, item?: AwsPage) => any;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["pageSize"] !== undefined) data["PageSize"] = params["pageSize"];

    if (params.pageNumber !== undefined) data["Page"] = params.pageNumber;
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
      (payload) => new AwsPage(operationVersion, payload, this._solution)
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
    targetUrl: string,
    callback?: (error: Error | null, items: AwsPage) => any
  ): Promise<AwsPage> {
    let operationPromise = this._version._domain.twilio.request({
      method: "get",
      uri: targetUrl,
    });

    operationPromise = operationPromise.then(
      (payload) => new AwsPage(this._version, payload, this._solution)
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

export class AwsPage extends Page<V1, AwsPayload, AwsResource, AwsInstance> {
  /**
   * Initialize the AwsPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(version: V1, response: Response<string>, solution: AwsSolution) {
    super(version, response, solution);
  }

  /**
   * Build an instance of AwsInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: AwsResource): AwsInstance {
    return new AwsInstance(this._version, payload);
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
