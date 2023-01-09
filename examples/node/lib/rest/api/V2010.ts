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
import { AccountContext } from "./v2010/account";

export default class V2010 extends Version {
  /**
   * Initialize the V2010 version of Api
   *
   * @param domain - The Twilio (Twilio.Api) domain
   */
  constructor(domain: ApiBase) {
    super(domain, "2010-04-01");
  }

  /** accounts - { Twilio.Api.V2010.AccountListInstance } resource */
  protected _accounts?: AccountListInstance;
  /** account - { Twilio.Api.V2010.AccountContext } resource */
  protected _account?: AccountContext;

  /** Getter for accounts resource */
  get accounts(): AccountListInstance {
    this._accounts = this._accounts || AccountListInstance(this);
    return this._accounts;
  }

  /** Getter for account resource */
  get account(): AccountContext {
    this._account =
      this._account || AccountListInstance(this)(this.domain.twilio.accountSid);
    return this._account;
  }
}
