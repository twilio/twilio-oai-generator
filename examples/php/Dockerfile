FROM php:7.4
COPY --from=composer:latest /usr/bin/composer /usr/local/bin/composer

RUN apt-get update -y && apt-get install -y git zip autoconf g++ make

ENV XDEBUG_MODE=coverage
ENV PATH="vendor/bin:$PATH"

RUN git clone --depth=1 --branch 3.1.6 https://github.com/xdebug/xdebug.git /tmp/xdebug \
    && cd /tmp/xdebug && phpize && ./configure && make && make install \
    && docker-php-ext-enable xdebug \
    && rm -rf /tmp/xdebug
RUN git clone --depth=1 --branch v1.0.12 https://github.com/krakjoe/pcov.git /tmp/pcov \
    && cd /tmp/pcov && phpize && ./configure --enable-pcov && make && make install \
    && docker-php-ext-enable pcov \
    && rm -rf /tmp/pcov

RUN git clone https://github.com/twilio/twilio-php.git twilio
WORKDIR /twilio

RUN rm -rf src/Twilio/Rest/*
COPY src/Twilio/Rest src/Twilio/Rest

RUN rm -rf tests/Twilio/Integration
COPY tests/Twilio/Integration/Rest tests/Twilio/Integration/Rest

RUN make install

CMD ["/bin/bash", "-c", "phpunit -d memory_limit=512M --disallow-test-output --colors --configuration tests/phpunit.xml --coverage-clover=php_coverage.xml --log-junit=execution-result.xml 2>&1 | tee /local/test-report.out && cp php_coverage.xml execution-result.xml /local"]
