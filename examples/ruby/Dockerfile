FROM ruby:2.7

RUN apt-get update -y && apt-get install -y git

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-ruby.git
WORKDIR /app/twilio-ruby/


# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && sleep 10 && make install test 2>&1 | tee /local/test-report.out"]