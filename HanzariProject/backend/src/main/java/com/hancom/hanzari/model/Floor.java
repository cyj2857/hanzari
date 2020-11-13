package com.hancom.hanzari.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hancom.hanzari.dto.FloorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "floors")
@Builder
public class Floor {

	@Id
	@NotNull
	@Column(name = "floor_id", nullable = false)
	private String floorId;

	@NotNull
	@Column(name = "floor_name", nullable = false)
	private String floorName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "building_id", nullable = true)
	@JsonBackReference
	private Building building;

	@NotNull
	@Column(name = "floor_order", nullable = false)
	private int floorOrder;

	//FloorImageUrl 정보를 Floor측에서 가지고있지 않아도 될 것이라 판단하여(이미지 관련 통신은 따로 axios 호출을 하여 FloorPlan과 통신함) 우선은 주석 처리를 해둠.
	//@Column(name = "floor_image_url", nullable = true)
	//private int floorImageUrl;

	@OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = false)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<Seat> seats;

	//현재까지 구현 상황에서는 DB의 floor와 floor_plan 테이블이 연결되지 않는다.(주된 이유는 클라이언트와 통신할 때 floor와 관련된 axios 호출과 floorPlan(이미지)과 관련된 axios 호출을 나눠서 해주기 때문이다.)
	//따라서 현재까지는 두 테이블이 연결될 필요가 없어 참조되는 필드를 사용하지 않지만 후에 연결하여 호출할 경우가 있을 수도 있기에 주석처리를 해두었다.
	//@OneToOne(cascade = CascadeType.ALL, orphanRemoval = false)
	//@JoinColumn(name="floor_plan_id")
	//private FloorPlan floorPlan;
	
	@PreRemove
	public void preRemove() {
		if (building != null) {
			setBuilding(null);
		}
		if (seats != null) {
			seats.forEach(e -> e.setFloor(null));
		}
	}

	public void setSeats(List<Seat> seats) {
		this.seats.clear();
		if (seats != null) {
			this.seats.addAll(seats);
		}
	}

	public void addSeat(Seat seat) {
		seats.add(seat);
	}

	public void removeSeat(Seat seat) {
		seats.remove(seat);
	}

	public Floor() {
	}

	public FloorDto toDto() {
		String buildingId = (building == null) ? null : building.getBuildingId();
		return FloorDto.builder().floor_id(floorId).floor_name(floorName).building_id(buildingId)
				.floor_order(floorOrder).build();
	}
}