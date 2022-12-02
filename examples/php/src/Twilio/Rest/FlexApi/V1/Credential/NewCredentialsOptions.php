<?php
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

namespace Twilio\Rest\FlexApi\V1\Credential;

use Twilio\Exceptions\TwilioException;
use Twilio\ListResource;
use Twilio\Options;
use Twilio\Stream;
use Twilio\Values;
use Twilio\Version;
use Twilio\InstanceContext;
use Twilio\Deserialize;
use Twilio\Serialize;


abstract class NewCredentialsOptions {
    /**
    * @param bool $testBoolean  
    * @param int $testInteger  
    * @param float $testNumber  
    * @param float $testNumberFloat  
    * @param float $testNumberDouble  
    * @param float $testNumberInt32  
    * @param int $testNumberInt64  
    * @param array $testObject  
    * @param \DateTime $testDateTime  
    * @param \DateTime $testDate  
    * @param string $testEnum  
    * @param array $testObjectArray  
    * @param array $testAnyType  
    * @param array $testAnyArray  
    * @param array $permissions A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;. 
    * @param string $someA2PThing  
    * @return CreateNewCredentialsOptions Options builder
    */
    public static function create(bool  $testBoolean=Values::NONE,int  $testInteger=Values::NONE,float  $testNumber=Values::NONE,float  $testNumberFloat=Values::NONE,float  $testNumberDouble=Values::NONE,float  $testNumberInt32=Values::NONE,int  $testNumberInt64=Values::NONE,array  $testObject=Values::NONE,\DateTime  $testDateTime=Values::NONE,\DateTime  $testDate=Values::NONE,string  $testEnum=Values::NONE,array  $testObjectArray=Values::ARRAY_NONE,array  $testAnyType=Values::NONE,array  $testAnyArray=Values::ARRAY_NONE,array  $permissions=Values::ARRAY_NONE,string  $someA2PThing=Values::NONE): CreateNewCredentialsOptions {
        return new CreateNewCredentialsOptions($testBoolean,$testInteger,$testNumber,$testNumberFloat,$testNumberDouble,$testNumberInt32,$testNumberInt64,$testObject,$testDateTime,$testDate,$testEnum,$testObjectArray,$testAnyType,$testAnyArray,$permissions,$someA2PThing);
    }

}

class CreateNewCredentialsOptions extends Options {
    /**
    * @param bool $testBoolean 
    * @param int $testInteger 
    * @param float $testNumber 
    * @param float $testNumberFloat 
    * @param float $testNumberDouble 
    * @param float $testNumberInt32 
    * @param int $testNumberInt64 
    * @param array $testObject 
    * @param \DateTime $testDateTime 
    * @param \DateTime $testDate 
    * @param string $testEnum 
    * @param array $testObjectArray 
    * @param array $testAnyType 
    * @param array $testAnyArray 
    * @param array $permissions A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;.
    * @param string $someA2PThing 
    */
    public function __construct(bool  $testBoolean=Values::NONE,int  $testInteger=Values::NONE,float  $testNumber=Values::NONE,float  $testNumberFloat=Values::NONE,float  $testNumberDouble=Values::NONE,float  $testNumberInt32=Values::NONE,int  $testNumberInt64=Values::NONE,array  $testObject=Values::NONE,\DateTime  $testDateTime=Values::NONE,\DateTime  $testDate=Values::NONE,string  $testEnum=Values::NONE,array  $testObjectArray=Values::ARRAY_NONE,array  $testAnyType=Values::NONE,array  $testAnyArray=Values::ARRAY_NONE,array  $permissions=Values::ARRAY_NONE,string  $someA2PThing=Values::NONE) {
        $this->options['testBoolean'] = $testBoolean;
        $this->options['testInteger'] = $testInteger;
        $this->options['testNumber'] = $testNumber;
        $this->options['testNumberFloat'] = $testNumberFloat;
        $this->options['testNumberDouble'] = $testNumberDouble;
        $this->options['testNumberInt32'] = $testNumberInt32;
        $this->options['testNumberInt64'] = $testNumberInt64;
        $this->options['testObject'] = $testObject;
        $this->options['testDateTime'] = $testDateTime;
        $this->options['testDate'] = $testDate;
        $this->options['testEnum'] = $testEnum;
        $this->options['testObjectArray'] = $testObjectArray;
        $this->options['testAnyType'] = $testAnyType;
        $this->options['testAnyArray'] = $testAnyArray;
        $this->options['permissions'] = $permissions;
        $this->options['someA2PThing'] = $someA2PThing;
    }

