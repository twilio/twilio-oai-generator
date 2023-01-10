FROM python:latest

ENV PYTHONBUFFERED 1

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-python.git
WORKDIR /app/twilio-python/
RUN git fetch && git checkout DI-2452 && git pull


ENV VIRTUAL_ENV=/opt/venv
RUN python3 -m venv $VIRTUAL_ENV
ENV PATH="$VIRTUAL_ENV/bin:$PATH"

RUN rm -rf twilio/rest tests

COPY lib/rest rest
COPY tests tests

CMD ["/bin/bash", "-c", "set -o pipefail && sleep 5 && ls -ltr && make test-install"]

