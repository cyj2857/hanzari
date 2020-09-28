package dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.Employee;
import model.Figure;
import model.Seat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatDto {

	@NotBlank
	private String seat_id;

	@NotBlank
	private BuildingDto building;

	@NotBlank
	private String floor;

	@NotBlank
	private Boolean is_group;

	@NotBlank
	private String group_id;

	@NotBlank
	private double x;

	@NotBlank
	private double y;

	@NotBlank
	private FigureDto figure;

	@NotBlank
	private String employee_id;
}