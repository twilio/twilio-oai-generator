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

export interface HistoryListInstance {
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
  const instance = {} as HistoryListInstanceImpl;

  instance._version = version;
  instance._solution = { sid };
  instance._uri = `/Credentials/AWS/${sid}/History`;

  instance.fetch = function fetch(callback?: any): Promise<HistoryInstance> {
    let operationVersion = version,
      operationPromise = operationVersion.fetch({
        uri: this._uri,
        method: "get",
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

interface HistoryPayload extends HistoryResource {}

interface HistoryResource {
  account_sid?: string | null;
  sid?: string | null;
  test_string?: string | null;
  test_integer?: number | null;
}

export class HistoryInstance {
  constructor(protected _version: V1, payload: HistoryPayload, sid?: string) {
    this.accountSid = payload.account_sid;
    this.sid = payload.sid;
    this.testString = payload.test_string;
    this.testInteger = deserialize.integer(payload.test_integer);
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