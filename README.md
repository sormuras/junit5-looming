# junit5-looming
JUnit 5 Test Engine using project Loom early-access builds

- https://jdk.java.net/loom/
- https://mail.openjdk.java.net/pipermail/loom-dev/2019-December/000931.html
- https://www.youtube.com/watch?v=NV46KFV1m-4

## timings

| Processor              | Threads 10.000 | Threads 100.000 | Virtual 10.000 | Virtual 100.000 | Virtual 1.000.000|
|----------------------- |----------------|-----------------|----------------|---------------- |------------------|
| Ryzen 3700X (Win 10)   | 5.6            | 51              | 1.1            | 1.2             | 3.9 :rocket:     |
| i7-3770K (Win 10)      | 6              | 51              | 1.2            | 1.5             | 5.2 :rocket:     |
| i7-7920HQ (Mac OS)     | 6              | 51              | 1.2            | 1.7             | 7.1 :rocket:     |
| GitHub/Azure (Linux)   | 6              | n.a.            | 3              | 20              | 223              |
| [GH 2019-12-19] (Linux)| 7              | 53              | 3              | 18              | 227              |

## projects, demos, blogs, and spikes using loom

- https://github.com/forax/loom-fiber
- https://i-rant.arnaudbos.com/loom-part-0-rationale/ (@arnaud_bos)
- https://horstmann.com/unblog/2019-12-05/ (@cayhorstmann)


[GH 2019-12-19]: https://github.com/sormuras/junit5-looming/runs/356277420
