<?php
/**
 * TestResponseObject
 *
 * PHP version 7.4
 *
 * @category Class
 * @package  OpenAPI\Client
 * @author   OpenAPI Generator team
 * @link     https://openapi-generator.tech
 */

/**
 * Twilio - Accounts
 *
 * This is the public Twilio REST API.
 *
 * The version of the OpenAPI document: 1.11.0
 * Contact: support@twilio.com
 * Generated by: https://openapi-generator.tech
 * OpenAPI Generator version: 6.2.0
 */

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

namespace OpenAPI\Client\Model;

use \ArrayAccess;
use \OpenAPI\Client\ObjectSerializer;

/**
 * TestResponseObject Class Doc Comment
 *
 * @category Class
 * @package  OpenAPI\Client
 * @author   OpenAPI Generator team
 * @link     https://openapi-generator.tech
 * @implements \ArrayAccess<string, mixed>
 */
class TestResponseObject implements ModelInterface, ArrayAccess, \JsonSerializable
{
    public const DISCRIMINATOR = null;

    /**
      * The original name of the model.
      *
      * @var string
      */
    protected static $openAPIModelName = 'test.response_object';

    /**
      * Array of property to type mappings. Used for (de)serialization
      *
      * @var string[]
      */
    protected static $openAPITypes = [
        'account_sid' => 'string',
        'sid' => 'string',
        'test_string' => 'string',
        'test_integer' => 'int',
        'test_object' => '\OpenAPI\Client\Model\TestResponseObjectTestObject',
        'test_date_time' => 'string',
        'test_number' => 'float',
        'price_unit' => 'string',
        'test_number_float' => 'float',
        'test_enum' => '\OpenAPI\Client\Model\TestEnumStatus',
        'a2p_profile_bundle_sid' => 'string',
        'test_array_of_integers' => 'int[]',
        'test_array_of_array_of_integers' => 'int[][]',
        'test_array_of_objects' => '\OpenAPI\Client\Model\TestResponseObjectTestArrayOfObjects[]',
        'test_array_of_enum' => '\OpenAPI\Client\Model\TestEnumStatus[]'
    ];

    /**
      * Array of property to format mappings. Used for (de)serialization
      *
      * @var string[]
      * @phpstan-var array<string, string|null>
      * @psalm-var array<string, string|null>
      */
    protected static $openAPIFormats = [
        'account_sid' => 'foobar',
        'sid' => null,
        'test_string' => null,
        'test_integer' => null,
        'test_object' => null,
        'test_date_time' => 'date-time-rfc-2822',
        'test_number' => null,
        'price_unit' => 'currency',
        'test_number_float' => 'float',
        'test_enum' => null,
        'a2p_profile_bundle_sid' => null,
        'test_array_of_integers' => null,
        'test_array_of_array_of_integers' => null,
        'test_array_of_objects' => null,
        'test_array_of_enum' => null
    ];

    /**
      * Array of nullable properties. Used for (de)serialization
      *
      * @var boolean[]
      */
    protected static array $openAPINullables = [
        'account_sid' => true,
		'sid' => true,
		'test_string' => true,
		'test_integer' => true,
		'test_object' => true,
		'test_date_time' => true,
		'test_number' => true,
		'price_unit' => true,
		'test_number_float' => true,
		'test_enum' => false,
		'a2p_profile_bundle_sid' => true,
		'test_array_of_integers' => false,
		'test_array_of_array_of_integers' => false,
		'test_array_of_objects' => true,
		'test_array_of_enum' => true
    ];

    /**
      * If a nullable field gets set to null, insert it here
      *
      * @var boolean[]
      */
    protected array $openAPINullablesSetToNull = [];

    /**
     * Array of property to type mappings. Used for (de)serialization
     *
     * @return array
     */
    public static function openAPITypes()
    {
        return self::$openAPITypes;
    }

    /**
     * Array of property to format mappings. Used for (de)serialization
     *
     * @return array
     */
    public static function openAPIFormats()
    {
        return self::$openAPIFormats;
    }

