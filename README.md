# josetta
A Java to ES6 Transpiler (WORK IN PROGRESS!!!!)

## Description
![josetta.jpeg](https://github.com/gianpierodiblasi/josetta/blob/master/josetta.jpeg?raw=true)

josetta is the inverted acronym of A Transpiler To ES6 of Old Java, or if you prefer is the union of Java and
[Rosetta](https://en.wikipedia.org/wiki/Rosetta_Stone).

Aside from these funny word games, josetta is my personal attempt to develop a Java to ES6 translator. josetta is used in my other
projects so its development follows my needs in those projects. As a result, only a small fraction (probably less than 10%) of 
all Java specifications are correctly handled.

josetta is ***NOT*** a production ready tool; if you need a professional Java to JavaScript transpiler then I suggest to use
[JSweet](https://jsweet.org).

## Writing Java code
josetta correctly handles a very small portion of Java, so there are a lot of limitations and conventions to follow:
- packages are not covered, so (for example) two classes with the same name in different packages are not covered
- imports are covered (they are omitted)
- interface and classes are covered (public, protected, private and friendly)
- class parameters are covered (public, protected, private and friendly)
- class methods are covered (public, protected, private and friendly)
- class constructors are covered (public, protected, private and friendly)
- constructor overloading is not covered
- method overloading is not covered
- parameters and methods cannot have the same name
- "this." and "super." prefixes are covered
- always use the "this." prefix to reference a parameter or a method
- primitive types are covered
- all Java objects in all Java packages (Object, String, etc.) are not covered
- collections (array, list, map, tree, etc.) are not covered, as an alternative the array oject in
[jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/6.0.1/) can be used
- in general standard DOM and JS objects can be "simulated" by using
[jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/6.0.1/)
- any JS external library can be "simulated" (if available) by using
[jsweet candies](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/candies/)

## Dependencies
- JavaParser - [link](https://javaparser.org/)
- Apache Commons CLI - [link](https://commons.apache.org/proper/commons-cli/)

## Build
josetta is developed in (NetBeans)(https://netbeans.apache.org/) as a [maven](https://maven.apache.org/) project.
In order to perform a build you can use maven cli or any IDE compatible with maven.

## Run

## Donate
If you would like to support the development of this and/or other projects, consider making a [donation](https://www.paypal.com/donate/?business=HCDX9BAEYDF4C&no_recurring=0&currency_code=EUR).