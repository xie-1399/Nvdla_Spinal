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

  reg        [7:0]    _zz_dout_33;
  reg        [7:0]    _zz_dout;
  reg        [7:0]    _zz_dout_1;
  reg        [7:0]    _zz_dout_2;
  reg        [7:0]    _zz_dout_3;
  reg        [7:0]    _zz_dout_4;
  reg        [7:0]    _zz_dout_5;
  reg        [7:0]    _zz_dout_6;
  reg        [7:0]    _zz_dout_7;
  reg        [7:0]    _zz_dout_8;
  reg        [7:0]    _zz_dout_9;
  reg        [7:0]    _zz_dout_10;
  reg        [7:0]    _zz_dout_11;
  reg        [7:0]    _zz_dout_12;
  reg        [7:0]    _zz_dout_13;
  reg        [7:0]    _zz_dout_14;
  reg        [7:0]    _zz_dout_15;
  reg        [7:0]    _zz_dout_16;
  reg        [7:0]    _zz_dout_17;
  reg        [7:0]    _zz_dout_18;
  reg        [7:0]    _zz_dout_19;
  reg        [7:0]    _zz_dout_20;
  reg        [7:0]    _zz_dout_21;
  reg        [7:0]    _zz_dout_22;
  reg        [7:0]    _zz_dout_23;
  reg        [7:0]    _zz_dout_24;
  reg        [7:0]    _zz_dout_25;
  reg        [7:0]    _zz_dout_26;
  reg        [7:0]    _zz_dout_27;
  reg        [7:0]    _zz_dout_28;
  reg        [7:0]    _zz_dout_29;
  reg        [7:0]    _zz_dout_30;
  reg        [7:0]    _zz_dout_31;
  reg        [4:0]    _zz_dout_32;
  wire       [31:0]   _zz_1;

  always @(*) begin
    case(_zz_dout_32)
      5'b00000 : _zz_dout_33 = _zz_dout;
      5'b00001 : _zz_dout_33 = _zz_dout_1;
      5'b00010 : _zz_dout_33 = _zz_dout_2;
      5'b00011 : _zz_dout_33 = _zz_dout_3;
      5'b00100 : _zz_dout_33 = _zz_dout_4;
      5'b00101 : _zz_dout_33 = _zz_dout_5;
      5'b00110 : _zz_dout_33 = _zz_dout_6;
      5'b00111 : _zz_dout_33 = _zz_dout_7;
      5'b01000 : _zz_dout_33 = _zz_dout_8;
      5'b01001 : _zz_dout_33 = _zz_dout_9;
      5'b01010 : _zz_dout_33 = _zz_dout_10;
      5'b01011 : _zz_dout_33 = _zz_dout_11;
      5'b01100 : _zz_dout_33 = _zz_dout_12;
      5'b01101 : _zz_dout_33 = _zz_dout_13;
      5'b01110 : _zz_dout_33 = _zz_dout_14;
      5'b01111 : _zz_dout_33 = _zz_dout_15;
      5'b10000 : _zz_dout_33 = _zz_dout_16;
      5'b10001 : _zz_dout_33 = _zz_dout_17;
      5'b10010 : _zz_dout_33 = _zz_dout_18;
      5'b10011 : _zz_dout_33 = _zz_dout_19;
      5'b10100 : _zz_dout_33 = _zz_dout_20;
      5'b10101 : _zz_dout_33 = _zz_dout_21;
      5'b10110 : _zz_dout_33 = _zz_dout_22;
      5'b10111 : _zz_dout_33 = _zz_dout_23;
      5'b11000 : _zz_dout_33 = _zz_dout_24;
      5'b11001 : _zz_dout_33 = _zz_dout_25;
      5'b11010 : _zz_dout_33 = _zz_dout_26;
      5'b11011 : _zz_dout_33 = _zz_dout_27;
      5'b11100 : _zz_dout_33 = _zz_dout_28;
      5'b11101 : _zz_dout_33 = _zz_dout_29;
      5'b11110 : _zz_dout_33 = _zz_dout_30;
      default : _zz_dout_33 = _zz_dout_31;
    endcase
  end

  assign _zz_1 = ({31'd0,1'b1} <<< wa);
  assign dout = _zz_dout_33;
  always @(posedge clk or posedge reset) begin
    if(reset) begin
      _zz_dout <= 8'h00;
      _zz_dout_1 <= 8'h00;
      _zz_dout_2 <= 8'h00;
      _zz_dout_3 <= 8'h00;
      _zz_dout_4 <= 8'h00;
      _zz_dout_5 <= 8'h00;
      _zz_dout_6 <= 8'h00;
      _zz_dout_7 <= 8'h00;
      _zz_dout_8 <= 8'h00;
      _zz_dout_9 <= 8'h00;
      _zz_dout_10 <= 8'h00;
      _zz_dout_11 <= 8'h00;
      _zz_dout_12 <= 8'h00;
      _zz_dout_13 <= 8'h00;
      _zz_dout_14 <= 8'h00;
      _zz_dout_15 <= 8'h00;
      _zz_dout_16 <= 8'h00;
      _zz_dout_17 <= 8'h00;
      _zz_dout_18 <= 8'h00;
      _zz_dout_19 <= 8'h00;
      _zz_dout_20 <= 8'h00;
      _zz_dout_21 <= 8'h00;
      _zz_dout_22 <= 8'h00;
      _zz_dout_23 <= 8'h00;
      _zz_dout_24 <= 8'h00;
      _zz_dout_25 <= 8'h00;
      _zz_dout_26 <= 8'h00;
      _zz_dout_27 <= 8'h00;
      _zz_dout_28 <= 8'h00;
      _zz_dout_29 <= 8'h00;
      _zz_dout_30 <= 8'h00;
      _zz_dout_31 <= 8'h00;
      _zz_dout_32 <= 5'h00;
    end else begin
      if(we) begin
        if(_zz_1[0]) begin
          _zz_dout <= di;
        end
        if(_zz_1[1]) begin
          _zz_dout_1 <= di;
        end
        if(_zz_1[2]) begin
          _zz_dout_2 <= di;
        end
        if(_zz_1[3]) begin
          _zz_dout_3 <= di;
        end
        if(_zz_1[4]) begin
          _zz_dout_4 <= di;
        end
        if(_zz_1[5]) begin
          _zz_dout_5 <= di;
        end
        if(_zz_1[6]) begin
          _zz_dout_6 <= di;
        end
        if(_zz_1[7]) begin
          _zz_dout_7 <= di;
        end
        if(_zz_1[8]) begin
          _zz_dout_8 <= di;
        end
        if(_zz_1[9]) begin
          _zz_dout_9 <= di;
        end
        if(_zz_1[10]) begin
          _zz_dout_10 <= di;
        end
        if(_zz_1[11]) begin
          _zz_dout_11 <= di;
        end
        if(_zz_1[12]) begin
          _zz_dout_12 <= di;
        end
        if(_zz_1[13]) begin
          _zz_dout_13 <= di;
        end
        if(_zz_1[14]) begin
          _zz_dout_14 <= di;
        end
        if(_zz_1[15]) begin
          _zz_dout_15 <= di;
        end
        if(_zz_1[16]) begin
          _zz_dout_16 <= di;
        end
        if(_zz_1[17]) begin
          _zz_dout_17 <= di;
        end
        if(_zz_1[18]) begin
          _zz_dout_18 <= di;
        end
        if(_zz_1[19]) begin
          _zz_dout_19 <= di;
        end
        if(_zz_1[20]) begin
          _zz_dout_20 <= di;
        end
        if(_zz_1[21]) begin
          _zz_dout_21 <= di;
        end
        if(_zz_1[22]) begin
          _zz_dout_22 <= di;
        end
        if(_zz_1[23]) begin
          _zz_dout_23 <= di;
        end
        if(_zz_1[24]) begin
          _zz_dout_24 <= di;
        end
        if(_zz_1[25]) begin
          _zz_dout_25 <= di;
        end
        if(_zz_1[26]) begin
          _zz_dout_26 <= di;
        end
        if(_zz_1[27]) begin
          _zz_dout_27 <= di;
        end
        if(_zz_1[28]) begin
          _zz_dout_28 <= di;
        end
        if(_zz_1[29]) begin
          _zz_dout_29 <= di;
        end
        if(_zz_1[30]) begin
          _zz_dout_30 <= di;
        end
        if(_zz_1[31]) begin
          _zz_dout_31 <= di;
        end
      end
      if(re) begin
        _zz_dout_32 <= ra;
      end
    end
  end


endmodule
