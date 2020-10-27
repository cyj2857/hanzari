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
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

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
	@Column(name = "floor_id", nullable = false)
	private String floorId;

	@Column(name = "floor_name", nullable = false)
	private String floorName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "building_id", nullable = true)
	@JsonBackReference
	private Building building;

	@Column(name = "floor_order", nullable = false)
	private int floorOrder;

	@Column(name = "floor_image_url", nullable = true)
	private int floorImageUrl;

	@OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	@JsonManagedReference
	private List<Seat> seats;

	@PreRemove
	public void preRemove() {
		if (building != null) {
			setBuilding(null);
		}
		if (seats != null) {
			seats.forEach(e -> e.setFloor(null));
		}
	}

	@PrePersist
	private void prePersist() {
		if (seats != null) {
			seats.forEach(e -> e.setFloor(this));
		}
	}
	
	@PreUpdate
	public void preUpdate() {
		/*if (seats != null) {
			this.seats.clear();
			this.seats.addAll(seats);
		}*/
		if (building != null) {
			setBuilding(null);
		}
		if (seats != null) {
			seats.forEach(e -> e.setFloor(null));
		}
	}

	public void setSeats(List<Seat> seats) {
		if (this.seats != null) {
			this.seats.clear();
		}
		this.seats.addAll(seats);
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
