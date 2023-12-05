package slibs

/* copy from the https://github.com/soDLA-publishment/soDLA/blob/soDLA_beta/src/main/scala/nvdla/slibs/IS_pipe.scala#L9*/

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import config._


/* Todo build the Pipe later */

class IS_pipe(dataWidth:Int,config:nvdlaConfig) extends PrefixComponent{

  val io = new Bundle{
    /**
     *  -vi  : input valid signal name
     *  -di  : input data signal name
     *  -ro  : output ready signal name
     *  -vo  : output valid signal name
     *  -do  : output data signal name
     *  -ri  : input ready signal name
     */
    val dout = out(Bits(dataWidth bits))
    val vo = out(Bool())
    val ri = in(Bool())
    val di = in(Bits(dataWidth bits))
    val vi = in(Bool())
    val ro = out(Bool())
  }

  val ro_out = RegInit(True)
  val skid_flop_ro = RegInit(True)
  val skid_flop_vi = RegInit(False)
  val skid_flop_di = if (config.REGINIT_DATA) Reg(Bits(dataWidth bits)).init(0) else Reg(Bits(dataWidth bits))
  val pipe_skid_vi = RegInit(False)
  val pipe_skid_di = if (config.REGINIT_DATA) Reg(Bits(dataWidth bits)).init(0) else Reg(Bits(dataWidth bits))

  val skid_vi = Bool()
  val skid_di = Bits(dataWidth bits)
  val skid_ro = Bool()
  val pipe_skid_ro = Bool()
  val vo_out = Bool()
  val do_out = Bits(dataWidth bits)
  //skid ready
  ro_out := skid_ro
  skid_flop_ro := skid_ro
  //skid valid
  when(skid_flop_ro) {
    skid_flop_vi := io.vi
  }
  skid_vi := Mux(skid_flop_ro, io.vi, skid_flop_vi)
  //skid data
  when(skid_flop_ro & io.vi) {
    skid_flop_di := io.di
  }
  skid_di := Mux(skid_flop_ro, io.di, skid_flop_di)
  //pipe ready
  skid_ro := pipe_skid_ro || ~pipe_skid_vi
  //pipe valid
  when(skid_ro) {
    pipe_skid_vi := skid_vi
  }
  //pipe data
  when(skid_ro && skid_vi) {
    pipe_skid_di := skid_di
  }
  //pipe output
  pipe_skid_ro := io.ri
  vo_out := pipe_skid_vi
  do_out := pipe_skid_di

  io.ro := ro_out
  io.vo := vo_out
  io.dout := do_out
}

