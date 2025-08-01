/*
 * This code was generated by
 * ___ _ _ _ _ _    _ ____    ____ ____ _    ____ ____ _  _ ____ ____ ____ ___ __   __
 *  |  | | | | |    | |  | __ |  | |__| | __ | __ |___ |\ | |___ |__/ |__|  | |  | |__/
 *  |  |_|_| | |___ | |__|    |__| |  | |    |__] |___ | \| |___ |  \ |  |  | |__| |  \
 *
 * Organization Public API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * NOTE: This class is auto generated by OpenAPI Generator.
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { inspect, InspectOptions } from "util";
import Versionless from "../Versionless";
const deserialize = require("../../../base/deserialize");
const serialize = require("../../../base/serialize");
import { isValidPathParam } from "../../../base/utility";
import { AccountListInstance } from "./organization/account";
import { RoleAssignmentListInstance } from "./organization/roleAssignment";
import { UserListInstance } from "./organization/user";

export interface OrganizationContext {
  accounts: AccountListInstance;
  roleAssignments: RoleAssignmentListInstance;
  users: UserListInstance;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export interface OrganizationContextSolution {
  organizationSid: string;
}

export class OrganizationContextImpl implements OrganizationContext {
  protected _solution: OrganizationContextSolution;
  protected _uri: string;

  protected _accounts?: AccountListInstance;
  protected _roleAssignments?: RoleAssignmentListInstance;
  protected _users?: UserListInstance;

  constructor(
    protected _version: Versionless,
    organizationSid: string,
  ) {
    if (!isValidPathParam(organizationSid)) {
      throw new Error("Parameter 'organizationSid' is not valid.");
    }

    this._solution = { organizationSid };
    this._uri = `/${organizationSid}`;
  }

  get accounts(): AccountListInstance {
    this._accounts =
      this._accounts ||
      AccountListInstance(this._version, this._solution.organizationSid);
    return this._accounts;
  }

  get roleAssignments(): RoleAssignmentListInstance {
    this._roleAssignments =
      this._roleAssignments ||
      RoleAssignmentListInstance(this._version, this._solution.organizationSid);
    return this._roleAssignments;
  }

  get users(): UserListInstance {
    this._users =
      this._users ||
      UserListInstance(this._version, this._solution.organizationSid);
    return this._users;
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

export interface OrganizationSolution {}

export interface OrganizationListInstance {
  _version: Versionless;
  _solution: OrganizationSolution;
  _uri: string;

  (organizationSid: string): OrganizationContext;
  get(organizationSid: string): OrganizationContext;

  /**
   * Provide a user-friendly representation
   */
  toJSON(): any;
  [inspect.custom](_depth: any, options: InspectOptions): any;
}

export function OrganizationListInstance(
  version: Versionless,
): OrganizationListInstance {
  const instance = ((organizationSid) =>
    instance.get(organizationSid)) as OrganizationListInstance;

  instance.get = function get(organizationSid): OrganizationContext {
    return new OrganizationContextImpl(version, organizationSid);
  };

  instance._version = version;
  instance._solution = {};
  instance._uri = ``;

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
