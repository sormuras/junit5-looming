# junit5-looming
JUnit 5 Test Engine using project Loom early-access builds

- https://jdk.java.net/loom/
- https://mail.openjdk.java.net/pipermail/loom-dev/2020-January/001002.html
- https://www.youtube.com/watch?v=NV46KFV1m-4

## timings

| Processor              | Threads 10.000 | Threads 100.000 | Virtual 10.000 | Virtual 100.000 | Virtual 1.000.000|
|----------------------- |----------------|-----------------|----------------|---------------- |------------------|
| Ryzen 3700X (Win 10)   | 5.6            | 51              | 1.1            | 1.2             | 3.9 :rocket:     |
| i7-3770K (Win 10)      | 6              | 51              | 1.2            | 1.5             | 5.2 :rocket:     |
| i7-7920HQ (Mac OS)     | 6              | 51              | 1.2            | 1.7             | 7.1 :rocket:     |
| GitHub/Azure (Linux)   | 6              | n.a.            | 3              | 20              | 223              |
| [GH 2019-12-19] (Linux)| 7              | 53              | 3              | 18              | 227              |
| [GH 2020-01-25] (Linux)| 6              | 53              | 2              | 14              | 162              |
| [GH 2020-01-28] (Linux)| 7              | 53              | 3              | 16              | 150              |
| [GH 2020-02-27] (Linux)| 7              | 52              | 3              | 17              | 150              |
| [GH 2020-05-15] (Linux)| 6              | 53              | 3              | 18              | 183              |

## projects, demos, blogs, and spikes using loom

- https://github.com/forax/loom-fiber (Rémi Forax)
- https://i-rant.arnaudbos.com/loom-part-0-rationale/ (@arnaud_bos)
- https://horstmann.com/unblog/2019-12-05/ (@cayhorstmann)
- https://blog.softwaremill.com/will-project-loom-obliterate-java-futures-fb1a28508232 (Adam Warski)
- https://developers.redhat.com/blog/2019/06/19/project-loom-lightweight-java-threads (Faisal Masood)


[GH 2019-12-19]: https://github.com/sormuras/junit5-looming/runs/356277420
[GH 2020-01-25]: https://github.com/sormuras/junit5-looming/runs/408878572
[GH 2020-01-28]: https://github.com/sormuras/junit5-looming/runs/412758929
[GH 2020-02-27]: https://github.com/sormuras/junit5-looming/runs/471973940
[GH 2020-05-15]: https://github.com/sormuras/junit5-looming/runs/678623328
