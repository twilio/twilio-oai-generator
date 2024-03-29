FROM php:7.4
COPY --from=composer:latest /usr/bin/composer /usr/local/bin/composer

RUN apt-get update -y && apt-get install -y git zip

ENV XDEBUG_MODE=coverage
ENV PATH="vendor/bin:$PATH"

RUN pecl install xdebug-2.9.0
RUN pecl install pcov && docker-php-ext-enable pcov

RUN git clone -b 7.0.0-rc https://github.com/twilio/twilio-php.git twilio
WORKDIR /twilio

RUN rm -rf src/Twilio/Rest/*
COPY src/Twilio/Rest src/Twilio/Rest

RUN rm -rf tests/Twilio/Integration
COPY tests/Twilio/Integration/Rest tests/Twilio/Integration/Rest

RUN make install

CMD ["/bin/bash", "-c", "phpunit -d memory_limit=512M --disallow-test-output --colors --configuration tests/phpunit.xml --coverage-clover=php_coverage.xml --log-junit=execution-result.xml 2>&1 | tee /local/test-report.out && cp php_coverage.xml execution-result.xml /local"]
