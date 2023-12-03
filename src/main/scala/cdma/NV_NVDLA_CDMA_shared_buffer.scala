package cdma

import DefineSim.SpinalSim.{PrefixComponent, RtlConfig}
import spinal.core._
import spinal.lib._
import config.nvdlaConfig
import rams._
// ================================================================
// NVDLA Open Source Project
//
// Copyright(c) 2016 - 2017 NVIDIA Corporation.  Licensed under the
// NVDLA Open Hardware License; Check "LICENSE" which comes with
// this distribution for more information.
// ================================================================

/**
 * the shared buffer will store and read the img data and Dc(feature map) data
 * the clock domain is on the NVDLA core clock domain
 * the default : 16 * 16 * 8 byte RAMs as local shared buffers
 * 4 ports : 2 ports are used for dc data and 2 ports are used for the img data
 * only support the dc and img data into the buffer
 * Todo rebuild it */

/* write interface from the sbuf */
case class nvdla_wr_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow(UInt(addrWidth bits))
  val data = Bits(dataWidth bits)

  override def asMaster(): Unit = {
    master(addr)
    out(data)
  }
}

/* read interface from the sbuf */
case class nvdla_rd_if(addrWidth:Int,dataWidth:Int) extends Bundle with IMasterSlave {
  val addr = Flow(UInt(addrWidth bits))
  val data = in (Bits(dataWidth bits))

  override def asMaster() = {
    in(data)
    master(addr)
  }
}


class NV_NVDLA_CDMA_shared_buffer(config:nvdlaConfig)  extends PrefixComponent {

  val io = new Bundle {

    /* write port */
    val dc2sbuf_p_wr = Vec(slave(nvdla_wr_if(8, config.CDMA_SBUF_SDATA_BITS)), 2)
    val img2sbuf_p_wr = Vec(slave(nvdla_wr_if(8, config.CDMA_SBUF_SDATA_BITS)), 2)

    /* read port */
    val dc2sbuf_p_rd = Vec(slave(nvdla_rd_if(8, config.CDMA_SBUF_SDATA_BITS)), 2)
    val img2sbuf_p_rd = Vec(slave(nvdla_rd_if(8, config.CDMA_SBUF_SDATA_BITS)), 2)
  }

  /* CDMA_SBUF_DEPTH : 256  CDMA_SBUF_NUMBER : 16*/
  val b1 = log2Up(config.CDMA_SBUF_DEPTH)
  val b0 = log2Up(config.CDMA_SBUF_DEPTH) - log2Up(config.CDMA_SBUF_NUMBER)

  val dc2sbuf_p0_wr_bsel = io.dc2sbuf_p_wr(0).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p0_wr_bsel = io.img2sbuf_p_wr(0).addr.payload(b1 - 1 downto b0)
  val dc2sbuf_p1_wr_bsel = io.img2sbuf_p_wr(1).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p1_wr_bsel = io.img2sbuf_p_wr(1).addr.payload(b1 - 1 downto b0)

