package slibs

/* copy from https://github.com/soDLA-publishment/soDLA/blob/soDLA_beta/src/main/scala/nvdla/slibs/NV_NVDLA_HLS_relu.scala */

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/* simple relu functions list need to test later */

class NV_NVDLA_relu(dataWidth:Int = 32) extends PrefixComponent{

  val io = new Bundle{
    val dataIn = in Bits(dataWidth bits)
    val dataOut = out UInt(dataWidth bits)
  }

  val data_in_sign = io.dataIn.msb

  when(data_in_sign){
    io.dataOut := io.dataIn.asUInt
  }.otherwise{
    io.dataOut := 0
  }
}

/* the prelu func can look https://zhuanlan.zhihu.com/p/71601989 */
class NV_NVDLA_prelu(inWidth:Int = 32,outWidth:Int = 64, opWidth:Int = 32) extends PrefixComponent{
   val io = new Bundle{
     val prelu_en = in Bool()
     val dataIn = in Bits(inWidth bits)
     val op = in Bits (opWidth bits)
     val dataOut = out UInt(outWidth bits)
  }

  val sign = io.dataIn.msb

  when(io.prelu_en && !sign){
    io.dataOut := io.dataIn.asUInt
  }.otherwise{
    io.dataOut := (io.dataIn.asSInt * io.op.asSInt).asUInt
  }

}


