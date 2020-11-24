package com.hancom.hanzari.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hancom.hanzari.dto.EmployeeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
@Builder
@SuppressWarnings("serial")
@Table(name = "employee")
public class Employee implements UserDetails {

	@Id
	@NotNull
	@Column(name = "employee_id", nullable = false)
	private String employeeId; // 로그인 시 아이디

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false, length = 100)
	private String password; // 로그인 시 비밀번호

	@NotNull
	@Column(name = "authority", nullable = false)
	private String authority;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@NotNull
	@JoinColumn(name = "addi_info_id", nullable = false)
	private EmployeeAdditionalInfo additionalInfo;

	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	@Column(nullable = true)
	@JsonManagedReference
	private List<Seat> seat; // = new ArrayList<Seat>();

	@ElementCollection(fetch = FetchType.EAGER)
	@Builder.Default
	private List<String> roles = new ArrayList<>(); // 접근 권한 List

	public Employee() {
	}

	public void add(Seat seat) {
		seat.setEmployee(this);
		getSeat().add(seat);
	}

	public void setAdditionalInfo(EmployeeAdditionalInfo additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public List<String> seatIdList() {
		List<String> result = new ArrayList<String>();
		seat.forEach(e -> result.add(e.getSeatId()));
		return result;
	}

	@PreRemove
	public void preRemove() {
		if (seat != null) {
			seat.forEach(e -> e.setEmployee(null)); // 특정 사원이 삭제되기 전 해당 사원이 배정되었던 모든 자리에 대해 employee를 null로 변경
		}
	}

	public EmployeeDto toDto() {
		String status = (additionalInfo == null) ? null : additionalInfo.getStatus();
		String employeeName = (additionalInfo == null) ? null : additionalInfo.getEmployeeName();
		String departmentName = (additionalInfo == null) ? null : additionalInfo.getDepartmentName();
		String extensionNumber = (additionalInfo == null) ? null : additionalInfo.getExtensionNumber();
		return EmployeeDto.builder().employee_id(employeeId).authority(authority).status(status)
				.employee_name(employeeName).department_name(departmentName).extension_number(extensionNumber)
				.seatList(seatIdList()).build();
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return this.employeeId;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isEnabled() {
		return true;
	}
}