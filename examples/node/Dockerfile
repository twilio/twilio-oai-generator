FROM node:lts

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-node.git
WORKDIR /app/twilio-node/
RUN git fetch && git checkout instance-refactor && git pull # to be reverted before merging

RUN rm -rf lib/rest/ spec/

COPY lib lib
COPY spec spec

# pipefail prevents errors in a pipeline from being masked.
CMD ["/bin/bash", "-c", "set -o pipefail && sleep 10 && make install test 2>&1 | tee /local/test-report.out && cp -r coverage /local"]
