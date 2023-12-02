package cdma

import DefineSim.SpinalSim.PrefixComponent
import spinal.core._
import spinal.lib._
import config.nvdlaConfig

// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

/**
 * the shared buffer will store and read the img data and Dc(feature map) data
 * the clock domain is on the nvdla core clock damain
 * the default : 16 * 256 * 8 byte RAMs as local shared buffers
 */

/* write interface from the sbuf */
case class nvdla_wr_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow(UInt(addrWidth bits))
  val data = UInt(dataWidth bits)

  override def asMaster(): Unit = {
    master(addr)
    out(data)
  }
}

/* read interface from the sbuf */
case class nvdla_rd_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow(UInt(addrWidth bits))
  val data = in (UInt(dataWidth bits))

  override def asMaster() = {
    in(data)
    master(addr)
  }
}


class NV_NVDLA_CDMA_shared_buffer(config:nvdlaConfig)  extends PrefixComponent{

  val io = new Bundle{

    val pwrbus_ram_pd = in UInt(32 bits)

    /* write port */
    val dc2sbuf_p_wr = Vec(slave(nvdla_wr_if(8,config.CDMA_SBUF_SDATA_BITS)),2)
    val img2sbuf_p_wr = Vec(slave(nvdla_wr_if(8,config.CDMA_SBUF_SDATA_BITS)),2)

    /* read port */
    val dc2sbuf_p_rd = Vec(slave(nvdla_rd_if(8,config.CDMA_SBUF_SDATA_BITS)),2)
    val img2sbuf_p_rd = Vec(slave(nvdla_rd_if(8,config.CDMA_SBUF_SDATA_BITS)),2)

  }

  /* CDMA_SBUF_DEPTH : 256  CDMA_SBUF_NUMBER : 16*/
  val b1 = log2Up(config.CDMA_SBUF_DEPTH)
  val b0 = log2Up(config.CDMA_SBUF_DEPTH) - log2Up(config.CDMA_SBUF_NUMBER)

  val dc2sbuf_p0_wr_bsel = io.dc2sbuf_p_wr(0).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p0_wr_bsel = io.img2sbuf_p_wr(0).addr.payload(b1 - 1 downto b0)
  val dc2sbuf_p1_wr_bsel = io.img2sbuf_p_wr(1).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p1_wr_bsel = io.img2sbuf_p_wr(1).addr.payload(b1 - 1 downto b0)

  /* the high 4 bits is used to choose which ram */
  val dc2sbuf_p0_wr_sel = Vec.fill(config.CDMA_SBUF_NUMBER)(Bool())
  (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p0_wr_bsel === U(i)) & (io.dc2sbuf_p_wr(0).addr.valid))

  val dc2sbuf_p1_wr_sel = Vec.fill(config.CDMA_SBUF_NUMBER)(Bool())
  (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p1_wr_bsel === U(i)) & (io.dc2sbuf_p_wr(1).addr.valid))

  val img2sbuf_p0_wr_sel = Vec.fill(config.CDMA_SBUF_NUMBER)(Bool())
  (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p0_wr_bsel === U(i)) & (io.img2sbuf_p_wr(0).addr.valid))

  val img2sbuf_p1_wr_sel = Vec.fill(config.CDMA_SBUF_NUMBER)(Bool())
  (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p1_wr_bsel === U(i)) & (io.img2sbuf_p_wr(1).addr.valid))

  val sbuf_we = Vec.fill(config.CDMA_SBUF_NUMBER)(Bool())
  (0 until config.CDMA_SBUF_NUMBER).map(i => dc2sbuf_p0_wr_sel(i) || dc2sbuf_p1_wr_sel(i) || img2sbuf_p0_wr_sel(i) || img2sbuf_p1_wr_sel(i) )



}
