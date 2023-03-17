FROM python:latest

ENV PYTHONBUFFERED 1

WORKDIR /app
RUN git clone https://github.com/twilio/twilio-python.git
WORKDIR /app/twilio-python/
#TODO: Change to main when GA
RUN git fetch && git checkout base-refactor && git pull ### Temp change for PR checks

RUN rm -rf tests/ twilio/rest/

COPY twilio twilio
COPY tests tests

# Install requirements
RUN pip install --no-cache-dir -r requirements.txt
RUN pip install pytest
RUN pip install pytz

# pipefail prevents errors in a pipeline from being masked
CMD ["/bin/bash", "-c", "set -o pipefail && sleep 10 && python -m pytest tests 2>&1 | tee /local/test-report.out"]

