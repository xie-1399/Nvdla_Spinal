package nocif.MCIF


// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

import DefineSim.SpinalSim.PrefixComponent
import config._

import spinal.core._
import spinal.sim._

class NV_NVDLA_MCIF_CSB_reg(config:nvdlaConfig) extends PrefixComponent{
  val io = new Bundle{

    val idle = in Bool() /* Read-only register inputs */

    val reg = 

  }


}
