### Quick Collections Microbenchmark ###

This test, based on the [Scala Micro-Benchmarking Template][3], performs some quick-and-dirty microbenchmarks involving
map operations with various collection classes.

This class is basically trying to quantify some of the performance characteristics of various collections. Of course
performance isn't everything! This is just one (possibly very small) factor in the decision of which collection
to use. But it is a factor, and one that is nice to be able to quantify.

This test is grossly unfair to some of the collections (scala.immutable.Map in particular) and several of these
libraries have other advantages that are not taken into account at all here.

There are a lot of other factors that are not taken into account here: ease-of-use for the collections, whether they
conform to a standard interface (or any interface at all), and so on.

### About this benchmark

Both benchmarks just insert 100,000 entries into a map, gets those same 100,000 entries, then updates (puts with a
different value) 100,000 entries. So, 1:2 read:write. I'm happy to write a more read-heavy benchmark if anyone is
interested.

The first test is a map where both keys and values are of type int. This is where primitive collections are most at an
advantage Ñ the java.util.collections have to wrap both key and value in an Integer object.

The second test is a map where the key is a long and the value is an object.

Both tests only use put and get; they don't test contains, iteration, or anything else. That's the particular use case
I was interested in when I wrote them, and a pretty typical one for the application in question, so it was a good place
to start. They test gets along with puts, instead of splitting them up, in order to let us test memory allocation (we
start with an empty map and fill it during the course of the tests).

The benchmark measures execution time and memory consumption (using some jdk instrumentation). The [Caliper][1]
framework takes care of running multiple iterations, running un-timed warmups to make sure code is compiled, and so on.

The tests allocate all test data before the timed portions of the tests. For example, in the long->Object map, we
pre-allocate a list of TestObject objects (a simple object that contains a Long) before the test, and then insert and
select those pre-allocated objects during the test. So we are not actually creating the TestObject instances themselves
during the test Ð time spent and memory usage shown below are simply the collections' overhead.

A few notes about the contenders in these benchmarks:

* The "JavaHashMap" is Java 6 (1.6.0_29-b11-402-10M3527). Java 7 collections are apparently significantly faster, but I haven't tested them yet.
* The *Prealloc contenders (ColtPrealloc, TrovePrealloc, etc.) are where I initialized the maps with the correct number of elements, i.e.:

    trove2Prealloc = new TLongObjectHashMap(length); // where length = 100,000 in this case.

That happens before the test, so since those collections had already internally allocated arrays of the appropriate length, they didn't have to allocate much of anything during the test. So it's not that the *Prealloc examples occupied less memory, it's just that they allocated less memory over the duration of the test.

* The "array" entries are just that: a vanilla java array ( int[] or Object[]). Obviously, that's a very different thing than a map. I put that in for two reasons:
** primarily, it gives you an idea of the lower bound Ð i.e., the most efficient possible way (that i know of) to store a collection of primitives in Java. So it gives you an idea of where the floor is.
** secondarily, these particularly simple artificial benchmarks are just iterating from 0-100,000 and inserting i > i or i>TestObject into maps. That particular use case is clearly better implemented with an array in real life, so I was curious what kind of benefit I could expect from actually using an array in this situation.
* The Scala collections are at a real disadvantage here; they're not terribly efficient and focus on usability over performance. They also offer a lot of operations that the others don't. If it helps any, collection performance is a big focus of upcoming Scala language releases.
** The Scala default (immutable) map has it the worst Ð since it's an immutable map, its actually creating a new map for each put() call. So, why include it at all, you ask? Because, while I know that scala.immutable.Map is much slower for puts, I wanted to be able to quantify how much. When deciding for or against an immutable collection in a particular scenario, I want to know how much performance I am giving up in favor of immutability. Knowing that, I can make an informed decision.



### Results (as of latest checkin):


