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
import V1 from "../V1";
const deserialize = require("../../../base/deserialize");
const serialize = require("../../../base/serialize");
import { isValidPathParam } from "../../../base/utility";

/**
 * Options to pass to create a SafelistInstance
 */
export interface SafelistListInstanceCreateOptions {
  /** The phone number to be added in SafeList. Phone numbers must be in [E.164 format](https://www.twilio.com/docs/glossary/what-e164). */
  phoneNumber: string;
}

/**
 * Options to pass to remove a SafelistInstance
 */
export interface SafelistListInstanceRemoveOptions {
  /** The phone number to be removed from SafeList. Phone numbers must be in [E.164 format](https://www.twilio.com/docs/glossary/what-e164). */
  phoneNumber?: string;
}

/**
 * Options to pass to fetch a SafelistInstance
 */
export interface SafelistListInstanceFetchOptions {
  /** The phone number to be fetched from SafeList. Phone numbers must be in [E.164 format](https://www.twilio.com/docs/glossary/what-e164). */
  phoneNumber?: string;
}

export interface SafelistSolution {}

export interface SafelistListInstance {
  _version: V1;
  _solution: SafelistSolution;
  _uri: string;

  /**
   * Create a SafelistInstance
   *
   * @param params - Parameter for request
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed SafelistInstance
   */
  create(
    params: SafelistListInstanceCreateOptions,
    callback?: (error: Error | null, item?: SafelistInstance) => any,
  ): Promise<SafelistInstance>;

  /**
   * Remove a SafelistInstance
   *
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed boolean
   */
  remove(
    callback?: (error: Error | null, item?: boolean) => any,
  ): Promise<boolean>;
  /**
   * Remove a SafelistInstance
   *
   * @param params - Parameter for request
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed SafelistInstance
   */
  remove(
    params: SafelistListInstanceRemoveOptions,
    callback?: (error: Error | null, item?: boolean) => any,
  ): Promise<boolean>;

  /**
   * Fetch a SafelistInstance
   *
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed SafelistInstance
   */
  fetch(
    callback?: (error: Error | null, item?: SafelistInstance) => any,
  ): Promise<SafelistInstance>;
  /**
   * Fetch a SafelistInstance
   *
   * @param params - Parameter for request
   * @param callback - Callback to handle processed record
   *
   * @returns Resolves to processed SafelistInstance
   */
  fetch(
    params: SafelistListInstanceFetchOptions,
    callback?: (error: Error | null, item?: SafelistInstance) => any,
  ): Promise<SafelistInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export function SafelistListInstance(version: V1): SafelistListInstance {
  const instance = {} as SafelistListInstance;

  instance._version = version;
  instance._solution = {};
  instance._uri = `/SafeList/Numbers`;

  instance.create = function create(
    params: SafelistListInstanceCreateOptions,
    callback?: (error: Error | null, items: SafelistInstance) => any,
  ): Promise<SafelistInstance> {
    if (params === null || params === undefined) {
      throw new Error('Required parameter "params" missing.');
    }

    if (params["phoneNumber"] === null || params["phoneNumber"] === undefined) {
      throw new Error("Required parameter \"params['phoneNumber']\" missing.");
    }

    let data: any = {};

    data["PhoneNumber"] = params["phoneNumber"];

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
      (payload) => new SafelistInstance(operationVersion, payload),
    );

    operationPromise = instance._version.setPromiseCallback(
      operationPromise,
      callback,
    );
    return operationPromise;
  };

  instance.remove = function remove(
    params?:
      | SafelistListInstanceRemoveOptions
      | ((error: Error | null, items: boolean) => any),
    callback?: (error: Error | null, items: boolean) => any,
  ): Promise<boolean> {
    if (params instanceof Function) {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["phoneNumber"] !== undefined)
      data["PhoneNumber"] = params["phoneNumber"];

    const headers: any = {};

    let operationVersion = version,
      operationPromise = operationVersion.remove({
        uri: instance._uri,
        method: "delete",
        params: data,
        headers,
      });

    operationPromise = instance._version.setPromiseCallback(
      operationPromise,
      callback,
    );
    return operationPromise;
  };

  instance.fetch = function fetch(
    params?:
      | SafelistListInstanceFetchOptions
      | ((error: Error | null, items: SafelistInstance) => any),
    callback?: (error: Error | null, items: SafelistInstance) => any,
  ): Promise<SafelistInstance> {
    if (params instanceof Function) {
      callback = params;
      params = {};
    } else {
      params = params || {};
    }

    let data: any = {};

    if (params["phoneNumber"] !== undefined)
      data["PhoneNumber"] = params["phoneNumber"];

    const headers: any = {};

    let operationVersion = version,
      operationPromise = operationVersion.fetch({
        uri: instance._uri,
        method: "get",
        params: data,
        headers,
      });

    operationPromise = operationPromise.then(
      (payload) => new SafelistInstance(operationVersion, payload),
    );

    operationPromise = instance._version.setPromiseCallback(
      operationPromise,
      callback,
    );
    return operationPromise;
  };

  instance.toJSON = function toJSON() {
    return instance._solution;
  };

  instance[inspect.custom] = function inspectImpl(
    _depth: any,
    options: InspectOptions,
  ) {
    return inspect(instance.toJSON(), options);
  };

  return instance;
}

interface SafelistPayload extends SafelistResource {}

interface SafelistResource {
  sid: string;
  phone_number: string;
}

export class SafelistInstance {
  constructor(
    protected _version: V1,
    payload: SafelistResource,
  ) {
    this.sid = payload.sid;
    this.phoneNumber = payload.phone_number;
  }

  /**
   * The unique string that we created to identify the SafeList resource.
   */
  sid: string;
  /**
   * The phone number in SafeList.
   */
  phoneNumber: string;

  /**
   * Provide a user-friendly representation
   *
   * @returns Object
   */
  toJSON() {
    return {
      sid: this.sid,
      phoneNumber: this.phoneNumber,
    };
  }

  [inspect.custom](_depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}