# Caching-Proxy

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-backend-brightgreen)
![Redis](https://img.shields.io/badge/Redis-cache-red)
![Docker](https://img.shields.io/badge/Docker-compose-blue)

A backend service that sits in front of another API, caches its responses in **Redis**, and serves repeat requests instantly without contacting the original server.

Built with **Spring Boot**, **Redis** and **Docker Compose**. Based on the [roadmap.sh Caching Proxy project](https://roadmap.sh/projects/caching-server).

---

## Overview

Calling the same API repeatedly is slow and wasteful. This proxy fixes that:

- The **first** request is forwarded to the origin server, and the response is saved in Redis.
- **Repeat** requests are answered directly from Redis until the cache expires.
- Every response includes an `X-Cache` header, so you can see where it came from.

| Header | Meaning |
|---|---|
| `X-Cache: MISS` | Response came from the origin server |
| `X-Cache: HIT` | Response came from the Redis cache |

## How it works

```
                 ┌──────────────┐
 Client ───────► │ Caching Proxy│
                 └──────┬───────┘
                        │ 1. Is it in Redis?
              ┌─────────┴──────────┐
         YES  ▼                    ▼  NO
       Return cached         Forward to origin,
       response (HIT)        store in Redis,
                             return response (MISS)
```

Cached entries expire automatically after a set time (TTL), so data never stays stale for long.

## Tech stack

| Layer | Technology |
|---|---|
| Language / Framework | Java, Spring Boot |
| Cache | Redis |
| Build tool | Maven |
| Infrastructure | Docker Compose |

## Quick start

**Requirements:** Java 17+, Docker Desktop

```bash
# 1. Clone
git clone https://github.com/Joyslin-2006/Caching-Proxy.git
cd Caching-Proxy

# 2. Start Redis
docker compose up -d

# 3. Run the service
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

The service runs at `http://localhost:8080`.

## Try it

Send the same request twice:

```bash
curl -i http://localhost:8080/posts/1
curl -i http://localhost:8080/posts/1
```

| Request | `X-Cache` | Speed |
|---|---|---|
| 1st | `MISS` | Slower (calls the origin) |
| 2nd | `HIT` | Faster (served from Redis) |

After the cache duration passes (60 seconds by default), the next request is a `MISS` again.

## Configuration

Settings live in the `.env` file:

| Variable | Purpose | Default |
|---|---|---|
| `PROXY_UPSTREAM_SERVER_HOST` | Origin server to forward requests to | `https://jsonplaceholder.typicode.com` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `CACHE_DURATION_SECONDS` | How long responses stay cached | `60` |

To proxy a different API, change `PROXY_UPSTREAM_SERVER_HOST` and restart.

## Project structure

```
Caching-Proxy/
├── src/            Application source code
├── compose.yaml    Redis container setup
├── .env            Configuration
├── pom.xml         Maven dependencies
└── mvnw            Maven wrapper
```

## Key concepts demonstrated

- Proxying and forwarding HTTP requests
- Cache-aside pattern with Redis
- TTL-based cache expiry
- Reducing latency and load on an origin server
- Containerized dependencies with Docker Compose
- Environment-based configuration

## Future improvements

- Command or endpoint to clear the cache
- Respect the origin's `Cache-Control` headers
- Dockerfile for one-command startup of the whole stack
- Automated tests

## Credits

Project idea from [roadmap.sh](https://roadmap.sh/projects/caching-server).
