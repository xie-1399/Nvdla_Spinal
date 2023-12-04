package cdma

import DefineSim.SIMCFG
import org.scalatest.funsuite.AnyFunSuite
import config._
import spinal.core.sim._

import scala.util.Random
import DefineSim.SimUntils._
import DefineSim.Logger._

/* simulation with the shared buffer about the img and dc data */
/* the read should be the same happen ( write can not be control */

class shared_bufferSim extends AnyFunSuite{
  test("shared buffer") {
    SIMCFG(compress = true).compile {
      val config = new nvdlaConfig()
      val dut = new NV_NVDLA_CDMA_shared_buffer(config)
      dut
    }.doSimUntilVoid{
     dut =>
      dut.clockDomain.forkStimulus(10)

      def init() ={
        dut.io.dc2sbuf_p_wr(0).addr.valid #= false
        dut.io.dc2sbuf_p_wr(1).addr.valid #= false
        dut.io.dc2sbuf_p_rd(0).addr.valid #= false
        dut.io.dc2sbuf_p_rd(1).addr.valid #= false

        dut.io.img2sbuf_p_wr(0).addr.valid #= false
        dut.io.img2sbuf_p_wr(1).addr.valid #= false
        dut.io.img2sbuf_p_rd(0).addr.valid #= false
        dut.io.img2sbuf_p_rd(1).addr.valid #= false
        dut.clockDomain.waitSampling()
      }

      def simulation(iter:Int = 100): Unit = {
        for(idx <- 0 until iter){
          val base  = Random.nextInt(208)
          val addrList = List(base,base + 16,base + 32,base + 48)
          val dataList = GenRandomList(0, 4096, 4)
          /* the write happens at the same time but not the same ram */
          dut.io.dc2sbuf_p_wr(0).addr.valid #= true
          dut.io.dc2sbuf_p_wr(1).addr.valid #= true
          dut.io.img2sbuf_p_wr(0).addr.valid #= true
          dut.io.img2sbuf_p_wr(1).addr.valid #= true
          dut.io.dc2sbuf_p_wr(0).addr.payload #= addrList(0)
          dut.io.dc2sbuf_p_wr(0).data #= dataList(0)
          dut.io.dc2sbuf_p_wr(1).addr.payload #= addrList(1)
          dut.io.dc2sbuf_p_wr(1).data #= dataList(1)
          dut.io.img2sbuf_p_wr(0).addr.payload #= addrList(2)
          dut.io.img2sbuf_p_wr(0).data #= dataList(2)
          dut.io.img2sbuf_p_wr(1).addr.payload #= addrList(3)
          dut.io.img2sbuf_p_wr(1).data #= dataList(3)
          dut.clockDomain.waitSampling()
          for((addr,idx) <- addrList.zipWithIndex){
            val ram = BigInt((HexStringWithWidth(addr.toLong.toBinaryString,8).substring(0,4)),2)
            val dep = BigInt((HexStringWithWidth(addr.toLong.toBinaryString,8).substring(4,8)),2)
            println(s"write ram $ram \t address ${dep} \t data ${dataList(idx)}")
          }
          println("=======================================")
          init()
          dut.io.dc2sbuf_p_rd(0).addr.payload #= addrList(0)
          dut.io.dc2sbuf_p_rd(1).addr.payload #= addrList(1)
          dut.io.img2sbuf_p_rd(0).addr.payload #= addrList(2)
          dut.io.img2sbuf_p_rd(1).addr.payload #= addrList(3)
          if(Random.nextInt(10) >= 5){
            dut.io.dc2sbuf_p_rd(0).addr.valid #= false
            dut.io.dc2sbuf_p_rd(1).addr.valid #= false
            dut.io.img2sbuf_p_rd(0).addr.valid #= true
            dut.io.img2sbuf_p_rd(1).addr.valid #= true
            dut.clockDomain.waitSampling()
            dut.io.img2sbuf_p_rd(0).addr.valid #= false
            dut.io.img2sbuf_p_rd(1).addr.valid #= false
            dut.clockDomain.waitSampling(2)
            assert(dut.io.img2sbuf_p_rd(0).data.toBigInt == dataList(2))
            assert(dut.io.img2sbuf_p_rd(1).data.toBigInt == dataList(3))
          }else{
            dut.io.dc2sbuf_p_rd(0).addr.valid #= true
            dut.io.dc2sbuf_p_rd(1).addr.valid #= true
            dut.io.img2sbuf_p_rd(0).addr.valid #= false
            dut.io.img2sbuf_p_rd(1).addr.valid #= false
            dut.clockDomain.waitSampling()
            dut.io.dc2sbuf_p_rd(0).addr.valid #= false
            dut.io.dc2sbuf_p_rd(1).addr.valid #= false
            dut.clockDomain.waitSampling(2)
            assert(dut.io.dc2sbuf_p_rd(0).data.toBigInt == dataList(0))
            assert(dut.io.dc2sbuf_p_rd(1).data.toBigInt == dataList(1))
          }

        }
      }

       init()
       simulation(10000)
       simSuccess()
    }
  }
}
