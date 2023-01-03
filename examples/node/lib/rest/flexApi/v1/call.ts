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

export interface CallContext {
  /**
   * Update a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  update(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance>;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface CallContextSolution {
  sid: string;
}

export class CallContextImpl implements CallContext {
  protected _solution: CallContextSolution;
  protected _uri: string;

  constructor(protected _version: V1, sid: string) {
    if (!isValidPathParam(sid)) {
      throw new Error("Parameter 'sid' is not valid.");
    }

    this._solution = { sid };
    this._uri = `/Voice/${sid}`;
  }

  update(callback?: any): Promise<CallInstance> {
    const instance = this;
    let operationVersion = instance._version,
      operationPromise = operationVersion.update({
        uri: instance._uri,
        method: "post",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(operationVersion, payload, instance._solution.sid)
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
  sid?: number | null;
}

export class CallInstance {
  protected _solution: CallContextSolution;
  protected _context?: CallContext;

  constructor(protected _version: V1, payload: CallResource, sid?: string) {
    this.sid = deserialize.integer(payload.sid);

    this._solution = { sid: sid || this.sid.toString() };
  }

  /**
   * Non-string path parameter in the response.
   */
  sid?: number | null;

  private get _proxy(): CallContext {
    this._context =
      this._context || new CallContextImpl(this._version, this._solution.sid);
    return this._context;
  }

  /**
   * Update a CallInstance
   *
   * @param { function } [callback] - Callback to handle processed record
   *
   * @returns { Promise } Resolves to processed CallInstance
   */
  update(
    callback?: (error: Error | null, item?: CallInstance) => any
  ): Promise<CallInstance> {
    return this._proxy.update(callback);
  }

  /**
   * Provide a user-friendly representation
   *
   * @returns Object
   */
  toJSON() {
    return {
      sid: this.sid,
    };
  }

  [inspect.custom](_depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}

export interface CallSolution {}

export interface CallListInstance {
  _version: V1;
  _solution: CallSolution;
  _uri: string;

  (sid: string): CallContext;
  get(sid: string): CallContext;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export function CallListInstance(version: V1): CallListInstance {
  const instance = ((sid) => instance.get(sid)) as CallListInstance;

  instance.get = function get(sid): CallContext {
    return new CallContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = ``;

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
