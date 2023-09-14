This section explains the relationship between a parameter, property, model
1. Request type: urlencoded
application/x-www-form-urlencoded:
  schema:
    type: object
    title: CreateMessageRequest
    properties:
      To:
      StatusCallback:
      ApplicationSid:
      MaxPrice:

2: Request type: json
application/json:
  schema:
    type: object
    title: CreateMessageRequest
    properties:
      To:
      StatusCallback:
      ApplicationSid:
      MaxPrice:

1. Parameter
In case of Request type is urlencoded, then each item inside request body is considered as codegenparameter(To, 
StatusCallback, ApplicationSid,MaxPrice).
In case of Request type is json, then there will be only one codegenparameter(CreateMessageRequest)
Each property will be present as codegenproperty(To, StatusCallback, ApplicationSid,MaxPrice).
A parameter can have multiple properties.
A parameter can be a model if it is referenced in component section.

2. Property
A parameter can have multiple properties. A property can be a model if it is referenced in component section.

3. Model
Any object defined inside component schema is a model. Now a model can have multiple properties.
Parameter can be a model
Property can be a model
