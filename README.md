# josetta
A Java to ES6 Transpiler (WORK IN PROGRESS!)

## Description
![josetta-mini.png](https://github.com/gianpierodiblasi/josetta/blob/master/josetta-mini.png?raw=true)

josetta is the inverted acronym of A Transpiler To ES6 of Old Java, or if you prefer is the union of Java and
[Rosetta](https://en.wikipedia.org/wiki/Rosetta_Stone).

Aside from these funny word games, josetta is my personal attempt to develop a Java to ES6 transpiler. josetta is used in my other
projects so its development follows my needs in those projects. As a result, only a very small fraction of all Java specifications
and J2SE are correctly handled.

josetta is ***NOT*** a production ready tool; if you need a professional Java to JavaScript transpiler then I suggest to use
[JSweet](https://jsweet.org). I started to use JSweet but I was not able to successfully integrate it into my development flow.

## Writing Java code
josetta correctly handles a very small portion of Java, so there are a lot of limitations and conventions to follow:
- conventions
  - first golden rule, write Java code as if you are writing ES6 code
  - always use the "this." prefix to reference a parameter or a method
  - always use the "ClassName." prefix to reference a static parameter or a static method
  - if you want to create a class or a method but you don't want the class/method to be transpiled then prefix its name with ***$*** symbol;
    in the transpilation phase the class/method ***$***<class/method-name> will be replaced with <class/method-name>
- covered features
  - imports (they are omitted)
  - generics (they are omitted)
  - class constructors (public, protected, private and friendly)
  - class (static) parameters (public, protected, private and friendly)
  - class (static) methods (public, protected, private and friendly)
  - primitive types (byte, short, int, long, float, double, boolean) and String type (with some limitations)
- limitations
  - packages are not covered, so (for example) two classes with the same name in different packages are not covered
    (the second transpiled class will replace the first one)
  - interfaces and abstract classes (public, protected, private and friendly) are converted to class (empty methods are appropriately managed)
  - multiple inheritance (classes and interfaces) is not covered, each class can extend only one class/interface
  - inner interfaces and classes are not covered
  - enums are not covered
  - constructor overloading is not covered
  - method overloading is not covered
  - parameters and methods cannot have the same name
  - parameters declarations can contain only one variable declaration for each row
  - all Java objects in all Java packages (Object, String, Math, etc.) are not covered
  - collections (array, list, map, tree, etc.) are not covered, as an alternative the Array object in
    [jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/) can be used
  - in general standard DOM and JS objects can be "simulated" by using
    [jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/)
  - any JS external library can be "simulated" (if available) by using
    [jsweet candies](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/candies/) or you can write your "simulation"

## Dependencies
- JavaParser - [link](https://javaparser.org/)
- Apache Commons CLI - [link](https://commons.apache.org/proper/commons-cli/)

## Build
josetta is developed in [NetBeans](https://netbeans.apache.org/) as a [maven](https://maven.apache.org/) project.
In order to perform a build you can use maven CLI or any IDE compatible with maven.

## Run
josetta can be embedded in your Java project or can be used by its CLI. The following is an example how to run josetta by command line:
```
java -jar <josetta-jar> -in <in> -out <out> -w
```
The following table explains the parameters

| Parameter | Description | Mandatory |
| - | - | - |
| in  | the input file/folder  | true |
| out | the output file/folder | true |
| w   | if available josetta will indefinitely watch for files changes in the input folder and automatically transpile the files | false |

## Donate
If you would like to support the development of this and/or other projects, consider making a [donation](https://www.paypal.com/donate/?business=HCDX9BAEYDF4C&no_recurring=0&currency_code=EUR).