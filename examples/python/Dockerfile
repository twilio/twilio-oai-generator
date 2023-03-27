FROM python:latest

ENV PYTHONBUFFERED 1

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-python.git
WORKDIR /app/twilio-python/
#TODO: Change to main when GA
RUN git fetch && git checkout 8.0.0-rc && git pull

RUN rm -rf tests/ twilio/rest/ setup.cfg

COPY twilio twilio
COPY tests tests
COPY setup.cfg setup.cfg

# Install requirements
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install pytest-cov
RUN pip install pytz

# pipefail prevents errors in a pipeline from being masked
CMD ["/bin/bash", "-c", "set -o pipefail && sleep 10 && python -m pytest tests 2>&1 | tee /local/test-report.out && cp coverage.xml /local"]
