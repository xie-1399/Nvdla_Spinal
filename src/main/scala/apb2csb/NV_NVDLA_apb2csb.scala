package NVDLA.apb2csb

// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import spinal.lib.bus.amba3.apb.Apb3

/*
  * this module can let the apb bus bundle to the csb request
  * the apb bus write and read request can be send to the dla
*/


/* define the config bus bundle here */
case class csb2nvdla_if() extends Bundle{
  val addr = out(UInt(16 bits))
  val wdat = out(UInt(32 bits))
  val write = out(Bool())
  val nposted = out(Bool())
}

case class nvdla2csb_if() extends Bundle{
  val data = out(UInt(32 bits))
}


class NV_NVDLA_apb2csb extends PrefixComponent{
  val io = new Bundle{

    val pclk = in(Bool())
    val prstn = in (Bool())
    val apb = slave(Apb3(32,32))

    val csb2nvdla = master (Stream(csb2nvdla_if()))
    val nvdla2csb = slave (Flow(nvdla2csb_if()))
  }

  val csbClock = ClockDomain(
    clock = io.pclk,
    reset = io.prstn,
    config = ClockDomainConfig(
      clockEdge = RISING,
      resetKind = ASYNC,
      resetActiveLevel = LOW
    )
  )

  val csbArea = new ClockingArea(csbClock){
    val rd_trans_low = RegInit(False)

    val wr_trans_vld = io.apb.PSEL.asBool && io.apb.PENABLE && io.apb.PWRITE
    val rd_trans_vld = io.apb.PSEL.asBool && io.apb.PENABLE && !io.apb.PWRITE

    when(io.nvdla2csb.valid && rd_trans_low){
      rd_trans_low := False
    }elsewhen(io.csb2nvdla.ready && rd_trans_vld){
      rd_trans_low := True
    }

    io.csb2nvdla.valid := wr_trans_vld || (rd_trans_vld && (!rd_trans_low))
    io.csb2nvdla.addr := io.apb.PADDR(17 downto 2)
    io.csb2nvdla.wdat := io.apb.PWDATA.asUInt
    io.csb2nvdla.write := io.apb.PWRITE
    io.csb2nvdla.nposted := False
    io.apb.PSLVERROR := False

    io.apb.PRDATA := io.nvdla2csb.data.asBits

    /* the same as !(wr_trans_vld &&(!io.csb2nvdla.ready) || rd_trans_vld && (!io.nvdla2csb.valid)) */
    io.apb.PREADY := ~(wr_trans_vld&(~io.csb2nvdla.ready)|rd_trans_vld&(~io.nvdla2csb.valid))
  }

}


object NV_NVDLA_apb2csb extends App{
  val rtl = new RtlConfig(path = "rtl/apb2csb").GenRTL(new NV_NVDLA_apb2csb(),pruned = false)
}