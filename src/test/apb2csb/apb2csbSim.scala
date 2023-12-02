package apb2csb

import DefineSim.SIMCFG
import NVDLA.apb2csb.NV_NVDLA_apb2csb
import org.scalatest.funsuite.AnyFunSuite
import spinal.core.sim._
import spinal.lib.bus.amba3.apb.sim.Apb3Driver

import scala.util.Random

class apb2csbSim extends AnyFunSuite{
  test("apb2csb"){
    SIMCFG(compress = true).compile {
      val dut = new NV_NVDLA_apb2csb()
      dut
    }.doSimUntilVoid {
      dut =>
        val clockDomain = dut.csbClock
        clockDomain.forkStimulus(10)

        dut.io.apb.PSEL #= 0
        dut.io.apb.PENABLE #= false
        dut.io.csb2nvdla.ready #= true
        dut.io.nvdla2csb.valid #= false
        clockDomain.waitSampling()

        var memory:Map[BigInt,BigInt] = Map()
        val driver = Apb3Driver(dut.io.apb,clockDomain)

        def read(addr:BigInt) = {
          dut.io.nvdla2csb.valid #= true
          dut.io.nvdla2csb.data #= memory(addr)
          driver.read(addr)
        }

        clockDomain.onSamplings {
          val write = dut.io.apb.PSEL.toBigInt == 1 && dut.io.apb.PWRITE.toBoolean && dut.io.apb.PREADY.toBoolean && dut.io.apb.PENABLE.toBoolean
          if (write) {
            memory += (dut.io.apb.PADDR.toBigInt -> dut.io.apb.PWDATA.toBigInt)
          }
        }

        for(idx <- 0 until 100){
          val randAddr = Random.nextInt(4096)
          val randData = Random.nextInt(4096)
          driver.write(randAddr ,randData)
          val rsp = read(randAddr)
          assert(rsp == randData)
        }
        simSuccess()
    }

  }

}
