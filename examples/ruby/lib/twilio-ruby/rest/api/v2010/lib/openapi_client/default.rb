=begin
#Twilio - Accounts

#This is the public Twilio REST API.

The version of the OpenAPI document: 1.11.0
Contact: support@twilio.com
Generated by: https://openapi-generator.tech
OpenAPI Generator version: 6.2.1

=end

require 'cgi'

module OpenapiClient
  class Default
    attr_accessor :api_client

    def initialize(api_client = ApiClient.default)
      @api_client = api_client
    end
    # @param [Hash] opts the optional parameters
    # @option opts [String] :x_twilio_webhook_enabled 
    # @option opts [String] :recording_status_callback 
    # @option opts [Array<String>] :recording_status_callback_event 
    # @return [TestResponseObject]
    def create_account(opts = {})
      data, _status_code, _headers = create_account_with_http_info(opts)
      data
    end

    # @param [Hash] opts the optional parameters
    # @option opts [String] :x_twilio_webhook_enabled 
    # @option opts [String] :recording_status_callback 
    # @option opts [Array<String>] :recording_status_callback_event 
    # @return [Array<(TestResponseObject, Integer, Hash)>] TestResponseObject data, response status code and response headers
    def create_account_with_http_info(opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.create_account ...'
      end
      allowable_values = ["true", "false"]
      if @api_client.config.client_side_validation && opts[:'x_twilio_webhook_enabled'] && !allowable_values.include?(opts[:'x_twilio_webhook_enabled'])
        fail ArgumentError, "invalid value for \"x_twilio_webhook_enabled\", must be one of #{allowable_values}"
      end
      # resource path
      local_var_path = '/2010-04-01/Accounts.json'

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])
      # HTTP header 'Content-Type'
      content_type = @api_client.select_header_content_type(['application/x-www-form-urlencoded'])
      if !content_type.nil?
          header_params['Content-Type'] = content_type
      end
      header_params[:'X-Twilio-Webhook-Enabled'] = opts[:'x_twilio_webhook_enabled'] if !opts[:'x_twilio_webhook_enabled'].nil?

      # form parameters
      form_params = opts[:form_params] || {}
      form_params['RecordingStatusCallback'] = opts[:'recording_status_callback'] if !opts[:'recording_status_callback'].nil?
      form_params['RecordingStatusCallbackEvent'] = @api_client.build_collection_param(opts[:'recording_status_callback_event'], :csv) if !opts[:'recording_status_callback_event'].nil?

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type] || 'TestResponseObject'

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.create_account",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:POST, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#create_account\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param account_sid [String] 
    # @param required_string_property [String] 
    # @param test_method [String] The HTTP method that we should use to request the &#x60;TestArrayOfUri&#x60;.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :test_array_of_strings 
    # @option opts [Array<String>] :test_array_of_uri 
    # @return [TestResponseObject]
    def create_call(account_sid, required_string_property, test_method, opts = {})
      data, _status_code, _headers = create_call_with_http_info(account_sid, required_string_property, test_method, opts)
      data
    end

    # @param account_sid [String] 
    # @param required_string_property [String] 
    # @param test_method [String] The HTTP method that we should use to request the &#x60;TestArrayOfUri&#x60;.
    # @param [Hash] opts the optional parameters
    # @option opts [Array<String>] :test_array_of_strings 
    # @option opts [Array<String>] :test_array_of_uri 
    # @return [Array<(TestResponseObject, Integer, Hash)>] TestResponseObject data, response status code and response headers
    def create_call_with_http_info(account_sid, required_string_property, test_method, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.create_call ...'
      end
      # verify the required parameter 'account_sid' is set
      if @api_client.config.client_side_validation && account_sid.nil?
        fail ArgumentError, "Missing the required parameter 'account_sid' when calling Default.create_call"
      end
      if @api_client.config.client_side_validation && account_sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.create_call, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && account_sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.create_call, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && account_sid !~ pattern
        fail ArgumentError, "invalid value for 'account_sid' when calling Default.create_call, must conform to the pattern #{pattern}."
      end

      # verify the required parameter 'required_string_property' is set
      if @api_client.config.client_side_validation && required_string_property.nil?
        fail ArgumentError, "Missing the required parameter 'required_string_property' when calling Default.create_call"
      end
      # verify the required parameter 'test_method' is set
      if @api_client.config.client_side_validation && test_method.nil?
        fail ArgumentError, "Missing the required parameter 'test_method' when calling Default.create_call"
      end
      # verify enum value
      allowable_values = ["HEAD", "GET", "POST", "PATCH", "PUT", "DELETE"]
      if @api_client.config.client_side_validation && !allowable_values.include?(test_method)
        fail ArgumentError, "invalid value for \"test_method\", must be one of #{allowable_values}"
      end
      # resource path
      local_var_path = '/2010-04-01/Accounts/{AccountSid}/Calls.json'.sub('{' + 'AccountSid' + '}', CGI.escape(account_sid.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])
      # HTTP header 'Content-Type'
      content_type = @api_client.select_header_content_type(['application/x-www-form-urlencoded'])
      if !content_type.nil?
          header_params['Content-Type'] = content_type
      end

      # form parameters
      form_params = opts[:form_params] || {}
      form_params['RequiredStringProperty'] = required_string_property
      form_params['TestMethod'] = test_method
      form_params['TestArrayOfStrings'] = @api_client.build_collection_param(opts[:'test_array_of_strings'], :csv) if !opts[:'test_array_of_strings'].nil?
      form_params['TestArrayOfUri'] = @api_client.build_collection_param(opts[:'test_array_of_uri'], :csv) if !opts[:'test_array_of_uri'].nil?

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type] || 'TestResponseObject'

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.create_call",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:POST, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#create_call\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param sid [String] 
    # @param [Hash] opts the optional parameters
    # @return [nil]
    def delete_account(sid, opts = {})
      delete_account_with_http_info(sid, opts)
      nil
    end

    # @param sid [String] 
    # @param [Hash] opts the optional parameters
    # @return [Array<(nil, Integer, Hash)>] nil, response status code and response headers
    def delete_account_with_http_info(sid, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.delete_account ...'
      end
      # verify the required parameter 'sid' is set
      if @api_client.config.client_side_validation && sid.nil?
        fail ArgumentError, "Missing the required parameter 'sid' when calling Default.delete_account"
      end
      if @api_client.config.client_side_validation && sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.delete_account, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.delete_account, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && sid !~ pattern
        fail ArgumentError, "invalid value for 'sid' when calling Default.delete_account, must conform to the pattern #{pattern}."
      end

      # resource path
      local_var_path = '/2010-04-01/Accounts/{Sid}.json'.sub('{' + 'Sid' + '}', CGI.escape(sid.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}

      # form parameters
      form_params = opts[:form_params] || {}

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type]

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.delete_account",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#delete_account\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param account_sid [String] 
    # @param test_integer [Integer] INTEGER ID param!!!
    # @param [Hash] opts the optional parameters
    # @return [nil]
    def delete_call(account_sid, test_integer, opts = {})
      delete_call_with_http_info(account_sid, test_integer, opts)
      nil
    end

    # @param account_sid [String] 
    # @param test_integer [Integer] INTEGER ID param!!!
    # @param [Hash] opts the optional parameters
    # @return [Array<(nil, Integer, Hash)>] nil, response status code and response headers
    def delete_call_with_http_info(account_sid, test_integer, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.delete_call ...'
      end
      # verify the required parameter 'account_sid' is set
      if @api_client.config.client_side_validation && account_sid.nil?
        fail ArgumentError, "Missing the required parameter 'account_sid' when calling Default.delete_call"
      end
      if @api_client.config.client_side_validation && account_sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.delete_call, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && account_sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.delete_call, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && account_sid !~ pattern
        fail ArgumentError, "invalid value for 'account_sid' when calling Default.delete_call, must conform to the pattern #{pattern}."
      end

      # verify the required parameter 'test_integer' is set
      if @api_client.config.client_side_validation && test_integer.nil?
        fail ArgumentError, "Missing the required parameter 'test_integer' when calling Default.delete_call"
      end
      # resource path
      local_var_path = '/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json'.sub('{' + 'AccountSid' + '}', CGI.escape(account_sid.to_s)).sub('{' + 'TestInteger' + '}', CGI.escape(test_integer.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}

      # form parameters
      form_params = opts[:form_params] || {}

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type]

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.delete_call",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:DELETE, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#delete_call\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param sid [String] 
    # @param [Hash] opts the optional parameters
    # @return [TestResponseObject]
    def fetch_account(sid, opts = {})
      data, _status_code, _headers = fetch_account_with_http_info(sid, opts)
      data
    end

    # @param sid [String] 
    # @param [Hash] opts the optional parameters
    # @return [Array<(TestResponseObject, Integer, Hash)>] TestResponseObject data, response status code and response headers
    def fetch_account_with_http_info(sid, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.fetch_account ...'
      end
      # verify the required parameter 'sid' is set
      if @api_client.config.client_side_validation && sid.nil?
        fail ArgumentError, "Missing the required parameter 'sid' when calling Default.fetch_account"
      end
      if @api_client.config.client_side_validation && sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.fetch_account, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.fetch_account, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && sid !~ pattern
        fail ArgumentError, "invalid value for 'sid' when calling Default.fetch_account, must conform to the pattern #{pattern}."
      end

      # resource path
      local_var_path = '/2010-04-01/Accounts/{Sid}.json'.sub('{' + 'Sid' + '}', CGI.escape(sid.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])

      # form parameters
      form_params = opts[:form_params] || {}

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type] || 'TestResponseObject'

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.fetch_account",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:GET, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#fetch_account\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param account_sid [String] 
    # @param test_integer [Integer] INTEGER ID param!!!
    # @param [Hash] opts the optional parameters
    # @return [nil]
    def fetch_call(account_sid, test_integer, opts = {})
      fetch_call_with_http_info(account_sid, test_integer, opts)
      nil
    end

    # @param account_sid [String] 
    # @param test_integer [Integer] INTEGER ID param!!!
    # @param [Hash] opts the optional parameters
    # @return [Array<(nil, Integer, Hash)>] nil, response status code and response headers
    def fetch_call_with_http_info(account_sid, test_integer, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.fetch_call ...'
      end
      # verify the required parameter 'account_sid' is set
      if @api_client.config.client_side_validation && account_sid.nil?
        fail ArgumentError, "Missing the required parameter 'account_sid' when calling Default.fetch_call"
      end
      if @api_client.config.client_side_validation && account_sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.fetch_call, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && account_sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.fetch_call, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && account_sid !~ pattern
        fail ArgumentError, "invalid value for 'account_sid' when calling Default.fetch_call, must conform to the pattern #{pattern}."
      end

      # verify the required parameter 'test_integer' is set
      if @api_client.config.client_side_validation && test_integer.nil?
        fail ArgumentError, "Missing the required parameter 'test_integer' when calling Default.fetch_call"
      end
      # resource path
      local_var_path = '/2010-04-01/Accounts/{AccountSid}/Calls/{TestInteger}.json'.sub('{' + 'AccountSid' + '}', CGI.escape(account_sid.to_s)).sub('{' + 'TestInteger' + '}', CGI.escape(test_integer.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])

      # form parameters
      form_params = opts[:form_params] || {}

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type]

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.fetch_call",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:GET, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#fetch_call\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param sid [String] 
    # @param status [TestEnumStatus] 
    # @param [Hash] opts the optional parameters
    # @option opts [String] :pause_behavior 
    # @return [TestResponseObject]
    def update_account(sid, status, opts = {})
      data, _status_code, _headers = update_account_with_http_info(sid, status, opts)
      data
    end

    # @param sid [String] 
    # @param status [TestEnumStatus] 
    # @param [Hash] opts the optional parameters
    # @option opts [String] :pause_behavior 
    # @return [Array<(TestResponseObject, Integer, Hash)>] TestResponseObject data, response status code and response headers
    def update_account_with_http_info(sid, status, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.update_account ...'
      end
      # verify the required parameter 'sid' is set
      if @api_client.config.client_side_validation && sid.nil?
        fail ArgumentError, "Missing the required parameter 'sid' when calling Default.update_account"
      end
      if @api_client.config.client_side_validation && sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.update_account, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "sid" when calling Default.update_account, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && sid !~ pattern
        fail ArgumentError, "invalid value for 'sid' when calling Default.update_account, must conform to the pattern #{pattern}."
      end

      # verify the required parameter 'status' is set
      if @api_client.config.client_side_validation && status.nil?
        fail ArgumentError, "Missing the required parameter 'status' when calling Default.update_account"
      end
      # resource path
      local_var_path = '/2010-04-01/Accounts/{Sid}.json'.sub('{' + 'Sid' + '}', CGI.escape(sid.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])
      # HTTP header 'Content-Type'
      content_type = @api_client.select_header_content_type(['application/x-www-form-urlencoded'])
      if !content_type.nil?
          header_params['Content-Type'] = content_type
      end

      # form parameters
      form_params = opts[:form_params] || {}
      form_params['Status'] = status
      form_params['PauseBehavior'] = opts[:'pause_behavior'] if !opts[:'pause_behavior'].nil?

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type] || 'TestResponseObject'

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.update_account",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:POST, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#update_account\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end

    # @param account_sid [String] 
    # @param sid [String] 
    # @param end_date [Date] 
    # @param start_date [Date] 
    # @param [Hash] opts the optional parameters
    # @option opts [String] :account_sid 
    # @return [TestResponseObject]
    def update_call_feedback_summary(account_sid, sid, end_date, start_date, opts = {})
      data, _status_code, _headers = update_call_feedback_summary_with_http_info(account_sid, sid, end_date, start_date, opts)
      data
    end

    # @param account_sid [String] 
    # @param sid [String] 
    # @param end_date [Date] 
    # @param start_date [Date] 
    # @param [Hash] opts the optional parameters
    # @option opts [String] :account_sid 
    # @return [Array<(TestResponseObject, Integer, Hash)>] TestResponseObject data, response status code and response headers
    def update_call_feedback_summary_with_http_info(account_sid, sid, end_date, start_date, opts = {})
      if @api_client.config.debugging
        @api_client.config.logger.debug 'Calling API: Default.update_call_feedback_summary ...'
      end
      # verify the required parameter 'account_sid' is set
      if @api_client.config.client_side_validation && account_sid.nil?
        fail ArgumentError, "Missing the required parameter 'account_sid' when calling Default.update_call_feedback_summary"
      end
      if @api_client.config.client_side_validation && account_sid.to_s.length > 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.update_call_feedback_summary, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && account_sid.to_s.length < 34
        fail ArgumentError, 'invalid value for "account_sid" when calling Default.update_call_feedback_summary, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && account_sid !~ pattern
        fail ArgumentError, "invalid value for 'account_sid' when calling Default.update_call_feedback_summary, must conform to the pattern #{pattern}."
      end

      # verify the required parameter 'sid' is set
      if @api_client.config.client_side_validation && sid.nil?
        fail ArgumentError, "Missing the required parameter 'sid' when calling Default.update_call_feedback_summary"
      end
      # verify the required parameter 'end_date' is set
      if @api_client.config.client_side_validation && end_date.nil?
        fail ArgumentError, "Missing the required parameter 'end_date' when calling Default.update_call_feedback_summary"
      end
      # verify the required parameter 'start_date' is set
      if @api_client.config.client_side_validation && start_date.nil?
        fail ArgumentError, "Missing the required parameter 'start_date' when calling Default.update_call_feedback_summary"
      end
      if @api_client.config.client_side_validation && !opts[:'account_sid'].nil? && opts[:'account_sid'].to_s.length > 34
        fail ArgumentError, 'invalid value for "opts[:"account_sid"]" when calling Default.update_call_feedback_summary, the character length must be smaller than or equal to 34.'
      end

      if @api_client.config.client_side_validation && !opts[:'account_sid'].nil? && opts[:'account_sid'].to_s.length < 34
        fail ArgumentError, 'invalid value for "opts[:"account_sid"]" when calling Default.update_call_feedback_summary, the character length must be great than or equal to 34.'
      end

      pattern = Regexp.new(/^AC[0-9a-fA-F]{32}$/)
      if @api_client.config.client_side_validation && !opts[:'account_sid'].nil? && opts[:'account_sid'] !~ pattern
        fail ArgumentError, "invalid value for 'opts[:\"account_sid\"]' when calling Default.update_call_feedback_summary, must conform to the pattern #{pattern}."
      end

      # resource path
      local_var_path = '/2010-04-01/Accounts/{AccountSid}/Calls/Feedback/Summary/{Sid}.json'.sub('{' + 'AccountSid' + '}', CGI.escape(account_sid.to_s)).sub('{' + 'Sid' + '}', CGI.escape(sid.to_s))

      # query parameters
      query_params = opts[:query_params] || {}

      # header parameters
      header_params = opts[:header_params] || {}
      # HTTP header 'Accept' (if needed)
      header_params['Accept'] = @api_client.select_header_accept(['application/json'])
      # HTTP header 'Content-Type'
      content_type = @api_client.select_header_content_type(['application/x-www-form-urlencoded'])
      if !content_type.nil?
          header_params['Content-Type'] = content_type
      end

      # form parameters
      form_params = opts[:form_params] || {}
      form_params['EndDate'] = end_date
      form_params['StartDate'] = start_date
      form_params['AccountSid'] = opts[:'account_sid'] if !opts[:'account_sid'].nil?

      # http body (model)
      post_body = opts[:debug_body]

      # return_type
      return_type = opts[:debug_return_type] || 'TestResponseObject'

      # auth_names
      auth_names = opts[:debug_auth_names] || ['accountSid_authToken']

      new_options = opts.merge(
        :operation => :"Default.update_call_feedback_summary",
        :header_params => header_params,
        :query_params => query_params,
        :form_params => form_params,
        :body => post_body,
        :auth_names => auth_names,
        :return_type => return_type
      )

      data, status_code, headers = @api_client.call_api(:POST, local_var_path, new_options)
      if @api_client.config.debugging
        @api_client.config.logger.debug "API called: Default#update_call_feedback_summary\nData: #{data.inspect}\nStatus code: #{status_code}\nHeaders: #{headers}"
      end
      return data, status_code, headers
    end
  end
end