<table>
    <tbody>
      <tr>
        <th>benchmark</th>
        <th>instances</th>
        <th>Bytes</th>
        <th>ms</th>
        <th>linear runtime</th>
      </tr>
      <tr>
        <td>Trove2</td>
        <td>54.00</td>
        <td>3703888</td>
        <td>10.60</td>
        <td>=</td>
      </tr>
      <tr>
        <td>Trove2Prealloc</td>
        <td>10.00</td>
        <td>184</td>
        <td>8.38</td>
        <td>=</td>
      </tr>
      <tr>
        <td>Trove3</td>
        <td>53.00</td>
        <td>3703872</td>
        <td>9.04</td>
        <td>=</td>
      </tr>
      <tr>
        <td>Trove3Prealloc</td>
        <td>10.00</td>
        <td>184</td>
        <td>7.10</td>
        <td>=</td>
      </tr>
      <tr>
        <td>Colt</td>
        <td>47.00</td>
        <td>5553768</td>
        <td>13.17</td>
        <td>=</td>
      </tr>
      <tr>
        <td>ColtPrealloc</td>
        <td>10.00</td>
        <td>184</td>
        <td>9.33</td>
        <td>=</td>
      </tr>
      <tr>
        <td>PColt</td>
        <td>47.00</td>
        <td>5553768</td>
        <td>13.40</td>
        <td>==</td>
      </tr>
      <tr>
        <td>PColtPrealloc</td>
        <td>10.00</td>
        <td>184</td>
        <td>9.06</td>
        <td>=</td>
      </tr>
      <tr>
        <td>ScalaDefault</td>
        <td>2652797.00</td>
        <td>128829464</td>
        <td>200.05</td>
        <td>==============================</td>
      </tr>
      <tr>
        <td>ScalaMutable</td>
        <td>999516.00</td>
        <td>20489384</td>
        <td>37.67</td>
        <td>=====</td>
      </tr>
      <tr>
        <td>ScalaIntMap</td>
        <td>3407644.00</td>
        <td>97850584</td>
        <td>91.55</td>
        <td>=============</td>
      </tr>
      <tr>
        <td>JavaHashMap</td>
        <td>499515.00</td>
        <td>11689384</td>
        <td>19.44</td>
        <td>==</td>
      </tr>
      <tr>
        <td>IntArray</td>
        <td>12.00</td>
        <td>400216</td>
        <td>4.62</td>
        <td>=</td>
      </tr>
  </tbody>
</table>


Long->Object map benchmark

<table>
    <tbody>
      <tr>
        <th>benchmark</th>
        <th>instances</th>
        <th>Bytes</th>
        <th>ms</th>
        <th>linear runtime</th>
      </tr>
      <tr>
        <td>Trove2</td>
        <td>54.00</td>
        <td>5349528</td>
        <td>13.21</td>
        <td>=========================</td>
      </tr>
      <tr>
        <td>Trove2Prealloc</td>
        <td>10.00</td>
        <td>192</td>
        <td>9.95</td>
        <td>===================</td>
      </tr>
      <tr>
        <td>Trove3</td>
        <td>54.00</td>
        <td>5349544</td>
        <td>11.12</td>
        <td>=====================</td>
      </tr>
      <tr>
        <td>Trove3Prealloc</td>
        <td>10.00</td>
        <td>192</td>
        <td>8.10</td>
        <td>===============</td>
      </tr>
      <tr>
        <td>Colt</td>
        <td>47.00</td>
        <td>8021648</td>
        <td>15.65</td>
        <td>==============================</td>
      </tr>
      <tr>
        <td>ColtPrealloc</td>
        <td>10.00</td>
        <td>192</td>
        <td>9.90</td>
        <td>==================</td>
      </tr>
      <tr>
        <td>PColt</td>
        <td>47.00</td>
        <td>8021648</td>
        <td>15.63</td>
        <td>=============================</td>
      </tr>
      <tr>
        <td>PColtPrealloc</td>
        <td>10.00</td>
        <td>192</td>
        <td>10.48</td>
        <td>====================</td>
      </tr>
      <tr>
        <td>JavaHashMap</td>
        <td>599642.00</td>
        <td>17288352</td>
        <td>15.39</td>
        <td>=============================</td>
      </tr>
      <tr>
        <td>ObjArray</td>
        <td>200012.00</td>
        <td>5200224</td>
        <td>6.35</td>
        <td>============</td>
      </tr>
    </tbody>
