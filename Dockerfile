FROM python:3.10-slim
ENTRYPOINT "python3", "-m", "flask", "run", "--host=0.0.0.0"
EXPOSE 5000
RUN pip3 install
WORKDIR /src
COPY . .