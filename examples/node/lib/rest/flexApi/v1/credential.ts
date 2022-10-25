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
import { NewCredentialsListInstance } from "./credential/newCredentials";
import { AWSListInstance } from "./credential/aws";

export interface CredentialListInstance {
  newCredentials: NewCredentialsListInstance;
  aws: AWSListInstance;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface CredentialSolution {}

interface CredentialListInstanceImpl extends CredentialListInstance {}
class CredentialListInstanceImpl implements CredentialListInstance {
  _version?: V1;
  _solution?: CredentialSolution;
  _uri?: string;

  _newCredentials?: NewCredentialsListInstance;
  _aws?: AWSListInstance;
}

export function CredentialListInstance(version: V1): CredentialListInstance {
  const instance = {} as CredentialListInstanceImpl;

  instance._version = version;
  instance._solution = {};
  instance._uri = `/Credentials`;

  Object.defineProperty(instance, "newCredentials", {
    get: function newCredentials() {
      if (!this._newCredentials) {
        this._newCredentials = NewCredentialsListInstance(this._version);
      }
      return this._newCredentials;
    },
  });

  Object.defineProperty(instance, "aws", {
    get: function aws() {
      if (!this._aws) {
        this._aws = AWSListInstance(this._version);
      }
      return this._aws;
    },
  });

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
