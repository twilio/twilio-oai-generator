/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Twilio - Versionless
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { inspect, InspectOptions } from "util";
import Page, { TwilioResponsePayload } from "../../../base/Page";
import Response from "../../../http/response";
import Understand from "../Understand";
const deserialize = require("../../../base/deserialize");
const serialize = require("../../../base/serialize");
import { isValidPathParam } from "../../../base/utility";

/**
 * Options to pass to each
 *
 * @property { Function } [callback] -
 *                         Function to process each record. If this and a positional
 *                         callback are passed, this one will be used
 * @property { Function } [done] - Function to be called upon completion of streaming
 * @property { number } [limit] -
 *                         Upper limit for the number of records to return.
 *                         each() guarantees never to return more than limit.
 *                         Default is no limit
 */
export interface AssistantListInstanceEachOptions {
  callback?: (item: AssistantInstance, done: (err?: Error) => void) => void;
  done?: Function;
  limit?: number;
}

/**
 * Options to pass to list
 *
 * @property { number } [limit] -
 *                         Upper limit for the number of records to return.
 *                         list() guarantees never to return more than limit.
 *                         Default is no limit
 */
export interface AssistantListInstanceOptions {
  limit?: number;
}

/**
 * Options to pass to page
 *
 * @property { number } [pageNumber] - Page Number, this value is simply for client state
 * @property { string } [pageToken] - PageToken provided by the API
 */
export interface AssistantListInstancePageOptions {
  pageNumber?: number;
  pageToken?: string;
}

export interface AssistantListInstance {
  /**
   * Streams AssistantInstance records from the API.
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
    callback?: (item: AssistantInstance, done: (err?: Error) => void) => void
  ): void;
  /**
   * Streams AssistantInstance records from the API.
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
   * @param { AssistantListInstanceEachOptions } [params] - Options for request
   * @param { function } [callback] - Function to process each record
   */
  each(
    params?: AssistantListInstanceEachOptions,
    callback?: (item: AssistantInstance, done: (err?: Error) => void) => void
  ): void;
  each(params?: any, callback?: any): void;
  /**
   * Retrieve a single target page of AssistantInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  getPage(
    callback?: (error: Error | null, items: AssistantPage) => any
  ): Promise<AssistantPage>;
  /**
   * Retrieve a single target page of AssistantInstance records from the API.
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
    callback?: (error: Error | null, items: AssistantPage) => any
  ): Promise<AssistantPage>;
  getPage(params?: any, callback?: any): Promise<AssistantPage>;
  /**
   * Lists AssistantInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    callback?: (error: Error | null, items: AssistantInstance[]) => any
  ): Promise<AssistantInstance[]>;
  /**
   * Lists AssistantInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AssistantListInstanceOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    params?: AssistantListInstanceOptions,
    callback?: (error: Error | null, items: AssistantInstance[]) => any
  ): Promise<AssistantInstance[]>;
  list(params?: any, callback?: any): Promise<AssistantInstance[]>;
  /**
   * Retrieve a single page of AssistantInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    callback?: (error: Error | null, items: AssistantPage) => any
  ): Promise<AssistantPage>;
  /**
   * Retrieve a single page of AssistantInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { AssistantListInstancePageOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    params: AssistantListInstancePageOptions,
    callback?: (error: Error | null, items: AssistantPage) => any
  ): Promise<AssistantPage>;
  page(params?: any, callback?: any): Promise<AssistantPage>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface AssistantSolution {}

interface AssistantListInstanceImpl extends AssistantListInstance {}
class AssistantListInstanceImpl implements AssistantListInstance {
  _version?: Understand;
  _solution?: AssistantSolution;
  _uri?: string;
}

export function AssistantListInstance(
  version: Understand
): AssistantListInstance {
  const instance = {} as AssistantListInstanceImpl;

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Assistants`;

  instance.page = function page(callback?: any): Promise<AssistantPage> {
    let operationVersion = version,
      operationPromise = operationVersion.page({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) => new AssistantPage(operationVersion, payload, this._solution)
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
  ): Promise<AssistantPage> {
    let operationPromise = this._version._domain.twilio.request({
      method: "get",
      uri: targetUrl,
    });

    operationPromise = operationPromise.then(
      (payload) => new AssistantPage(this._version, payload, this._solution)
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

interface AssistantPayload extends TwilioResponsePayload {
  assistants: AssistantResource[];
}

interface AssistantResource {
  sid?: string | null;
  friendly_name?: string | null;
}

export class AssistantInstance {
  constructor(protected _version: Understand, payload: AssistantPayload) {
    this.sid = payload.sid;
    this.friendlyName = payload.friendly_name;
  }

  /**
   * A string that uniquely identifies this Fleet.
   */
  sid?: string | null;
  /**
   * A human readable description for this Fleet.
   */
  friendlyName?: string | null;

  /**
   * Provide a user-friendly representation
   *
   * @returns Object
   */
  toJSON() {
    return {
      sid: this.sid,
      friendlyName: this.friendlyName,
    };
  }

  [inspect.custom](_depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}

export class AssistantPage extends Page<
  Understand,
  AssistantPayload,
  AssistantResource,
  AssistantInstance
> {
  /**
   * Initialize the AssistantPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(
    version: Understand,
    response: Response<string>,
    solution: AssistantSolution
  ) {
    super(version, response, solution);
  }

  /**
   * Build an instance of AssistantInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: AssistantPayload): AssistantInstance {
    return new AssistantInstance(this._version, payload);
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}
