FROM python:3.12-slim AS build
WORKDIR /build
COPY pyproject.toml ./
COPY app ./app
RUN pip install --no-cache-dir --prefix=/install .

FROM python:3.12-slim
WORKDIR /app
RUN useradd --system --uid 10001 app
COPY --from=build /install /usr/local
COPY app ./app
COPY alembic.ini ./
COPY migrations ./migrations
USER 10001
EXPOSE 8000
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
