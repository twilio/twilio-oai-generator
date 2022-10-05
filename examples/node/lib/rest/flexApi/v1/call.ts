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
import V1 from "../V1";
const deserialize = require("../../../base/deserialize");
const serialize = require("../../../base/serialize");

export interface CallListInstance {
  (sid: string): CallContext;
  get(sid: string): CallContext;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

interface CallListInstanceImpl extends CallListInstance {}
class CallListInstanceImpl implements CallListInstance {
  _version?: V1;
  _solution?: CallSolution;
  _uri?: string;
}

export function CallListInstance(version: V1): CallListInstance {
  const instance = ((sid) => instance.get(sid)) as CallListInstanceImpl;

  instance.get = function get(sid): CallContext {
    return new CallContextImpl(version, sid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Voice`;

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

export class CallContextImpl implements CallContext {
  protected _solution: CallSolution;
  protected _uri: string;

  constructor(protected _version: V1, sid: string) {
    this._solution = { sid };
    this._uri = `/Voice/${sid}`;
  }

  update(callback?: any): Promise<CallInstance> {
    let operationVersion = this._version,
      operationPromise = operationVersion.update({
        uri: this._uri,
        method: "post",
      });

    operationPromise = operationPromise.then(
      (payload) =>
        new CallInstance(operationVersion, payload, this._solution.sid)
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

export interface CallSolution {
  sid?: string;
}

export class CallPage extends Page<
  V1,
  CallPayload,
  CallResource,
  CallInstance
> {
  /**
   * Initialize the CallPage
   *
   * @param version - Version of the resource
   * @param response - Response from the API
   * @param solution - Path solution
   */
  constructor(version: V1, response: Response<string>, solution: CallSolution) {
    super(version, response, solution);
  }

  /**
   * Build an instance of CallInstance
   *
   * @param payload - Payload response from the API
   */
  getInstance(payload: CallPayload): CallInstance {
    return new CallInstance(this._version, payload, this._solution.sid);
  }

  [inspect.custom](depth: any, options: InspectOptions) {
    return inspect(this.toJSON(), options);
  }
}

interface CallPayload extends CallResource, Page.TwilioResponsePayload {}

interface CallResource {
  sid?: string | null;
}

export class CallInstance {
  protected _solution: CallSolution;
  protected _context?: CallContext;

  constructor(protected _version: V1, payload: CallPayload, sid?: string) {
    this.sid = payload.sid;

    this._solution = { sid: sid || this.sid };
  }

  sid?: string | null;

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
