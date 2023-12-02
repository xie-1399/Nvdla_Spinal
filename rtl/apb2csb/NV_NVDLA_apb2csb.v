// Generator : SpinalHDL v1.9.4    git head : 270018552577f3bb8e5339ee2583c9c22d324215
// Component : NV_NVDLA_apb2csb
// Git hash  : 0cde351c5d300a33a4e894fb9bfe1d0ec574ccb6

`timescale 1ns/1ps

module NV_NVDLA_apb2csb (
  input  wire          pclk,
  input  wire          prstn,
  input  wire [31:0]   apb_PADDR,
  input  wire [0:0]    apb_PSEL,
  input  wire          apb_PENABLE,
  output wire          apb_PREADY,
  input  wire          apb_PWRITE,
  input  wire [31:0]   apb_PWDATA,
  output wire [31:0]   apb_PRDATA,
  output wire          apb_PSLVERROR,
  output wire          csb2nvdla_valid,
  input  wire          csb2nvdla_ready,
  output wire [15:0]   csb2nvdla_payload_addr,
  output wire [31:0]   csb2nvdla_payload_wdat,
  output wire          csb2nvdla_payload_write,
  output wire          csb2nvdla_payload_nposted,
  input  wire          nvdla2csb_valid,
  input  wire [31:0]   nvdla2csb_payload_data
);

  reg                 csbArea_rd_trans_low;
  wire                csbArea_wr_trans_vld;
  wire                csbArea_rd_trans_vld;
  wire                when_NV_NVDLA_apb2csb_l61;
  wire                when_NV_NVDLA_apb2csb_l63;

  assign csbArea_wr_trans_vld = ((apb_PSEL[0] && apb_PENABLE) && apb_PWRITE);
  assign csbArea_rd_trans_vld = ((apb_PSEL[0] && apb_PENABLE) && (! apb_PWRITE));
  assign when_NV_NVDLA_apb2csb_l61 = (nvdla2csb_valid && csbArea_rd_trans_low);
  assign when_NV_NVDLA_apb2csb_l63 = (csb2nvdla_ready && csbArea_rd_trans_vld);
  assign csb2nvdla_valid = (csbArea_wr_trans_vld || (csbArea_rd_trans_vld && (! csbArea_rd_trans_low)));
  assign csb2nvdla_payload_addr = apb_PADDR[17 : 2];
  assign csb2nvdla_payload_wdat = apb_PWDATA;
  assign csb2nvdla_payload_write = apb_PWRITE;
  assign csb2nvdla_payload_nposted = 1'b0;
  assign apb_PSLVERROR = 1'b0;
  assign apb_PRDATA = nvdla2csb_payload_data;
  assign apb_PREADY = (! ((csbArea_wr_trans_vld && (! csb2nvdla_ready)) || (csbArea_rd_trans_vld && (! nvdla2csb_valid))));
  always @(posedge pclk or negedge prstn) begin
    if(!prstn) begin
      csbArea_rd_trans_low <= 1'b0;
    end else begin
      if(when_NV_NVDLA_apb2csb_l61) begin
        csbArea_rd_trans_low <= 1'b0;
      end else begin
        if(when_NV_NVDLA_apb2csb_l63) begin
          csbArea_rd_trans_low <= 1'b1;
        end
      end
    end
  end


endmodule