    /**
    * @param bool $testBoolean 
    * @return $this Fluent Builder
    */
    public function setTestBoolean(bool $testBoolean): self {
        $this->options['testBoolean'] = $testBoolean;
        return $this;
    }
    /**
    * @param int $testInteger 
    * @return $this Fluent Builder
    */
    public function setTestInteger(int $testInteger): self {
        $this->options['testInteger'] = $testInteger;
        return $this;
    }
    /**
    * @param float $testNumber 
    * @return $this Fluent Builder
    */
    public function setTestNumber(float $testNumber): self {
        $this->options['testNumber'] = $testNumber;
        return $this;
    }
    /**
    * @param float $testNumberFloat 
    * @return $this Fluent Builder
    */
    public function setTestNumberFloat(float $testNumberFloat): self {
        $this->options['testNumberFloat'] = $testNumberFloat;
        return $this;
    }
    /**
    * @param float $testNumberDouble 
    * @return $this Fluent Builder
    */
    public function setTestNumberDouble(float $testNumberDouble): self {
        $this->options['testNumberDouble'] = $testNumberDouble;
        return $this;
    }
    /**
    * @param float $testNumberInt32 
    * @return $this Fluent Builder
    */
    public function setTestNumberInt32(float $testNumberInt32): self {
        $this->options['testNumberInt32'] = $testNumberInt32;
        return $this;
    }
    /**
    * @param int $testNumberInt64 
    * @return $this Fluent Builder
    */
    public function setTestNumberInt64(int $testNumberInt64): self {
        $this->options['testNumberInt64'] = $testNumberInt64;
        return $this;
    }
    /**
    * @param array $testObject 
    * @return $this Fluent Builder
    */
    public function setTestObject(array $testObject): self {
        $this->options['testObject'] = $testObject;
        return $this;
    }
    /**
    * @param \DateTime $testDateTime 
    * @return $this Fluent Builder
    */
    public function setTestDateTime(\DateTime $testDateTime): self {
        $this->options['testDateTime'] = $testDateTime;
        return $this;
    }
    /**
    * @param \DateTime $testDate 
    * @return $this Fluent Builder
    */
    public function setTestDate(\DateTime $testDate): self {
        $this->options['testDate'] = $testDate;
        return $this;
    }
    /**
    * @param string $testEnum 
    * @return $this Fluent Builder
    */
    public function setTestEnum(string $testEnum): self {
        $this->options['testEnum'] = $testEnum;
        return $this;
    }
    /**
    * @param array $testObjectArray 
    * @return $this Fluent Builder
    */
    public function setTestObjectArray(array $testObjectArray): self {
        $this->options['testObjectArray'] = $testObjectArray;
        return $this;
    }
    /**
    * @param array $testAnyType 
    * @return $this Fluent Builder
    */
    public function setTestAnyType(array $testAnyType): self {
        $this->options['testAnyType'] = $testAnyType;
        return $this;
    }
    /**
    * @param array $testAnyArray 
    * @return $this Fluent Builder
    */
    public function setTestAnyArray(array $testAnyArray): self {
        $this->options['testAnyArray'] = $testAnyArray;
        return $this;
    }
    /**
    * @param array $permissions A comma-separated list of the permissions you will request from the users of this ConnectApp.  Can include: &#x60;get-all&#x60; and &#x60;post-all&#x60;.
    * @return $this Fluent Builder
    */
    public function setPermissions(array $permissions): self {
        $this->options['permissions'] = $permissions;
        return $this;
    }
    /**
    * @param string $someA2PThing 
    * @return $this Fluent Builder
    */
    public function setSomeA2PThing(string $someA2PThing): self {
        $this->options['someA2PThing'] = $someA2PThing;
        return $this;
    }

    /**
    * Provide a friendly representation
    *
    * @return string Machine friendly representation
    */
    public function __toString(): string {
        $options = \http_build_query(Values::of($this->options), '', ' ');
        return '[Twilio.FlexApi.V1.CreateNewCredentialsOptions ' . $options . ']';
    }
}

