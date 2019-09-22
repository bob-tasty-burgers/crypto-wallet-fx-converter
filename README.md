# Bob's crypto wallet euro converter

Bob, here's the source code for an app to convert your crypto holdings into euros.

According to the description I received for this project, it seems you only have java 8 in your work laptop.
However, in the same description I was also encouraged to use the latest java version.
If you still java 8 in your work laptop, I suggest you upgrade to a more recent version with a package manager
or following [these instructions](https://openjdk.java.net/install/).
This program was created using OpenJDK 13, but it should also work with Java 11 or 12.

### Running the application ###
Get the  source code with:
```
git clone https://github.com/bob-tasty-burgers/crypto-wallet-fx-converter.git
```

Compile the source code with:
```
 ./gradlew jar
```
Run the application with:
``` 
java -jar build/libs/crypto-wallet-fx-converter-1.0.jar
```

By default the application will try to read the file `bobs_crypto.txt` in the folder from which you launch the command.
However, you can also specify a different location for your crypto assets file with:
```
java -jar build/libs/crypto-wallet-fx-converter-1.0.jar /path/to/some/other/crypto/assets/file
```

If one of the entries in `bobs_crypto.txt` is invalid or if no euro exchange rate could be obtained for one of
the cryptocurrencies, the application ignores that particular input and writes a message to standard error.

All output is written to standard output. If you don't want to have mixed errors and output in the terminal, you can execute:
```
java -jar build/libs/crypto-wallet-fx-converter-1.0.jar 1> bobs_crypto_out.txt 2> bobs_crypto_err.txt
```

To run tests:
```
./gradlew test
```

### Investment advice ###
Bob, be careful with cryptocurrencies, they are highly volatile and often trade in unregulated exchanges.
If I may, I would suggest you don't overexpose yourself to this asset class (or any other) and diversify your wealth
into other asset categories: stocks, bonds, real estate, etc. Consistently good investment performance can only be achieved
with sound risk management.

Interestingly, insurance companies also need to ensure the risks they insure their clients against are appropriately diversified,
and not overexpose themselves to any particular type of risk or geography. Reinsurance companies allow insurance companies to
take away some of the risk of their policies. What's really interesting about the insurance business is that clients pay upfront
for a risk that will possibly materialise in the future. This provides insurance companies with a lot of capital, called float,
that they can invest elsewhere. An important part of the insurance business is investing that float.

### Implementation notes ###
The HttpClient, available since Java 11, has the non-blocking `sendAsync` method, that returns a CompletableFuture. 
Using this method would allow to parallelize requests and liberate CPU time for other tasks. However, given the requirements,
it was considered preferable to have a simpler (albeit less efficient) solution. Indeed, if you are given the task to construct
a vehicle to commute every day 4 kilometers, it's probably better to build a bicycle and not a roket; although building rockets is
fun, they are difficult to operate and expensive to maintain!
