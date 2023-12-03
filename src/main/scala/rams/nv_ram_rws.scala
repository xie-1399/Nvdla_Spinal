package rams

import DefineMem.MemOperation
import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import config._
import spinal.core.sim._
/* this is copy from https://github.com/soDLA-publishment/soDLA/blob/soDLA_beta/src/main/scala/nvdla/rams/nv_ram_rws.scala */


class nv_ram_rws(config:nvdlaConfig,depth:Int,width:Int,asic:Boolean = false) extends PrefixComponent{
  val io = new Bundle{
    /* control */
    val re = in Bool()
    val we = in Bool()

    /* data */
    val ra = in UInt(log2Up(depth) bits)
    val wa = in UInt(log2Up(depth) bits)
    val di = in Bits(width bits)
    val dout = out Bits(width bits)

  }

  val ramArea = new Area {
    if(!asic){
      /* using the vec to create */
      val mem = if(config.REGINIT_DATA) RegInit(Vec(Seq.fill(depth)(B(0,width bits)))) else Reg(Vec(Bits(32 bits),depth))
      val ra_d = if(config.REGINIT_DATA) RegInit(U(0,log2Up(depth) bits)) else Reg(UInt(log2Up(depth) bits))
      when(io.we) {
        mem(io.wa) := io.di
      }
      when(io.re) {
        ra_d := io.ra
      }
      io.dout := mem(ra_d)
    }
    else{
      val mem = Mem(Bits(width bits),depth)
      if(config.NVDLA_ASIC_MEM_SIM) mem.simPublic()
      mem.write(io.wa,io.di,enable = io.we)
      io.dout := mem.readSync(io.ra,enable = io.re)
    }
  }
}

object nv_ram_rws extends App{
  val rtl = new RtlConfig("rtl/rams").GenRTL(new nv_ram_rws(new nvdlaConfig(),32,8))
  //val rtl_asic = new RtlConfig("rtl/rams").GenRTL(new nv_ram_rws(new nvdlaConfig(),32,8,true))
}