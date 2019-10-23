jshell https://bit.ly/bach-jsh

java --module-path lib;bin\realm\main\modules --module org.junit.platform.console --details SUMMARY --scan-modules --config tests=10000 --config thread=true
java --module-path lib;bin\realm\main\modules --module org.junit.platform.console --details SUMMARY --scan-modules --config tests=100000 --config thread=true

java --module-path lib;bin\realm\main\modules --module org.junit.platform.console --details SUMMARY --scan-modules --config tests=10000 --config thread=false
java --module-path lib;bin\realm\main\modules --module org.junit.platform.console --details SUMMARY --scan-modules --config tests=100000 --config thread=false
java --module-path lib;bin\realm\main\modules --module org.junit.platform.console --details SUMMARY --scan-modules --config tests=1000000 --config thread=false