    /**
     * Array of nullable properties
     *
     * @return array
     */
    protected static function openAPINullables(): array
    {
        return self::$openAPINullables;
    }

    /**
     * Array of nullable field names deliberately set to null
     *
     * @return boolean[]
     */
    private function getOpenAPINullablesSetToNull(): array
    {
        return $this->openAPINullablesSetToNull;
    }

    /**
     * Checks if a property is nullable
     *
     * @param string $property
     * @return bool
     */
    public static function isNullable(string $property): bool
    {
        return self::openAPINullables()[$property] ?? false;
    }

    /**
     * Checks if a nullable property is set to null.
     *
     * @param string $property
     * @return bool
     */
    public function isNullableSetToNull(string $property): bool
    {
        return in_array($property, $this->getOpenAPINullablesSetToNull(), true);
    }

    /**
     * Array of attributes where the key is the local name,
     * and the value is the original name
     *
     * @var string[]
     */
    protected static $attributeMap = [
        'account_sid' => 'account_sid',
        'sid' => 'sid',
        'test_string' => 'test_string',
        'test_integer' => 'test_integer',
        'test_object' => 'test_object',
        'test_date_time' => 'test_date_time',
        'test_number' => 'test_number',
        'price_unit' => 'price_unit',
        'test_number_float' => 'test_number_float',
        'test_enum' => 'test_enum',
        'a2p_profile_bundle_sid' => 'a2p_profile_bundle_sid',
        'test_array_of_integers' => 'test_array_of_integers',
        'test_array_of_array_of_integers' => 'test_array_of_array_of_integers',
        'test_array_of_objects' => 'test_array_of_objects',
        'test_array_of_enum' => 'test_array_of_enum'
    ];

    /**
     * Array of attributes to setter functions (for deserialization of responses)
     *
     * @var string[]
     */
    protected static $setters = [
        'account_sid' => 'setAccountSid',
        'sid' => 'setSid',
        'test_string' => 'setTestString',
        'test_integer' => 'setTestInteger',
        'test_object' => 'setTestObject',
        'test_date_time' => 'setTestDateTime',
        'test_number' => 'setTestNumber',
        'price_unit' => 'setPriceUnit',
        'test_number_float' => 'setTestNumberFloat',
        'test_enum' => 'setTestEnum',
        'a2p_profile_bundle_sid' => 'setA2pProfileBundleSid',
        'test_array_of_integers' => 'setTestArrayOfIntegers',
        'test_array_of_array_of_integers' => 'setTestArrayOfArrayOfIntegers',
        'test_array_of_objects' => 'setTestArrayOfObjects',
        'test_array_of_enum' => 'setTestArrayOfEnum'
    ];

    /**
     * Array of attributes to getter functions (for serialization of requests)
     *
     * @var string[]
     */
    protected static $getters = [
        'account_sid' => 'getAccountSid',
        'sid' => 'getSid',
        'test_string' => 'getTestString',
        'test_integer' => 'getTestInteger',
        'test_object' => 'getTestObject',
        'test_date_time' => 'getTestDateTime',
        'test_number' => 'getTestNumber',
        'price_unit' => 'getPriceUnit',
        'test_number_float' => 'getTestNumberFloat',
        'test_enum' => 'getTestEnum',
        'a2p_profile_bundle_sid' => 'getA2pProfileBundleSid',
        'test_array_of_integers' => 'getTestArrayOfIntegers',
        'test_array_of_array_of_integers' => 'getTestArrayOfArrayOfIntegers',
        'test_array_of_objects' => 'getTestArrayOfObjects',
        'test_array_of_enum' => 'getTestArrayOfEnum'
    ];

    /**
     * Array of attributes where the key is the local name,
     * and the value is the original name
     *
     * @return array
     */
    public static function attributeMap()
    {
        return self::$attributeMap;
    }