</table>


Note that the primitive collections require almost no object allocation Ð they don't use HashMap$Entry wrapper objects
around the keys and values like java.util.collections.Map does. As a tradeoff, you can't natively iterate over the
key+value of those maps Ð those that let you will actually create Entry objects as you iterate, in which case you'd
lose the memory advantage.

### Running the tests yourself

1. Clone this repository

        $ git clone git://github.com/matthoffman/primitive-collection-benchmark.git primitive-collection-benchmark

2. Modify the tests if desired

3. Set up an environment variable ALLOCATION_JAR to point to the absolute directory of allocation.jar

I should automate this step in SBT, but haven't yet. The Caliper folks have also said they plan on making this step
unnecessary, but haven't yet. Meanwhile:

       $ cd primitive-collection-benchmark/lib
       $ export ALLOCATION_JAR=`pwd`/allocation.jar
       $ echo $ALLOCATION_JAR  # output should look like: ALLOCATION_JAR=/Users/matt/src/primitive-collection-benchmark/lib/allocation.jar

4. Run sbt (if you don't have sbt installed, see [https://github.com/harrah/xsbt][4]

       $ sbt

5. Invoke:

       > run --measureMemory

It should compile and run one of the tests. Right now, it will only actually run the FIRST test listed in the org.mhoffman.Runner class;
I think the Caliper code calls System.exit() after the each test. So to run the other test, you have to manually edit that file.

### Some postscript notes

I originally wrote the benchmark in more idomatic Scala. A couple of interesting things to note:
1.) Scala's default for loop in notoriously slow. Fortunately, the starting point I used (git://github.com/ngerhart/scala-benchmarking-template.git)
already worked around that for me.
2.) In these tests, I created the test data up front and stuck it in a list, then pulled it out of that list as I went. That was so that the memory allocation I was measuring was really just the overhead of the collections themselves, not the test data that was the same for all tests. But when I did this using Scala's default List ( var testData = List() ) all of the collections under test took the same amount of time, and they were all slow. Turns out, scala.List was slow enough that it was masking any difference between the primitive collections. The moral of the story:  one different collection class might be faster than another, but one bad design decision can mask a hundred good ones. So before switching to an efficient primitive collection, make sure there's not other bottlenecks you need to deal with.
3.) Similarly, I started out by writing the boilerplate test method only once, then passing in a closure that does the putting and getting. However, the closure itself added enough overhead that I thought it more prudent to just copy-and-paste the test methods. Yes, its painful to have all that very repeated, very similar code, but it's worth it to have a valid test.


### Original README of the [Scala Micro-Benchmarking Template][3]:
  
This is an SBT template project for creating micro benchmarks for scala code snippets.
It's not much more than a simple wrapper around [Caliper][1], an open-source library for properly
running benchmark code on the JVM (written by some guys at Google).

Manually writing benchmarks for the JVM that actually measure what you intend to measure is much harder than it
initially appears. There are quite a few [rules][2] you need to keep in mind, so it's best to rely on a framework
that takes care of the details and let's you focus on the code relevant to your application.
[Caliper][1] provides just this framework and this project makes it easily accessible for Scala developers.

#### How to create your own Scala micro-benchmark

Check out github.com/ngerhart/scala-benchmarking-template.git my-benchmark


  [1]: http://code.google.com/p/caliper/
  [2]: http://wikis.sun.com/display/HotSpotInternals/MicroBenchmarks
  [3]: http://github.com/ngerhart/scala-benchmarking-template
  [4]: https://github.com/harrah/xsbt


