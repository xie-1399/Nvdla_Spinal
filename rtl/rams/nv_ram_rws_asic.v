// Generator : SpinalHDL v1.9.4    git head : 270018552577f3bb8e5339ee2583c9c22d324215
// Component : nv_ram_rws
// Git hash  : 595b46de21e9894cca541d4483c8c23e940c7d27

`timescale 1ns/1ps

module nv_ram_rws (
  input  wire          re,
  input  wire          we,
  input  wire [4:0]    ra,
  input  wire [4:0]    wa,
  input  wire [7:0]    di,
  output wire [7:0]    dout,
  input  wire          clk,
  input  wire          reset
);

  reg        [7:0]    _zz__zz_1_port1;
  reg [7:0] _zz_1 [0:31];

  always @(posedge clk) begin
    if(we) begin
      _zz_1[wa] <= di;
    end
  end

  always @(posedge clk) begin
    if(re) begin
      _zz__zz_1_port1 <= _zz_1[ra];
    end
  end

  assign dout = _zz__zz_1_port1;

endmodule
