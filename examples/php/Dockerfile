FROM php:7.4

RUN apt-get update -y && apt-get install -y zip
RUN apt-get install -y git

RUN git clone https://github.com/twilio/twilio-php.git twilio
WORKDIR /twilio

ENV XDEBUG_MODE=coverage
ENV PATH="vendor/bin:$PATH"

RUN rm -rf src/Twilio/Rest/*/
COPY src/Twilio/Rest src/Twilio/Rest

RUN rm -rf tests/Twilio/Integration/
COPY tests/Twilio/Integration tests/Twilio/Integration

RUN rm -rf tests/Twilio/Unit/Rest
COPY tests/Twilio/Unit/Rest tests/Twilio/Unit/Rest

COPY --from=composer:latest /usr/bin/composer /usr/local/bin/composer
RUN composer install --prefer-dist

RUN pecl install xdebug

CMD ["/bin/bash", "-c", "phpunit -d memory_limit=512M --disallow-test-output --colors --configuration tests/phpunit.xml --coverage-clover=php_coverage.xml  --log-junit=execution-result.xml 2>&1 | tee /local/test-report.out && cp php_coverage.xml  execution-result.xml /local"]