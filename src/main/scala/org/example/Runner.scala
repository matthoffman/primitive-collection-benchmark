package org.example

import com.google.caliper.{Runner => CaliperRunner}

object Runner {

  // main method for IDEs, from the CLI you can also run the caliper Runner directly
  // or simply use SBTs "run" action
  def main(args: Array[String]) {
    // we simply pass in the CLI args,
    // we could of course also just pass hardcoded arguments to the caliper Runner
    println("Running with args "+ args.mkString(","))
    println("First, running the Int,Int benchmark")
    CaliperRunner.main(classOf[Benchmark], args)
    println("Next, running the Long,Object benchmark")
    CaliperRunner.main(classOf[LongObjectBenchmark], args)
  }
  
}