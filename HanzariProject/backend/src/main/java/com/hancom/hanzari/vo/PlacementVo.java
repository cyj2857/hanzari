package com.hancom.hanzari.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementVo {
	String floorId;
	String floorName;
	String seatName;
	String employeeId;
}
