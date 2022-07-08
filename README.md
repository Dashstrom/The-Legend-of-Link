# The legend of Link

Small project over 7 weeks on the theme of zelda-likes. Use the MVC model with javafx.

## Development

Make Maven wrapper

```sh
mvn -N io.takari:maven:wrapper
```
Install source with javadoc

```sh
./mvnw dependency:sources
```


## Source

without git

```sh
curl -g https://codeload.github.com/Dashstrom/The-Legend-of-Link/zip/refs/heads/master --ouput The-Legend-of-Link.zip
tar xf The-Legend-of-Link-archive.zip
cd The-Legend-of-Link-master
```

with git

```sh
git clone https://github.com/Dashstrom/The-Legend-of-Link.git
cd The-Legend-of-Link
```

## Windows prerequisite

As administrator (not mandatory)

```sh
where java
# replace your Path by jdk-X or jre-X
setx JAVA_HOME 'Path'
```

## Build standalone jar

```sh
.\mvnw -T 2C clean package
```

## Run game

```sh
java -jar target/the-legend-of-link-1.0.1-jar-with-dependencies.jar
```

## Proxy Maven

Sometimes you need to configure your society proxy.

`~/.m2/settings.xml`

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
    <proxies>
        <proxy>
            <id>id</id>
            <active>true</active>
            <protocol>http</protocol>
            <username>username</username>
            <password>password</password>
            <host>host</host>
            <port>port</port>
            <nonProxyHosts>local.net|some.host.com</nonProxyHosts>
        </proxy>
    </proxies>
</settings>
```

`.mvn/jvm.config`

```sh
-Dhttp.proxyHost=host 
-Dhttp.proxyPort=port 
-Dhttps.proxyHost=host 
-Dhttps.proxyPort=port 
-Dhttp.proxyUser=username 
-Dhttp.proxyPassword=password
```
