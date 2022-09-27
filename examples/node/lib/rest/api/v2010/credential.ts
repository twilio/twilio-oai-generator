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
import { AWSListInstance } from "./credential/aWS";

export interface CredentialListInstance {
  aws: AWSListInstance;

  /**
   * Streams CredentialInstance records from the API.
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
    callback?: (item: CredentialInstance, done: (err?: Error) => void) => void
  ): void;
  /**
   * Streams CredentialInstance records from the API.
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
   * @param { CredentialListInstanceEachOptions } [params] - Options for request
   * @param { function } [callback] - Function to process each record
   */
  each(
    params?: CredentialListInstanceEachOptions,
    callback?: (item: CredentialInstance, done: (err?: Error) => void) => void
  ): void;
  each(params?: any, callback?: any): void;
  /**
   * Retrieve a single target page of CredentialInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  getPage(
    callback?: (error: Error | null, items: CredentialPage) => any
  ): Promise<CredentialPage>;
  /**
   * Retrieve a single target page of CredentialInstance records from the API.
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
    callback?: (error: Error | null, items: CredentialPage) => any
  ): Promise<CredentialPage>;
  getPage(params?: any, callback?: any): Promise<CredentialPage>;
  /**
   * Lists CredentialInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    callback?: (error: Error | null, items: CredentialInstance[]) => any
  ): Promise<CredentialInstance[]>;
  /**
   * Lists CredentialInstance records from the API as a list.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { CredentialListInstanceOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  list(
    params?: CredentialListInstanceOptions,
    callback?: (error: Error | null, items: CredentialInstance[]) => any
  ): Promise<CredentialInstance[]>;
  list(params?: any, callback?: any): Promise<CredentialInstance[]>;
  /**
   * Retrieve a single page of CredentialInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    callback?: (error: Error | null, items: CredentialPage) => any
  ): Promise<CredentialPage>;
  /**
   * Retrieve a single page of CredentialInstance records from the API.
   *
   * The request is executed immediately.
   *
   * If a function is passed as the first argument, it will be used as the callback
   * function.
   *
   * @param { CredentialListInstancePageOptions } [params] - Options for request
   * @param { function } [callback] - Callback to handle list of records
   */
  page(
    params: CredentialListInstancePageOptions,
    callback?: (error: Error | null, items: CredentialPage) => any
  ): Promise<CredentialPage>;
  page(params?: any, callback?: any): Promise<CredentialPage>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface CredentialListInstanceImpl extends CredentialListInstance {}
class CredentialListInstanceImpl implements CredentialListInstance {
  _version?: V2010;
  _solution?: CredentialSolution;
  _uri?: string;

  _aws?: AWSListInstance;
}

export function CredentialListInstance(version: V2010): CredentialListInstance {
  const instance = {} as CredentialListInstanceImpl;

  instance._version = version;
  instance._solution = {};
  instance._uri = `/v1/Credentials`;

  Object.defineProperty(instance, "aws", {
    get: function aws() {
      if (!this._aws) {
        this._aws = AWSListInstance(this._version);
      }
      return this._aws;
    },
  });

  instance.page = function page(callback?: any): Promise<CredentialPage> {
    let operationVersion = version,
      operationPromise = operationVersion.page({
        uri: this._uri,
        method: "get",
      });

    operationPromise = operationPromise.then(
      (payload) => new CredentialPage(operationVersion, payload, this._solution)
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
  ): Promise<CredentialPage> {
    let operationPromise = this._version._domain.twilio.request({
      method: "get",
      uri: targetUrl,
    });

    operationPromise = operationPromise.then(
      (payload) => new CredentialPage(this._version, payload, this._solution)
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
