FROM python:latest

ENV PYTHONBUFFERED 1

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-python.git
WORKDIR /app/twilio-python/
RUN git fetch && git checkout 8.0.0-rc && git pull

RUN rm -rf twilio/rest tests

COPY twilio/rest twilio/rest
COPY tests tests

# COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install pytest
RUN pytest tests

#CMD ["/bin/bash", "-c", "set -o pipefail && sleep 5 && ls -ltr && make test-install"]

