package cdma

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._

/* */

class NV_NVDLA_CDMA_regfile extends PrefixComponent{

  val io = new Bundle{
    //general clock
    val nvdla_core_clk = in Bool()

    

  }


}
