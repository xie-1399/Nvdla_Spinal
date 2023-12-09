package cdma.WT

import DefineSim.SIMCFG
import DefineSim.SpinalSim.PrefixComponent
import DefineUntils.Untils.CatInorder
import spinal.core._

// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

class NV_NVDLA_CDMA_WT_sp_arb extends PrefixComponent{

  val io = new Bundle{
    val req0 = in Bool()
    val req1 = in Bool()
    val gnt_busy = in Bool()
    val gnt0 = out Bool()
    val gnt1 = out Bool()
  }
  /* here just set a combine logic and get some regs pruned */

  val req = CatInorder(io.req0,io.req1) /* the cat is on the b ## a */
  val gnt_pre = Vec(Bool(),2)

  when(req.lsb){
    gnt_pre(0) := True
    gnt_pre(1) := False
  }.elsewhen(req.msb){
    gnt_pre(0) := False
    gnt_pre(1) := True
  }.otherwise{
    gnt_pre(0) := False
    gnt_pre(1) := False
  }

  io.gnt0 := gnt_pre(0) && (!io.gnt_busy)
  io.gnt1 := gnt_pre(1) && (!io.gnt_busy)
}

/* simple arbitration logic and the gnt 0 priority > gnt 1*/

object NV_NVDLA_CDMA_WT_sp_arb extends App{
  import spinal.core.sim._

  SIMCFG(compress = true).compile{
    val dut = new NV_NVDLA_CDMA_WT_sp_arb()
    dut
  }.doSimUntilVoid{
    dut =>
      dut.clockDomain.forkStimulus(10)
      dut.io.req0 #= false
      dut.io.req1 #= false
      dut.io.gnt0 #= false
      dut.clockDomain.waitSampling()
      def checkIt() = {
        (dut.io.req0.toBoolean, dut.io.req1.toBoolean, dut.io.gnt_busy.toBoolean) match {
          case (true,true,false) => assert(dut.io.gnt0.toBoolean && !dut.io.gnt1.toBoolean)
          case (true,false,false) => assert(dut.io.gnt0.toBoolean && !dut.io.gnt1.toBoolean)
          case (false,true,false) => assert(!dut.io.gnt0.toBoolean && dut.io.gnt1.toBoolean)
          case _ => assert(!dut.io.gnt0.toBoolean && !dut.io.gnt1.toBoolean)
        }
      }
      for(idx <- 0 until 1000){
        dut.io.req0.randomize()
        dut.io.req1.randomize()
        dut.io.gnt_busy.randomize()
        dut.clockDomain.waitSampling()
        checkIt()
      }
      simSuccess()
  }
}
