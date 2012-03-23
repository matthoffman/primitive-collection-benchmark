package org.example

import annotation.tailrec
import com.google.caliper.Param
import java.util.ArrayList
import gnu.trove.TIntIntHashMap
import gnu.trove.map.hash.{TIntIntHashMap => T3TIntIntHashMap}
import cern.colt.map.OpenIntIntHashMap
import collection.immutable.IntMap

// a caliper benchmark is a class that extends com.google.caliper.Benchmark
// the SimpleScalaBenchmark trait does it and also adds some convenience functionality
class IntIntGetBenchmark extends SimpleScalaBenchmark with IntData {
  
  // to make your benchmark depend on one or more parameterized values, create fields with the name you want
  // the parameter to be known by, and add this annotation (see @Param javadocs for more details)
  // caliper will inject the respective value at runtime and make sure to run all combinations 
  @Param(Array("1000000")) // "100000", "1000000", "10000000")) // in practice, we tend to hang (OOM?) after 100,000 elements. What's up with that?
  val length: Int = 0

  override def setUp() {
    setUp(length)
  }


  /**
   * The actual tests.
   *
   * For the record, I did at one point use some Scala cleverness to avoid the copy-and-paste in the tests
   * here (I instead had one "runTest" method that took a structural type called Mappish) but that had significant
   * performance overhead -- using structural types had a > 200% penalty.
   * So, we're back to the copy-and-paste
   *
   */
  def timeTrove2(reps: Int) = repeat(reps) {
    val map = new TIntIntHashMap()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i * i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeTrove2Prealloc(reps: Int) = repeat(reps) {
    val map = trove2Prealloc
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeTrove3(reps: Int) = repeat(reps) {
    val map = new T3TIntIntHashMap()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeTrove3Prealloc(reps: Int) = repeat(reps) {
    val map = trove3Prealloc
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeColt(reps: Int) = repeat(reps) {
    val map = new OpenIntIntHashMap()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeColtPrealloc(reps: Int) = repeat(reps) {
    val map = coltPrealloc
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timePColt(reps: Int) = repeat(reps) {
    val map = new cern.colt.map.tint.OpenIntIntHashMap()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timePColtPrealloc(reps: Int) = repeat(reps) {
    val map = pcoltPrealloc
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, i);
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }



  def timeScalaDefault(reps: Int) = repeat(reps) {
    var map = Map[Int,Int]()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> i;
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map(i)
//      result += map.getOrElse(i + 1, 0)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> 123
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeScalaMutable(reps: Int) = repeat(reps) {
    var map = scala.collection.mutable.Map[Int,Int]()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> i;
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map(i)
//      result += map.getOrElse(i + 1, 0)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> 123
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeScalaIntMap(reps: Int) = repeat(reps) {
    var map = IntMap[Int]()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> i;
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map(i)
//      result += map.getOrElse(i + 1, 0)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map += i -> 123
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }



  def timeJavaHashMap(reps: Int) = repeat(reps) {
    var map = new java.util.HashMap[Int,Int]()
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.put(i,i)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map.get(i)
//      result += map.get(i + 1)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map.put(i, 123)
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  /**
   * Realizing that our example is stupid (sequential list of ints? really??) and that this particular trivial case
   * should just be an array, i'm curious how an array compares.
   */
  def timeIntArray(reps: Int) = repeat(reps) {
    var map = new Array[Int](length)
    var result = 0
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map(i) = i
      result += i
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      result += map(i)
    }
    tfor(0)(_ < dataset.size(), _ + 1) { i =>
      map(i) = 123
      result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  override def tearDown() {
    // clean up after yourself if required
  }


}


trait IntData {

  // yes, it's just a list of numbers. But they're proper java.lang.Integers.
  val dataset: java.util.List[java.lang.Integer] = new ArrayList[java.lang.Integer]()

  def initializeData(length: Int) {
    // this would be much prettier as a Map, but it's a Java collection, and this is easier.
    // the JavaConversion implicits mainly wrap Scala collections in wrappers that implement Java
    // interfaces, which isn't what we want.
    0 to (length-1) foreach { i : Int =>
      val in : java.lang.Integer = i
      dataset.add(in)
    }
  }

    // our preallocated candidates
  var trove2Prealloc = new TIntIntHashMap();
  var trove3Prealloc   = new T3TIntIntHashMap()
  var coltPrealloc  = new OpenIntIntHashMap();
  var pcoltPrealloc  = new cern.colt.map.tint.OpenIntIntHashMap();


  def setUp(length: Int) {
    // set up all your benchmark data here
    initializeData(length)

    trove2Prealloc  = new TIntIntHashMap(length);
    trove3Prealloc  = new T3TIntIntHashMap(length)
    coltPrealloc    = new OpenIntIntHashMap(length);
    pcoltPrealloc   = new cern.colt.map.tint.OpenIntIntHashMap(length);
  }
}



