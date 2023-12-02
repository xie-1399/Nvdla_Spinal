package cdma

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

/* */

class NV_NVDLA_CDMA_regfile extends PrefixComponent{

  val io = new Bundle{
    //general clock
    val nvdla_core_clk = in Bool()

    

  }


}
