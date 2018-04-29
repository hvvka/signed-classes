# signed-classes

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

--------------


### Temat: 
Wykorzystanie szyfrowania oraz polityki bezpieczeństwa przy ładowaniu klas. 

### Wymagania: 
Do realizacji zadania potrzebna będzie wiedza o kluczach, cyfrowym podpisywaniem paczek jar, weryfikacji podpisów. 

### Zadanie: 
Napisz program, który pozwoli użytkownikowi zaszyfrować i rozszyfrować dany plik. Program powinien korzystać z klasy, która została dostarczona w podpisanej cyfrowo paczce jar. Podczas realizacji zadania należy wygenerować klucz prywatny i publiczny. Należy podpisać cyfrowo jar zawierający skompilowane klasy. Należy wykorzystać pliki polityki i zezwolenia (permissions). 

### Materiały: 
Security Features in Java SE (Oracle) 
Signed Classes (JavaSecurity)