  /* the high 4 bits is used to choose which ram */
  val dc2sbuf_p0_wr_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p0_wr_bsel === U(i)) & (io.dc2sbuf_p_wr(0).addr.valid))
  val dc2sbuf_p1_wr_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p1_wr_bsel === U(i)) & (io.dc2sbuf_p_wr(1).addr.valid))
  val img2sbuf_p0_wr_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p0_wr_bsel === U(i)) & (io.img2sbuf_p_wr(0).addr.valid))
  val img2sbuf_p1_wr_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p1_wr_bsel === U(i)) & (io.img2sbuf_p_wr(1).addr.valid))

  // write enable
  val sbuf_we = (0 until config.CDMA_SBUF_NUMBER).map(i => dc2sbuf_p0_wr_sel(i) || dc2sbuf_p1_wr_sel(i) || img2sbuf_p0_wr_sel(i) || img2sbuf_p1_wr_sel(i))

  //write address
  val sbuf_wa = (0 until config.CDMA_SBUF_NUMBER).map {
    i =>
      ((Repeat(dc2sbuf_p0_wr_sel(i), b0) & io.dc2sbuf_p_wr(0).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(dc2sbuf_p1_wr_sel(i), b0) & io.dc2sbuf_p_wr(1).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(img2sbuf_p0_wr_sel(i), b0) & io.img2sbuf_p_wr(0).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(img2sbuf_p1_wr_sel(i), b0) & io.img2sbuf_p_wr(1).addr.payload(b0 - 1 downto 0).asBits)).asUInt
  }

  val sbuf_wdat = (0 until config.CDMA_SBUF_NUMBER).map {
    i =>
      (Repeat(dc2sbuf_p0_wr_sel(i), config.CDMA_SBUF_SDATA_BITS) & io.dc2sbuf_p_wr(0).data.asBits) |
        (Repeat(dc2sbuf_p1_wr_sel(i), config.CDMA_SBUF_SDATA_BITS) & io.dc2sbuf_p_wr(1).data.asBits) |
        (Repeat(img2sbuf_p0_wr_sel(i), config.CDMA_SBUF_SDATA_BITS) & io.img2sbuf_p_wr(0).data.asBits) |
        (Repeat(img2sbuf_p1_wr_sel(i), config.CDMA_SBUF_SDATA_BITS) & io.img2sbuf_p_wr(1).data.asBits)
  }


  /* read port is the same as the write port */
  val dc2sbuf_p0_rd_bsel = io.dc2sbuf_p_rd(0).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p0_rd_bsel = io.img2sbuf_p_rd(0).addr.payload(b1 - 1 downto b0)
  val dc2sbuf_p1_rd_bsel = io.dc2sbuf_p_rd(1).addr.payload(b1 - 1 downto b0)
  val img2sbuf_p1_rd_bsel = io.img2sbuf_p_rd(1).addr.payload(b1 - 1 downto b0)

  val dc2sbuf_p0_rd_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p0_rd_bsel === U(i)) & (io.dc2sbuf_p_rd(0).addr.valid))
  val dc2sbuf_p1_rd_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (dc2sbuf_p1_rd_bsel === U(i)) & (io.dc2sbuf_p_rd(1).addr.valid))
  val img2sbuf_p0_rd_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p0_rd_bsel === U(i)) & (io.img2sbuf_p_rd(0).addr.valid))
  val img2sbuf_p1_rd_sel = (0 until config.CDMA_SBUF_NUMBER).map(i => (img2sbuf_p1_rd_bsel === U(i)) & (io.img2sbuf_p_rd(1).addr.valid))

  //read enable
  val sbuf_p0_re = (0 until config.CDMA_SBUF_NUMBER).map(i => dc2sbuf_p0_rd_sel(i) | img2sbuf_p0_rd_sel(i))
  val sbuf_p1_re = (0 until config.CDMA_SBUF_NUMBER).map(i => dc2sbuf_p1_rd_sel(i) | img2sbuf_p1_rd_sel(i))
  val sbuf_re = (0 until config.CDMA_SBUF_NUMBER).map(i => sbuf_p0_re(i) | sbuf_p1_re(i))
  //read address
  val sbuf_ra = (0 until config.CDMA_SBUF_NUMBER).map {
    i =>
      ((Repeat(dc2sbuf_p0_rd_sel(i), b0) & io.dc2sbuf_p_rd(0).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(dc2sbuf_p1_rd_sel(i), b0) & io.dc2sbuf_p_rd(1).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(img2sbuf_p0_rd_sel(i), b0) & io.img2sbuf_p_rd(0).addr.payload(b0 - 1 downto 0).asBits) |
        (Repeat(img2sbuf_p1_rd_sel(i), b0) & io.img2sbuf_p_rd(1).addr.payload(b0 - 1 downto 0).asBits)).asUInt
  }

  val shareBuffer = new Area {
    val buffer = Array.fill(config.CDMA_SBUF_NUMBER) {
      /* using the asic build mem */
      new nv_ram_rws(config = config, depth = config.CDMA_SBUF_DEPTH / config.CDMA_SBUF_NUMBER, width = config.CDMA_SBUF_SDATA_BITS, asic = true)
    }

    val sbuf_rdat = Vec(Bits(config.CDMA_SBUF_SDATA_BITS bits), config.CDMA_SBUF_NUMBER)
    for (idx <- 0 until config.CDMA_SBUF_NUMBER) {
      /* connect them */
      buffer(idx).io.re := sbuf_re(idx)
      buffer(idx).io.we := sbuf_we(idx)
      buffer(idx).io.ra := sbuf_ra(idx)
      buffer(idx).io.wa := sbuf_wa(idx)
      buffer(idx).io.di := sbuf_wdat(idx)
      sbuf_rdat(idx) := buffer(idx).io.dout
    }

    /* ram stage1 */
    val sbuf_p0_re_norm_d1 = Vec(RegInit(False), config.CDMA_SBUF_NUMBER)
    val sbuf_p1_re_norm_d1 = Vec(RegInit(False), config.CDMA_SBUF_NUMBER)
    for (i <- 0 to config.CDMA_SBUF_NUMBER - 1) {
      sbuf_p0_re_norm_d1(i) := sbuf_p0_re(i)
      sbuf_p1_re_norm_d1(i) := sbuf_p1_re(i)
    }
    val sbuf_p0_rd_en_d1 = RegNext(io.dc2sbuf_p_rd(0).addr.valid | io.img2sbuf_p_rd(0).addr.valid, False)
    val sbuf_p1_rd_en_d1 = RegNext(io.dc2sbuf_p_rd(1).addr.valid | io.img2sbuf_p_rd(1).addr.valid, False)


    /* ram stage2 */
    val sbuf_p0_rdat = (0 to config.CDMA_SBUF_NUMBER - 1).map {
      i => Repeat(sbuf_p0_re_norm_d1(i), config.CDMA_SBUF_SDATA_BITS) & sbuf_rdat(i)
    }.reduce(_ | _)

    val sbuf_p1_rdat = (0 to config.CDMA_SBUF_NUMBER - 1).map {
      i => Repeat(sbuf_p1_re_norm_d1(i), config.CDMA_SBUF_SDATA_BITS) & sbuf_rdat(i)
    }.reduce(_ | _)

    val sbuf_p0_rdat_d2 = RegNextWhen(sbuf_p0_rdat, sbuf_p0_rd_en_d1)
    val sbuf_p1_rdat_d2 = RegNextWhen(sbuf_p1_rdat, sbuf_p1_rd_en_d1)
    io.dc2sbuf_p_rd(0).data := sbuf_p0_rdat_d2
    io.img2sbuf_p_rd(0).data := sbuf_p0_rdat_d2
    io.dc2sbuf_p_rd(1).data := sbuf_p1_rdat_d2
    io.img2sbuf_p_rd(1).data := sbuf_p1_rdat_d2
  }


}

object NV_NVDLA_CDMA_shared_buffer extends App{
  val rtl = new RtlConfig(path = "rtl/cdma").GenRTL(new NV_NVDLA_CDMA_shared_buffer(config = new nvdlaConfig))
}
