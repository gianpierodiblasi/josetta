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
  - always use the "this." prefix to reference a parameter or a method (for methods, josetta will try to add the prefix, but it is not guaranteed)
  - always use the "ClassName." prefix to reference a static parameter or a static method  
  - don't use full qualified name of classes, that is
    - don't use
  ```java
  package.subpackage1.subpackage11.ClassName object = new package.subpackage1.subpackage11.ClassName();
  ```
    - use
  ```java
  import package.subpackage1.subpackage11.ClassName;
  ClassName object = new ClassName();
  ```
- covered features
  - imports (they are omitted)
  - generics (they are omitted)
  - class cast (they are omitted)
  - class constructors (public, protected, private and friendly)
  - class (static) parameters (public, protected, private and friendly)
  - class (static) methods (public, protected, private and friendly)
  - primitive types (byte, short, int, long, float, double, boolean) and String type (with some limitations)
- limitations
  - packages are not covered, so (for example) two classes with the same name in different packages are not covered
    (the second transpiled class will replace the first one)
  - interfaces and abstract classes (public, protected, private and friendly) are converted into class (empty methods are appropriately managed)
  - multiple inheritance (classes and interfaces) is not covered, each class can extend only one class/interface
  - inner interfaces and classes are not covered
  - enums are not covered
  - constructor overloading is not covered
  - method overloading is not covered
  - parameters and methods cannot have the same name
  - parameters declarations can contain only one variable declaration for each row
  - all Java objects in all Java packages (Object, Math, etc.) are not covered
  - collections (array, list, map, tree, etc.) are not covered, as an alternative the Array object in
    [jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/) can be used
  - in general standard DOM and JS objects can be "simulated" by using
    [jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/)
  - any JS external library can be "simulated" (if available) by using
    [jsweet candies](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/candies/) or you can write your "simulation"
    (for example by means of the ***$*** features described [below](#$symbol))

### Special Use Cases
- if you are using the Array object available in [jsweet-core](https://repository.jsweet.org/artifactory/libs-release-local/org/jsweet/jsweet-core/)
  or if you are simulating arrays by means of your own class, then you can use the special methods $get/$set to get/set the array values.
  For example the following code snippet:
  ```java
  Array array = new Array();
  ...
  array.$set(0, 1);
  ...
  int value = array.$get(0);
  ```
  will be transpiled into:
  ```javascript
  let array = new Array();
  ...
  array[0] = 1;
  ...
  let value = array[0];
  ```
- <a name="get_set_side_effect"></a>as a side effect you cannot declare methods named $get/$get otherwise they will be treated as array access methods
- if you cannot use the $get/$set methods then you can define new getter and setter methods for arrays (see [below](#run))
- if you want to simulate the JavaScript behaviour relative to the comparison with *null*, *undefined*, empty strings and zero you can use the special
  method $exists. For example the following code snippet:
  ```java
  Object obj = ...;
  ...
  boolean b1 = $exists(obj);
  ...
  boolean b2 = !$exists(obj);
  ```
  will be transpiled into:
  ```javascript
  let obj = ...;
  ...
  let b1 = !!(obj);
  ...
  let b2 = !obj;
  ```
- <a name="exists_side_effect"></a>as a side effect you cannot declare methods named $exists otherwise they will be treated as comparison with *null*, *undefined*,
  empty strings and zero
- if you cannot use the $exists methods then you can define new exists methods (see [below](#run))
- if you want to simulate the JavaScript behaviour relative to the *typeof* comparison you can use the special method $typeof. For example the following code snippet:
  ```java
  Object obj = ...;
  ...
  boolean b1 = $typeof(obj, "string");
  ```
  will be transpiled into:
  ```javascript
  let obj = ...;
  ...
  let b1 = typeof obj === "string";
  ```
- <a name="typeof_side_effect"></a>as a side effect you cannot declare methods named $typeof otherwise they will be treated as *typeof* comparison
- if you cannot use the $typeof methods then you can define new typeof methods (see [below](#run))
- if you want to simulate the JavaScript lambda functions you can use the special method $apply. For example the following code snippet:
  ```java
  @FunctionalInterface
  public interface $Apply_1_Void {
    void $apply(String element);
  }
  ...
  $Apply_1_Void onchange = element -> console.log(element);
  ...
  onchange.$apply("hello world");
  ```
  will be transpiled into:
  ```javascript
  let onchange = element -> console.log(element);
  ...
  onchange("hello world");
  ```
- <a name="apply_side_effect"></a>as a side effect you cannot declare methods named $apply otherwise they will be treated as lambda functions
- if you cannot use the $apply methods then you can define new apply methods (see [below](#run))
- <a name="$symbol"></a>if you want to create a class or a method but you don't want the class/method to be transpiled then prefix its name with the ***$*** symbol;
  in the transpilation phase the class/method ***$***<class/method-name> will be replaced with <class/method-name> (pay attention to
  the side effect described [here](#get_set_side_effect), [here](#exists_side_effect), [here](#typeof_side_effect), and [here](#apply_side_effect))
- if you cannot use the ***$*** symbol then you can define new symbols (see [below](#run))

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
  -ag <array-getter-methods> -as <array-setter-methods>
  -ex <exists-methods> -to <typeof-methods> -ap <apply-methods>
  -nt <no-transpilation-symbols>
```
The following table explains the parameters

| Parameter | Description | Mandatory | Default |
| - | - | - | - |
| in  | the input file/folder  | true | no default value |
| out | the output file/folder | true | no default value |
| w   | if available josetta will indefinitely watch for files changes in the input folder and automatically transpile the files | false | no default value |
| ag  | array getter methods, as a string of comma separated values | false | "$get" |
| as  | array setter methods, as a string of comma separated values | false | "$set" | 
| ex  | exists methods, as a string of comma separated values | false | "$exists" |
| to  | typeof methods, as a string of comma separated values | false | "$typeof" |
| ap  | apply methods, as a string of comma separated values | false | "$apply" |
| nt  | no transpilation symbols, as a string of comma separated values | false | "$" |

## Donate
If you would like to support the development of this and/or other projects, consider making a [donation](https://www.paypal.com/donate/?business=HCDX9BAEYDF4C&no_recurring=0&currency_code=EUR).