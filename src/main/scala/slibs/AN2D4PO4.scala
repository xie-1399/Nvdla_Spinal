package slibs

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/**
 * the module is two simple Bool with and operation
 */
class AN2D4PO4 extends PrefixComponent{

  val io = new Bundle{
    val A1 = in Bool()
    val A2 = in Bool()
    val Z = out Bool()
  }

  io.Z := io.A1 & io.Z
}
