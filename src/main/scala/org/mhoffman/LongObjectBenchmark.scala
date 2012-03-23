package org.mhoffman

import com.google.caliper.Param
import gnu.trove.map.hash.{TLongObjectHashMap => T3TLongObjectHashMap}
import cern.colt.map.OpenLongObjectHashMap
import gnu.trove.TLongObjectHashMap
import cern.colt.map.tobject.{OpenLongObjectHashMap => POpenLongObjectHashMap}
import java.util.ArrayList

// a caliper benchmark is a class that extends com.google.caliper.Benchmark
// the SimpleScalaBenchmark trait does it and also adds some convenience functionality
class LongObjectBenchmark extends SimpleScalaBenchmark with LongData with ObjectData {


  // our preallocated candidates
  var trove2Prealloc = new TLongObjectHashMap[TestObj]();
  var trove3Prealloc = new T3TLongObjectHashMap[TestObj]()
  var coltPrealloc = new OpenLongObjectHashMap();
  var pcoltPrealloc = new POpenLongObjectHashMap();

  def setUp(length: Int) {
    // set up all your benchmark data here
    initializeLongData(length)
    initializeObjectData(length)

    trove2Prealloc = new TLongObjectHashMap(length);
    trove3Prealloc = new T3TLongObjectHashMap(length)
    coltPrealloc = new OpenLongObjectHashMap(length);
    pcoltPrealloc = new POpenLongObjectHashMap(length);
  }

  /*

   */
  // to make your benchmark depend on one or more parameterized values, create fields with the name you want
  // the parameter to be known by, and add this annotation (see @Param javadocs for more details)
  // caliper will inject the respective value at runtime and make sure to run all combinations 
  @Param(Array("100000")) // "100000", "1000000", "10000000"))
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
    val map = new TLongObjectHashMap[TestObj]()
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).value
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeTrove2Prealloc(reps: Int) = repeat(reps) {
    val map = trove2Prealloc
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeTrove3(reps: Int) = repeat(reps) {
    val map = new T3TLongObjectHashMap[TestObj]()
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeTrove3Prealloc(reps: Int) = repeat(reps) {
    val map = trove3Prealloc
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  def timeColt(reps: Int) = repeat(reps) {
    val map = new OpenLongObjectHashMap()
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).asInstanceOf[TestObj].value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timeColtPrealloc(reps: Int) = repeat(reps) {
    val map = coltPrealloc
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).asInstanceOf[TestObj].value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timePColt(reps: Int) = repeat(reps) {
    val map = new POpenLongObjectHashMap()
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).asInstanceOf[TestObj].value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  def timePColtPrealloc(reps: Int) = repeat(reps) {
    val map = pcoltPrealloc
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i).asInstanceOf[TestObj].value
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


//  def timeScalaDefault(reps: Int) = repeat(reps) {
//    var map = Map[Long, TestObj]()
//    var result : Long = 0
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        map += i -> new TestObj(i);
//        result += i
//    }
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        result += map(i).value
//      //      result += map.getOrElse(i + 1, 0)
//    }
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        map += i -> new TestObj(123)
//        result += i
//    }
//    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
//    result
//  }
//
//
//  def timeScalaMutable(reps: Int) = repeat(reps) {
//    var map = scala.collection.mutable.Map[Int, Int]()
//    var result : Long = 0
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        map += i -> i;
//        result += i
//    }
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        result += map(i)
//      //      result += map.getOrElse(i + 1, 0)
//    }
//    tfor(0)(_ < longdata.size(), _ + 1) {
//      i =>
//        map += i -> 123
//        result += i
//    }
//    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
//    result
//  }



  def timeJavaHashMap(reps: Int) = repeat(reps) {
    val map = new java.util.HashMap[Long, TestObj]()
    var result : Long = 0

    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(i))
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map.get(i.asInstanceOf[Long]).value // ok, i'm sure there's a great reason why i need to have to do this asInstanceOf[Long]
        // but without it, it actually returns null (as if key isn't found). Not sure what implicit magic is going on here...?
      //      result += map.get(i + 1)
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map.put(i, objdata.get(123))
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }


  /**
   * Realizing that our example is stupid (sequential list of ints? really??) and that this particular trivial case
   * should just be an array, i'm curious how an array compares.
   */
  def timeObjArray(reps: Int) = repeat(reps) {
    var map = new Array[TestObj](length)
    var result : Long = 0
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map(i) = objdata.get(i)
        result += i
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        result += map(i).asInstanceOf[TestObj].value
    }
    tfor(0)(_ < longdata.size(), _ + 1) {
      i =>
        map(i) = objdata.get(123)
        result += i
    }
    // the value of result doesn't matter...it's just there so that Hotspot doesn't optimize away our useless loops
    result
  }

  override def tearDown() {
    // clean up after yourself if required
  }


}


trait LongData {

  var longdata: java.util.List[java.lang.Long] = new ArrayList[java.lang.Long]()

  def initializeLongData(length: Int) {
    // this would be much prettier as a Map, but it's a Java collection, and this is easier.
    // the JavaConversion implicits mainly wrap Scala collections in wrappers that implement Java
    // interfaces, which isn't what we want.
    0 to (length-1) foreach { i : Int =>
      val in : java.lang.Long = i
      longdata.add(in)
    }
  }

}

trait ObjectData {

  // yes, it's just a list of numbers. But they're proper java.lang.Integers.
  var objdata: java.util.List[TestObj] = new ArrayList[TestObj]()


  def initializeObjectData(length: Int) {
    // this would be much prettier as a Map, but it's a Java collection, and this is easier.
    // the JavaConversion implicits mainly wrap Scala collections in wrappers that implement Java
    // interfaces, which isn't what we want.
    0 to (length-1) foreach { i : Int =>
      val in : java.lang.Long = i
      objdata.add(new TestObj(i))
    }
  }
}

class TestObj(val value: Long) {
  // nothing here.
}
