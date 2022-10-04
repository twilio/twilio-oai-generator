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

import ApiBase from "../ApiBase";
import Version from "../../base/Version";
import { AccountListInstance } from "./v2010/account";

export default class V2010 extends Version {
  /**
   * Initialize the V2010 version of Api
   *
   * @property { Twilio.Api.V2010.AccountListInstance } accounts - accounts resource
   *
   * @param { Twilio.Api } domain - The Twilio domain
   */
  constructor(domain: ApiBase) {
    super(domain, "2010-04-01");
  }

  protected _accounts?: AccountListInstance;

  get accounts(): AccountListInstance {
    this._accounts = this._accounts || AccountListInstance(this);
    return this._accounts;
  }

}