    /**
     * Array of attributes to setter functions (for deserialization of responses)
     *
     * @return array
     */
    public static function setters()
    {
        return self::$setters;
    }

    /**
     * Array of attributes to getter functions (for serialization of requests)
     *
     * @return array
     */
    public static function getters()
    {
        return self::$getters;
    }

    /**
     * The original name of the model.
     *
     * @return string
     */
    public function getModelName()
    {
        return self::$openAPIModelName;
    }


    /**
     * Associative array for storing property values
     *
     * @var mixed[]
     */
    protected $container = [];

    /**
     * Constructor
     *
     * @param mixed[] $data Associated array of property values
     *                      initializing the model
     */
    public function __construct(array $data = null)
    {
        $this->setIfExists('account_sid', $data ?? [], null);
        $this->setIfExists('sid', $data ?? [], null);
        $this->setIfExists('test_string', $data ?? [], null);
        $this->setIfExists('test_integer', $data ?? [], null);
        $this->setIfExists('test_object', $data ?? [], null);
        $this->setIfExists('test_date_time', $data ?? [], null);
        $this->setIfExists('test_number', $data ?? [], null);
        $this->setIfExists('price_unit', $data ?? [], null);
        $this->setIfExists('test_number_float', $data ?? [], null);
        $this->setIfExists('test_enum', $data ?? [], null);
        $this->setIfExists('a2p_profile_bundle_sid', $data ?? [], null);
        $this->setIfExists('test_array_of_integers', $data ?? [], null);
        $this->setIfExists('test_array_of_array_of_integers', $data ?? [], null);
        $this->setIfExists('test_array_of_objects', $data ?? [], null);
        $this->setIfExists('test_array_of_enum', $data ?? [], null);
    }

    /**
    * Sets $this->container[$variableName] to the given data or to the given default Value; if $variableName
    * is nullable and its value is set to null in the $fields array, then mark it as "set to null" in the
    * $this->openAPINullablesSetToNull array
    *
    * @param string $variableName
    * @param array  $fields
    * @param mixed  $defaultValue
    */
    private function setIfExists(string $variableName, array $fields, $defaultValue): void
    {
        if (self::isNullable($variableName) && array_key_exists($variableName, $fields) && is_null($fields[$variableName])) {
            $this->openAPINullablesSetToNull[] = $variableName;
        }

        $this->container[$variableName] = $fields[$variableName] ?? $defaultValue;
    }

    /**
     * Show all the invalid properties with reasons.
     *
     * @return array invalid properties with reasons
     */
    public function listInvalidProperties()
    {
        $invalidProperties = [];

        if (!is_null($this->container['a2p_profile_bundle_sid']) && (mb_strlen($this->container['a2p_profile_bundle_sid']) > 34)) {
            $invalidProperties[] = "invalid value for 'a2p_profile_bundle_sid', the character length must be smaller than or equal to 34.";
        }

        if (!is_null($this->container['a2p_profile_bundle_sid']) && (mb_strlen($this->container['a2p_profile_bundle_sid']) < 34)) {
            $invalidProperties[] = "invalid value for 'a2p_profile_bundle_sid', the character length must be bigger than or equal to 34.";
        }

        if (!is_null($this->container['a2p_profile_bundle_sid']) && !preg_match("/^BU[0-9a-fA-F]{32}$/", $this->container['a2p_profile_bundle_sid'])) {
            $invalidProperties[] = "invalid value for 'a2p_profile_bundle_sid', must be conform to the pattern /^BU[0-9a-fA-F]{32}$/.";
        }

        return $invalidProperties;
    }

    /**
     * Validate all the properties in the model
     * return true if all passed
     *
     * @return bool True if all properties are valid
     */
    public function valid()
    {
        return count($this->listInvalidProperties()) === 0;
    }


    /**
     * Gets account_sid
     *
     * @return string|null
     */
    public function getAccountSid()
    {
        return $this->container['account_sid'];
    }

