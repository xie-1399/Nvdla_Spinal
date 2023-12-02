// Generator : SpinalHDL v1.9.4    git head : 270018552577f3bb8e5339ee2583c9c22d324215
// Component : NV_NVDLA_CDMA_single_reg
// Git hash  : 0cde351c5d300a33a4e894fb9bfe1d0ec574ccb6

`timescale 1ns/1ps

module NV_NVDLA_CDMA_single_reg (
  output wire [31:0]   reg_rd_data,
  input  wire [11:0]   reg_offset,
  input  wire [31:0]   reg_wr_data,
  input  wire          reg_wr_en,
  output wire          producer,
  output wire [3:0]    arb_weight,
  output wire [3:0]    arb_wmb,
  input  wire          flush_done,
  input  wire          consumer,
  input  wire [1:0]    status_0,
  input  wire [1:0]    status_1,
  input  wire          clk,
  input  wire          reset
);

  wire       [3:0]    _zz_H8;
  wire       [3:0]    _zz_HC;
  wire       [3:0]    _zz_H4;
  wire       [3:0]    _zz_H0;
  wire       [11:0]   _zz__zz_reg_rd_data;
  wire       [0:0]    _zz__zz_reg_rd_data_1;
  wire       [14:0]   _zz__zz_reg_rd_data_2;
  wire       [0:0]    _zz__zz_reg_rd_data_3;
  wire       [30:0]   _zz__zz_reg_rd_data_4;
  wire       [0:0]    _zz__zz_reg_rd_data_5;
  wire       [14:0]   _zz__zz_reg_rd_data_6;
  wire       [0:0]    _zz__zz_reg_rd_data_7;
  wire       [14:0]   _zz__zz_reg_rd_data_8;
  wire       [0:0]    _zz__zz_reg_rd_data_9;
  wire       [13:0]   _zz__zz_reg_rd_data_10;
  wire       [0:0]    _zz__zz_reg_rd_data_11;
  wire       [13:0]   _zz__zz_reg_rd_data_12;
  wire       [0:0]    _zz__zz_reg_rd_data_13;
  wire       [11:0]   H8;
  wire       [11:0]   HC;
  wire       [11:0]   H4;
  wire       [11:0]   H0;
  wire                nvdla_cdma_s_arbiter_0_wren;
  wire                nvdla_cdma_s_cbuf_flush_status_0_wren;
  wire                nvdla_cdma_s_pointer_0_wren;
  wire                nvdla_cdma_s_status_0_wren;
  reg        [31:0]   _zz_reg_rd_data;
  reg        [3:0]    _zz_arb_weight;
  reg        [3:0]    _zz_arb_wmb;
  reg                 _zz_producer;

  assign _zz_H8 = 4'b1000;
  assign _zz_HC = 4'b1100;
  assign _zz_H4 = 4'b0100;
  assign _zz_H0 = 4'b0000;
  assign _zz__zz_reg_rd_data_1 = 1'b0;
  assign _zz__zz_reg_rd_data = {11'd0, _zz__zz_reg_rd_data_1};
  assign _zz__zz_reg_rd_data_3 = 1'b0;
  assign _zz__zz_reg_rd_data_2 = {14'd0, _zz__zz_reg_rd_data_3};
  assign _zz__zz_reg_rd_data_5 = 1'b0;
  assign _zz__zz_reg_rd_data_4 = {30'd0, _zz__zz_reg_rd_data_5};
  assign _zz__zz_reg_rd_data_7 = 1'b0;
  assign _zz__zz_reg_rd_data_6 = {14'd0, _zz__zz_reg_rd_data_7};
  assign _zz__zz_reg_rd_data_9 = 1'b0;
  assign _zz__zz_reg_rd_data_8 = {14'd0, _zz__zz_reg_rd_data_9};
  assign _zz__zz_reg_rd_data_11 = 1'b0;
  assign _zz__zz_reg_rd_data_10 = {13'd0, _zz__zz_reg_rd_data_11};
  assign _zz__zz_reg_rd_data_13 = 1'b0;
  assign _zz__zz_reg_rd_data_12 = {13'd0, _zz__zz_reg_rd_data_13};
  assign H8 = {8'd0, _zz_H8};
  assign HC = {8'd0, _zz_HC};
  assign H4 = {8'd0, _zz_H4};
  assign H0 = {8'd0, _zz_H0};
  assign nvdla_cdma_s_arbiter_0_wren = ((reg_offset == H8) && reg_wr_en);
  assign nvdla_cdma_s_cbuf_flush_status_0_wren = ((reg_offset == HC) && reg_wr_en);
  assign nvdla_cdma_s_pointer_0_wren = ((reg_offset == H4) && reg_wr_en);
  assign nvdla_cdma_s_status_0_wren = ((reg_offset == H0) && reg_wr_en);
  always @(*) begin
    if((reg_offset == H8)) begin
        _zz_reg_rd_data = {_zz__zz_reg_rd_data,{arb_wmb,{_zz__zz_reg_rd_data_2,producer}}};
    end else if((reg_offset == HC)) begin
        _zz_reg_rd_data = {_zz__zz_reg_rd_data_4,flush_done};
    end else if((reg_offset == H4)) begin
        _zz_reg_rd_data = {_zz__zz_reg_rd_data_6,{consumer,{_zz__zz_reg_rd_data_8,producer}}};
    end else if((reg_offset == H0)) begin
        _zz_reg_rd_data = {_zz__zz_reg_rd_data_10,{status_1,{_zz__zz_reg_rd_data_12,status_0}}};
    end else begin
        _zz_reg_rd_data = 32'h00000000;
    end
  end

  assign reg_rd_data = _zz_reg_rd_data;
  assign arb_weight = _zz_arb_weight;
  assign arb_wmb = _zz_arb_wmb;
  assign producer = _zz_producer;
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      _zz_arb_weight <= 4'b1111;
      _zz_arb_wmb <= 4'b0011;
      _zz_producer <= 1'b0;
    end else begin
      if(nvdla_cdma_s_arbiter_0_wren) begin
        _zz_arb_weight <= reg_wr_data[3 : 0];
      end
      if(nvdla_cdma_s_arbiter_0_wren) begin
        _zz_arb_wmb <= reg_wr_data[19 : 16];
      end
      if(nvdla_cdma_s_pointer_0_wren) begin
        _zz_producer <= reg_wr_data[0];
      end
    end
  end


endmodule
