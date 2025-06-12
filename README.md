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

### Prerequisites
- Java 21+ (for virtual threads)
- Maven

### Compile & Run
```bash
# Build the project
mvn clean install

# Run the server (optionally specify a directory for file routes)
java -jar target/http-server-from-scratch.jar --directory <dirname>
````

### Example requests

#### 1. Basic Server Check
```bash
curl -v http://localhost:4221/
```  
Expected Response:  
HTTP/1.1 200 OK  
Body: OK

---

#### 2. Echo Route
```bash
curl -v http://localhost:4221/echo/banana
```
Expected Response:  
HTTP/1.1 200 OK  
Body: banana

---

#### 3. User-Agent Route
```bash
curl -v -H "User-Agent: blueberry/apple-blueberry" http://localhost:4221/user-agent
```
Expected Response:  
HTTP/1.1 200 OK  
Body: blueberry/apple-blueberry

---

#### 4. File Reading (assuming `test.txt` exists)
```bash
curl -v http://localhost:4221/files/test.txt
```
Expected Response:  
HTTP/1.1 200 OK  
Body: contents of test.txt

---

#### 5. Missing File
```bash
curl -v http://localhost:4221/files/missing.txt
```
Expected Response:  
HTTP/1.1 404 Not Found

---

#### 6. File Writing
```bash
curl -v -X POST http://localhost:4221/files/newfile.txt --data "hello world"
``` 
Expected Response:  
HTTP/1.1 201 Created  
File `newfile.txt` written with body: "hello world"

---

#### 7. Gzip Response
```bash
curl -v -H "Accept-Encoding: gzip" http://localhost:4221/echo/banana --output - | gunzip
``` 
Expected Response:  
HTTP/1.1 200 OK  
Header: Content-Encoding: gzip  
Decompressed Body: banana

---

#### 8. Persistent Connection (Two Requests)
```bash
curl --http1.1 -v http://localhost:4221/user-agent \
-H "User-Agent: blueberry/pear-pear" \
--next http://localhost:4221/user-agent \
-H "Connection: close" \
-H "User-Agent: blueberry/pear"
```
Expected Response:  
Two HTTP/1.1 200 OK responses over a single connection


## Why?
Built as a learning project to understand the internals of HTTP, sockets, and server concurrency in Java — including the new virtual threads introduced in Project Loom (Java 21+).

