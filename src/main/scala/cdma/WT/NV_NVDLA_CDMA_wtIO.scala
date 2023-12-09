package cdma.WT

import config.nvdlaConfig
import spinal.core._
import spinal.lib._
import common._

/**
 *
 * all io bundle are declared as list
 */

case class NV_NVDLA_CDMA_wtIO(config:nvdlaConfig) extends Bundle {


    /* mcif */
    val cdma_wt2mcif_rd_req_pd = master Stream (UInt(config.NVDLA_MEM_RD_REQ bits)) /* 32 + 15 */
    val mcif2cdma_wt_rd_rsp_pd = slave Stream (UInt(config.NVDLA_MEM_RD_RSP bits))

    /* cvif */
    val cdma_wt2cvif_rd_req_pd = ifGen(config.NVDLA_SECONDARY_MEMIF_ENABLE) {
      master Stream (UInt(config.NVDLA_MEM_RD_REQ bits))
    }
    val cvif2cdma_wt_rd_rsp_pd = ifGen(config.NVDLA_SECONDARY_MEMIF_ENABLE) {
      slave Stream (UInt(config.NVDLA_MEM_RD_RSP bits))
    }


    val cdma2buf_wt_wr = master(nvdla_wr_if(17, config.DMAIF)) /* small-64 large-256 */

    val status2dma_fsm_switch = in Bool()
    val wt2status_state = out(UInt(2 bits))

}