    /**
     * Sets account_sid
     *
     * @param string|null $account_sid account_sid
     *
     * @return self
     */
    public function setAccountSid($account_sid)
    {

        if (is_null($account_sid)) {
            array_push($this->openAPINullablesSetToNull, 'account_sid');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('account_sid', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['account_sid'] = $account_sid;

        return $this;
    }

    /**
     * Gets sid
     *
     * @return string|null
     */
    public function getSid()
    {
        return $this->container['sid'];
    }

    /**
     * Sets sid
     *
     * @param string|null $sid sid
     *
     * @return self
     */
    public function setSid($sid)
    {

        if (is_null($sid)) {
            array_push($this->openAPINullablesSetToNull, 'sid');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('sid', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['sid'] = $sid;

        return $this;
    }

    /**
     * Gets test_string
     *
     * @return string|null
     */
    public function getTestString()
    {
        return $this->container['test_string'];
    }

    /**
     * Sets test_string
     *
     * @param string|null $test_string test_string
     *
     * @return self
     */
    public function setTestString($test_string)
    {

        if (is_null($test_string)) {
            array_push($this->openAPINullablesSetToNull, 'test_string');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_string', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_string'] = $test_string;

        return $this;
    }

    /**
     * Gets test_integer
     *
     * @return int|null
     */
    public function getTestInteger()
    {
        return $this->container['test_integer'];
    }

    /**
     * Sets test_integer
     *
     * @param int|null $test_integer test_integer
     *
     * @return self
     */
    public function setTestInteger($test_integer)
    {

        if (is_null($test_integer)) {
            array_push($this->openAPINullablesSetToNull, 'test_integer');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_integer', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_integer'] = $test_integer;

        return $this;
    }

    /**
     * Gets test_object
     *
     * @return \OpenAPI\Client\Model\TestResponseObjectTestObject|null
     */
    public function getTestObject()
    {
        return $this->container['test_object'];
    }

    /**
     * Sets test_object
     *
     * @param \OpenAPI\Client\Model\TestResponseObjectTestObject|null $test_object test_object
     *
     * @return self
     */
    public function setTestObject($test_object)
    {

        if (is_null($test_object)) {
            array_push($this->openAPINullablesSetToNull, 'test_object');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_object', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_object'] = $test_object;

        return $this;
    }

    /**
     * Gets test_date_time
     *
     * @return string|null
     */
    public function getTestDateTime()
    {
        return $this->container['test_date_time'];
    }

    /**
     * Sets test_date_time
     *
     * @param string|null $test_date_time test_date_time
     *
     * @return self
     */
    public function setTestDateTime($test_date_time)
    {

        if (is_null($test_date_time)) {
            array_push($this->openAPINullablesSetToNull, 'test_date_time');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_date_time', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_date_time'] = $test_date_time;

        return $this;
    }

    /**
     * Gets test_number
     *
     * @return float|null
     */
    public function getTestNumber()
    {
        return $this->container['test_number'];
    }

    /**
     * Sets test_number
     *
     * @param float|null $test_number test_number
     *
     * @return self
     */
    public function setTestNumber($test_number)
    {

        if (is_null($test_number)) {
            array_push($this->openAPINullablesSetToNull, 'test_number');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_number', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_number'] = $test_number;

        return $this;
    }

    /**
     * Gets price_unit
     *
     * @return string|null
     */
    public function getPriceUnit()
    {
        return $this->container['price_unit'];
    }

    /**
     * Sets price_unit
     *
     * @param string|null $price_unit price_unit
     *
     * @return self
     */
    public function setPriceUnit($price_unit)
    {

        if (is_null($price_unit)) {
            array_push($this->openAPINullablesSetToNull, 'price_unit');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('price_unit', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['price_unit'] = $price_unit;

        return $this;
    }

    /**
     * Gets test_number_float
     *
     * @return float|null
     */
    public function getTestNumberFloat()
    {
        return $this->container['test_number_float'];
    }

    /**
     * Sets test_number_float
     *
     * @param float|null $test_number_float test_number_float
     *
     * @return self
     */
    public function setTestNumberFloat($test_number_float)
    {

        if (is_null($test_number_float)) {
            array_push($this->openAPINullablesSetToNull, 'test_number_float');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_number_float', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_number_float'] = $test_number_float;

        return $this;
    }

    /**
     * Gets test_enum
     *
     * @return \OpenAPI\Client\Model\TestEnumStatus|null
     */
    public function getTestEnum()
    {
        return $this->container['test_enum'];
    }

    /**
     * Sets test_enum
     *
     * @param \OpenAPI\Client\Model\TestEnumStatus|null $test_enum test_enum
     *
     * @return self
     */
    public function setTestEnum($test_enum)
    {

        if (is_null($test_enum)) {
            throw new \InvalidArgumentException('non-nullable test_enum cannot be null');
        }

        $this->container['test_enum'] = $test_enum;

        return $this;
    }

    /**
     * Gets a2p_profile_bundle_sid
     *
     * @return string|null
     */
    public function getA2pProfileBundleSid()
    {
        return $this->container['a2p_profile_bundle_sid'];
    }

    /**
     * Sets a2p_profile_bundle_sid
     *
     * @param string|null $a2p_profile_bundle_sid A2P Messaging Profile Bundle BundleSid
     *
     * @return self
     */
    public function setA2pProfileBundleSid($a2p_profile_bundle_sid)
    {
        if (!is_null($a2p_profile_bundle_sid) && (mb_strlen($a2p_profile_bundle_sid) > 34)) {
            throw new \InvalidArgumentException('invalid length for $a2p_profile_bundle_sid when calling TestResponseObject., must be smaller than or equal to 34.');
        }
        if (!is_null($a2p_profile_bundle_sid) && (mb_strlen($a2p_profile_bundle_sid) < 34)) {
            throw new \InvalidArgumentException('invalid length for $a2p_profile_bundle_sid when calling TestResponseObject., must be bigger than or equal to 34.');
        }
        if (!is_null($a2p_profile_bundle_sid) && (!preg_match("/^BU[0-9a-fA-F]{32}$/", $a2p_profile_bundle_sid))) {
            throw new \InvalidArgumentException("invalid value for \$a2p_profile_bundle_sid when calling TestResponseObject., must conform to the pattern /^BU[0-9a-fA-F]{32}$/.");
        }


        if (is_null($a2p_profile_bundle_sid)) {
            array_push($this->openAPINullablesSetToNull, 'a2p_profile_bundle_sid');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('a2p_profile_bundle_sid', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['a2p_profile_bundle_sid'] = $a2p_profile_bundle_sid;

        return $this;
    }

    /**
     * Gets test_array_of_integers
     *
     * @return int[]|null
     */
    public function getTestArrayOfIntegers()
    {
        return $this->container['test_array_of_integers'];
    }

    /**
     * Sets test_array_of_integers
     *
     * @param int[]|null $test_array_of_integers test_array_of_integers
     *
     * @return self
     */
    public function setTestArrayOfIntegers($test_array_of_integers)
    {

        if (is_null($test_array_of_integers)) {
            throw new \InvalidArgumentException('non-nullable test_array_of_integers cannot be null');
        }

        $this->container['test_array_of_integers'] = $test_array_of_integers;

        return $this;
    }

    /**
     * Gets test_array_of_array_of_integers
     *
     * @return int[][]|null
     */
    public function getTestArrayOfArrayOfIntegers()
    {
        return $this->container['test_array_of_array_of_integers'];
    }

    /**
     * Sets test_array_of_array_of_integers
     *
     * @param int[][]|null $test_array_of_array_of_integers test_array_of_array_of_integers
     *
     * @return self
     */
    public function setTestArrayOfArrayOfIntegers($test_array_of_array_of_integers)
    {

        if (is_null($test_array_of_array_of_integers)) {
            throw new \InvalidArgumentException('non-nullable test_array_of_array_of_integers cannot be null');
        }

        $this->container['test_array_of_array_of_integers'] = $test_array_of_array_of_integers;

        return $this;
    }

    /**
     * Gets test_array_of_objects
     *
     * @return \OpenAPI\Client\Model\TestResponseObjectTestArrayOfObjects[]|null
     */
    public function getTestArrayOfObjects()
    {
        return $this->container['test_array_of_objects'];
    }

    /**
     * Sets test_array_of_objects
     *
     * @param \OpenAPI\Client\Model\TestResponseObjectTestArrayOfObjects[]|null $test_array_of_objects test_array_of_objects
     *
     * @return self
     */
    public function setTestArrayOfObjects($test_array_of_objects)
    {

        if (is_null($test_array_of_objects)) {
            array_push($this->openAPINullablesSetToNull, 'test_array_of_objects');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_array_of_objects', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_array_of_objects'] = $test_array_of_objects;

        return $this;
    }

    /**
     * Gets test_array_of_enum
     *
     * @return \OpenAPI\Client\Model\TestEnumStatus[]|null
     */
    public function getTestArrayOfEnum()
    {
        return $this->container['test_array_of_enum'];
    }

    /**
     * Sets test_array_of_enum
     *
     * @param \OpenAPI\Client\Model\TestEnumStatus[]|null $test_array_of_enum Permissions authorized to the app
     *
     * @return self
     */
    public function setTestArrayOfEnum($test_array_of_enum)
    {

        if (is_null($test_array_of_enum)) {
            array_push($this->openAPINullablesSetToNull, 'test_array_of_enum');
        } else {
            $nullablesSetToNull = $this->getOpenAPINullablesSetToNull();
            $index = array_search('test_array_of_enum', $nullablesSetToNull);
            if ($index !== FALSE) {
                unset($nullablesSetToNull[$index]);
                $this->setOpenAPINullablesSetToNull($nullablesSetToNull);
            }
        }

        $this->container['test_array_of_enum'] = $test_array_of_enum;

        return $this;
    }
    /**
     * Returns true if offset exists. False otherwise.
     *
     * @param integer $offset Offset
     *
     * @return boolean
     */
    public function offsetExists($offset): bool
    {
        return isset($this->container[$offset]);
    }

    /**
     * Gets offset.
     *
     * @param integer $offset Offset
     *
     * @return mixed|null
     */
    #[\ReturnTypeWillChange]
    public function offsetGet($offset)
    {
        return $this->container[$offset] ?? null;
    }

    /**
     * Sets value based on offset.
     *
     * @param int|null $offset Offset
     * @param mixed    $value  Value to be set
     *
     * @return void
     */
    public function offsetSet($offset, $value): void
    {
        if (is_null($offset)) {
            $this->container[] = $value;
        } else {
            $this->container[$offset] = $value;
        }
    }

    /**
     * Unsets offset.
     *
     * @param integer $offset Offset
     *
     * @return void
     */
    public function offsetUnset($offset): void
    {
        unset($this->container[$offset]);
    }

    /**
     * Serializes the object to a value that can be serialized natively by json_encode().
     * @link https://www.php.net/manual/en/jsonserializable.jsonserialize.php
     *
     * @return mixed Returns data which can be serialized by json_encode(), which is a value
     * of any type other than a resource.
     */
    #[\ReturnTypeWillChange]
    public function jsonSerialize()
    {
       return ObjectSerializer::sanitizeForSerialization($this);
    }

    /**
     * Gets the string presentation of the object
     *
     * @return string
     */
    public function __toString()
    {
        return json_encode(
            ObjectSerializer::sanitizeForSerialization($this),
            JSON_PRETTY_PRINT
        );
    }

    /**
     * Gets a header-safe presentation of the object
     *
     * @return string
     */
    public function toHeaderValue()
    {
        return json_encode(ObjectSerializer::sanitizeForSerialization($this));
    }
}


