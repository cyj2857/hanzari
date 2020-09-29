package dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

	@NotBlank
	private String seat_id;

	@NotBlank
	private String floor;
	
	@NotBlank
	private double x;

	@NotBlank
	private double y;
	
	@NotBlank
	private String building_id;

	@NotBlank
	private String employee_id;

	@NotBlank
	private double width;
	
	@NotBlank
	private double height;
	
	@NotBlank
	private double degree;
	
	@NotBlank
	private String shape_id;
	/* 추후 추가될 수도 있음
	@NotBlank
	private Boolean is_group;

	private String group_id;
	 */
	}