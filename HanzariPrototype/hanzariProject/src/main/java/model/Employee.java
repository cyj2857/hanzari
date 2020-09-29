package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@Table(name = "employee")
public class Employee {

	@Id
	@Column(name = "employee_id", nullable = false)
	String employee_id;

	@Column(name = "authority", nullable = false)
	String authority;

	@Column(name = "employee_name", nullable = false)
	String employee_name;

	@ManyToOne(cascade = CascadeType.ALL) // 관계의 주인
	@JoinColumn(name = "department_id")
	Department department;

	@Column(name = "extension_number", nullable = true)
	String extension_number;

	@OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Column(nullable = true)
	List<Seat> seat = new ArrayList<Seat>();

	public Employee() {
	}

	public void add(Seat seat) {
		seat.setEmployee(this);
		getSeat().add(seat);
	}

	public List<String> seatIdList() {
		List<String> result = new ArrayList<String>();
		seat.forEach(e -> result.add(e.seat_id));
		return result;
	}
}
