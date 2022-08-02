FROM node:lts

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-node.git
WORKDIR /app/twilio-node/
RUN git fetch && git checkout 4.0.0-rc

RUN rm -rf lib/rest/ spec/

COPY lib lib
COPY spec spec

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && npm install && npm run test:javascript 2>&1 | tee /local/test-report.out && cp -r coverage /local"]
