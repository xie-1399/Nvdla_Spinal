package rams

// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/*
* add some rams which is copy like from  https://github.com/soDLA-publishment/soDLA/blob/soDLA_beta/src/main/scala/nvdla/rams/nv_flopram.scala
* */


class nv_flopram(depth:Int,width:Int,wr_reg:Boolean = false) extends PrefixComponent{

    val io = new Bundle{
      val clk = in Bool()
      val clk_mgated = ifGen(wr_reg){in Bool()}
      val iwe = ifGen(wr_reg){in Bool()}

      val di = in UInt(width bits)
      val we = in Bool()
      val wa = ifGen(depth > 1){in UInt(log2Up(depth) bits)}
      val ra = in UInt(log2Up(depth + 1) bits)
      val dout = out(UInt(width bits))

      val pwrbus_ram_pd = in(UInt(32 bits))
  }

  val internal_clk = Bool()

  if(wr_reg){
    internal_clk := io.clk_mgated
  }else{
    internal_clk := io.clk
  }
  val internalClock = ClockDomain(clock = internal_clk)


  val flopramArea = new ClockingArea(internalClock){

  }

}
