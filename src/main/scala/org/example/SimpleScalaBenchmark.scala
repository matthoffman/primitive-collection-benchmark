package org.example

import com.google.caliper.SimpleBenchmark
import annotation.tailrec

trait SimpleScalaBenchmark extends SimpleBenchmark {
  
  // helper method to keep the actual benchmarking methods a bit cleaner
  // your code snippet should always return a value that cannot be "optimized away"
  def repeat[@specialized A](reps: Int)(snippet: => A) = {
    val zero = 0.asInstanceOf[A] // looks wierd but does what it should: init w/ default value in a fully generic way
    var i = 0
    var result = zero
    while (i < reps) {
      val res = snippet 
      if (res != zero) result = res // make result depend on the benchmarking snippet result 
      i = i + 1
    }
    result
  }


  /**
   *   this is a scala version of Javas "for" loop. It happens to be significantly more efficient than Scala's default "for i <- Iter" syntax.
   *   normally, the difference doesn't matter much, but since we're testing very small things, it seems worth it to be
   *   as efficient in the test harness as possible.
   */
  @tailrec
  final def tfor[@specialized T](i: T)(test: T => Boolean, inc: T => T)(f: T => Unit) {
    if(test(i)) {
      f(i)
      tfor(inc(i))(test, inc)(f)
    }
  }

}