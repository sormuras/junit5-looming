# junit5-looming
JUnit 5 Test Engine using project Loom early-access builds

- https://jdk.java.net/loom/
- https://www.youtube.com/watch?v=NV46KFV1m-4

## timings

[i7-3770K](https://ark.intel.com/content/www/us/en/ark/products/65523/intel-core-i7-3770k-processor-8m-cache-up-to-3-90-ghz.html) on Windows 10

- Threads
  - `10.000` 5.7s
  - `100.000` 50.7s
- Fibers
  - `10.000` 1.2s
  - `100.000` 1.5s
  - `1.000.000` 5.2s :rocket:
  
[i7-7920HQ](https://ark.intel.com/content/www/us/en/ark/products/97462/intel-core-i7-7920hq-processor-8m-cache-up-to-4-10-ghz.html) on Mac OS

- Threads
  - `10.000` 5.8s
  - `100.000` 51.0s
- Fibers
  - `10.000` 1.2s
  - `100.000` 1.7s
  - `1.000.000` 7.1s :rocket:
  
[GitHub CI/CD box](https://azure.microsoft.com/en-us/services/devops/pipelines/) on Linux

- Threads
  - `10.000` 6.1s
- Fibers
  - `10.000` 2.9s
  - `100.000` 19.3s
  - `1.000.000` 223.1s
