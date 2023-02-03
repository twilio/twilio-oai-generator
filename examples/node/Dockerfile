FROM node:lts

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-node.git
WORKDIR /app/twilio-node/

RUN rm -rf src/rest/ spec/

COPY src src
COPY spec spec

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && sleep 10 && make install test 2>&1 | tee /local/test-report.out && cp -r coverage /local"]
