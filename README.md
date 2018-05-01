# signed-classes

Enables encrypting and decrypting classes from selected jar with base64 encoding.

## How to create a signed jar?

### Create keys
```
$ keytool -genkey -alias hello -keyalg RSA -keysize 2048 -keystore hello-keystore -validity 660
```

Everything after alias is optional. Default keystore is `.keystore` file in home directory and default validity is 90 days.

The information for signing jar can be sent to stdout with:
```
$ keytool -genkey -alias hello -keystore hello-keystore < keystore.conf
```

### Sign a jar
```
$ cd signed && gradle copyJarToDirectory && cd ../jar
$ jarsigner -keystore ~/hello-keystore -storepass hania123 -keypass hania123 signed-1.0-SNAPSHOT.jar hello 
```

Verify:
```
jarsigner -verify signed-1.0-SNAPSHOT.jar
```
`-verbose` option gives more info.

------------

TODO
- [ ] set hellopolicy
- [ ] signed jar verification on GUI
- [ ] enable reading encrypted files; cleanup JarManager
