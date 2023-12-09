package common

import spinal.core._
import spinal.lib._

/* register control interface */
case class reg_control_if() extends Bundle{
  val wr_en = in Bool()
  val wr_data = in UInt(32 bits)
  val offset = in UInt(12 bits)
  val rd_data = out(UInt(32 bits))
}


/* the nvdla write interface */
case class nvdla_wr_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow (UInt())
  val data = Bits(dataWidth bits)

  override def asMaster(): Unit = {
    master(addr)
    out(data)
  }
}

/* the nvdla read interface */
case class nvdla_rd_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow(UInt())
  val data = Bits(dataWidth bits)

  override def asMaster(): Unit = {
    master(addr)
    in(data)
  }
}