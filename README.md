# http-server-from-scratch

A minimal HTTP/1.1 server built from scratch using Java sockets and virtual threads. No frameworks, no external web libraries — just core Java.

## Features

- HTTP/1.1 request parsing and routing
- Supports `GET` and `POST` methods
- File serving and file upload
- Gzip compression (via `Accept-Encoding: gzip`)
- Persistent connections with `Connection: keep-alive`
- Custom middleware support
- Lightweight and fully testable with `curl`

## Usage

```bash
Basic server check
curl -v http://localhost:4221/
→ Expected: HTTP/1.1 200 OK, body: "OK"

Echo route
curl -v http://localhost:4221/echo/banana
→ Expected: HTTP/1.1 200 OK, body: "banana"

User-Agent route
curl -v -H "User-Agent: blueberry/apple-blueberry" http://localhost:4221/user-agent
→ Expected: HTTP/1.1 200 OK, body: "blueberry/apple-blueberry"

File reading (assuming test.txt exists in the configured directory)
curl -v http://localhost:4221/files/test.txt
→ Expected: HTTP/1.1 200 OK, body: contents of test.txt

Missing file
curl -v http://localhost:4221/files/missing.txt
→ Expected: HTTP/1.1 404 Not Found

File writing
curl -v -X POST http://localhost:4221/files/newfile.txt --data "hello world"
→ Expected: HTTP/1.1 201 Created (or 200 OK), file "newfile.txt" written with "hello world"

Gzip response
curl -v -H "Accept-Encoding: gzip" http://localhost:4221/echo/banana --output - | gunzip
→ Expected: HTTP/1.1 200 OK with gzip-encoded body, decompressed output: "banana"

Persistent connection (2 requests back-to-back)
curl --http1.1 -v http://localhost:4221/user-agent -H "User-Agent: blueberry/pear-pear" --next http://localhost:4221/user-agent -H "Connection: close" -H "User-Agent: blueberry/pear"
→ Expected: Two HTTP/1.1 200 OK responses over one connection
```
Why?
Built as a learning project to understand the internals of HTTP, sockets, and server concurrency in Java — including the new virtual threads introduced in Project Loom (Java 21+).

