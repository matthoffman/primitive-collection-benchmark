package org.mhoffman

import com.google.caliper.{Runner => CaliperRunner}

object Runner {

  // main method for IDEs, from the CLI you can also run the caliper Runner directly
  // or simply use SBTs "run" action
  def main(args: Array[String]) {
    // we simply pass in the CLI args,
    // we could of course also just pass hardcoded arguments to the caliper Runner
    println("Running with args "+ args.mkString(","))

    println("Running the Int,Int benchmark")
    CaliperRunner.main(classOf[IntIntBenchmark], args)

//    println("Running the Long,Object benchmark")
//    CaliperRunner.main(classOf[LongObjectBenchmark], args)
  }
  
}
