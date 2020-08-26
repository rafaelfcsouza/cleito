# Cleito - A Clojure Proof of Work Client Attestation API

In order to prevent brute force attempts in a endpoint, we could use Proof-of-Work techniques to slow down the attackers speed. Cleito will act like a captcha and force the client to "work" to send a valid request to the backend.

This repository contains a server and an example client in Node.js.

## How does it work?

A Client will request Cleito for a work to be done. The response will contain a string and a difficulty, which dictates the work that the client need to fulfil. The Client will then process a SHA-256 of the string plus a number until it meets the requirement of the first N characters of the SHA-256 hash with zeros, where the N = difficulty.

After that, the Client can send the payload to the Server, which can verify the Proof that the work has been done by the Client with Cleito.

```
+-----------+     +-----------+     +-----------+
|           |     |           |     |           |
| Client    |     | Cleito    |     | Server    |
|           |     |           |     |           |
+-----+-----+     +-----+-----+     +-----+-----+
      |                 |                 |
      |  requests work  |                 |
      +---------------->+                 |
      |                 |                 |
      +<----------------+                 |
      |                 |                 |
      +--+ work to meet |                 |
      |  | requirements |                 |
      +<-+              |                 |
      |                 |                 |
      |                 |                 |
      |  sends request with proof of work |
      +-----------------+---------------->+
      |                 |                 |
      |                 | Validates proof |
      |                 +<----------------+
      |                 |  OK or Not OK   |
      |                 +---------------->+
      |                 |                 |
      +<----------------+-----------------+
      |   answers to client according to  |
      |   cleito validation of work       |
      +                 +                 +
```
