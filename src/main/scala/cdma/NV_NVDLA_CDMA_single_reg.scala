package cdma
// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

/**
 * here set the single cdma reg format like this
 */

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._

/* register control interface */
case class reg_control_if() extends Bundle{
  val wr_en = in Bool()
  val wr_data = in UInt(32 bits)
  val offset = in UInt(12 bits)
  val rd_data = out(UInt(32 bits))
}


class NV_NVDLA_CDMA_single_reg extends PrefixComponent{

  val io = new Bundle{
    /* the clock domain using the core clock */
    val reg = reg_control_if()
    // Writable register flop/trigger outputs
    val producer = out Bool()
    val arb_weight = out UInt(4 bits)
    val arb_wmb = out UInt(4 bits)

    // Read-only register inputs
    val flush_done = in Bool()
    val consumer = in Bool()
    val status_0 = in UInt(2 bits)
    val status_1 = in UInt(2 bits)

  }

  /* decode address */
  val H8 = U("h8").resize(12) /* WMB and Weight share same port to access external memory. This register controls the weight factor in the arbiter. */
  val HC = U("hc").resize(12) /* Indicates whether CBUF flush is finished after reset. */
  val H4 = U("h4").resize(12) /* Pointer for CSB master and data path to access groups */
  val H0 = U("h0").resize(12) /* Idle status of two register groups */

  val nvdla_cdma_s_arbiter_0_wren = (io.reg.offset === H8) & io.reg.wr_en
  val nvdla_cdma_s_cbuf_flush_status_0_wren = (io.reg.offset === HC) & io.reg.wr_en
  val nvdla_cdma_s_pointer_0_wren = (io.reg.offset === H4) & io.reg.wr_en
  val nvdla_cdma_s_status_0_wren = (io.reg.offset === H0) & io.reg.wr_en

  io.reg.rd_data := io.reg.offset.mux(
    H8 -> Cat(U"b0".resize(12),io.arb_wmb,U"b0".resize(15),io.producer).asUInt,
    HC -> Cat(U"b0".resize(31),io.flush_done).asUInt,
    H4 -> Cat(U"b0".resize(15),io.consumer,U"b0".resize(15),io.producer).asUInt,
    H0 -> Cat(U"b0".resize(14),io.status_1,U"b0".resize(14),io.status_0).asUInt,
    default -> U(0,32 bits)
  )

  io.arb_weight := RegNextWhen(io.reg.wr_data(3 downto 0),init = U"b1111",cond = nvdla_cdma_s_arbiter_0_wren)
  io.arb_wmb := RegNextWhen(io.reg.wr_data(19 downto 16),init = U"b0011",cond = nvdla_cdma_s_arbiter_0_wren)
  io.producer := RegNextWhen(io.reg.wr_data(0),init = False,cond = nvdla_cdma_s_pointer_0_wren)
}

object NV_NVDLA_CDMA_single_reg extends App{
  val rtl = new RtlConfig(path = "rtl/cdma").GenRTL(new NV_NVDLA_CDMA_single_reg())
}