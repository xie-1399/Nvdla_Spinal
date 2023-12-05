package slibs

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

case class MulOperation(op1Width:Int,op2Width:Int) extends Bundle with IMasterSlave {
  val op1 = Bits(op1Width bits)
  val op2 = Bits(op2Width bits)
  override def asMaster(): Unit = {
    out(op1,op2)
  }
}

/* just use the * to calculate the mul */
/* here set a very common fire logic in the hardware */

class NV_NVDLA_MUL_unit(op1Width:Int,op2Width:Int,sign:Boolean = false) extends PrefixComponent{
  val dataType = if(sign) SInt(op1Width + op2Width bits) else UInt(op1Width + op2Width bits)
  val io = new Bundle{
    val ops = slave Stream(MulOperation(op1Width,op2Width))
    val res = master Stream(cloneOf(dataType))
  }

  val Mul = new Composite(this,"MulUnit"){

    val mul_out = if(sign) Reg(SInt(op1Width + op2Width bits)).init(0) else Reg(UInt(op1Width + op2Width bits)).init(0)
    val mul_valid = RegInit(False)

    io.ops.ready := io.res.isFree

    when(io.ops.fire){
      val mul_res = if(sign){io.ops.op1.asSInt * io.ops.op2.asSInt}else{io.ops.op1.asUInt * io.ops.op2.asUInt}
      mul_out := mul_res
    }

    when(io.ops.valid){
      mul_valid := True
    }.elsewhen(io.res.ready){
      mul_valid := False
    }

    io.res.valid := mul_valid
    io.res.payload := mul_out
  }

}

object NV_NVDLA_MUL_unit extends App{
  val rtl = new RtlConfig().GenRTL(new NV_NVDLA_MUL_unit(9,16,false))
}