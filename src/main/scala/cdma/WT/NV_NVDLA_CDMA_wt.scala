package cdma.WT

import DefineSim.SpinalSim.PrefixComponent
import config._
import spinal.core._
import spinal.lib._
import common._


// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

/**
 * the cdma_wt will read the weight from the (mcif -> external dram)
 * @param config
 */

class NV_NVDLA_CDMA_wt(config:nvdlaConfig) extends PrefixComponent{

  val io = NV_NVDLA_CDMA_wtIO(config)

  /* CDMA weight fetching logic FSM  */



}
