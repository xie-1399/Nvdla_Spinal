package spec

import spinal.lib._
import spinal.core._

/* register control interface */
case class reg_control_if() extends Bundle{
  val rd_data = out(UInt(32 bits))
  val offset = in (UInt(12 bits))
  val wr_data = in(UInt(32 bits))
  val wr_en = in Bool()
}
