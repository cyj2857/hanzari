package com.hancom.hanzari.model;

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
import javax.persistence.PreRemove;
import javax.persistence.Table;

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
//@Builder
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

	//@OneToMany(mappedBy = "floor", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
	@OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonManagedReference
	private List<Seat> seats = new ArrayList<Seat>();

	@PreRemove
	public void preRemove() {
		if (building != null) {
			setBuilding(null);
		}
		if (seats != null) {
			seats.forEach(e -> e.setFloor(null));
		}
	}

//	@PrePersist
//	private void prePersist() {
//		System.out.println("#####\n#####\nprePersist#####\n#####\n");
//	}

//	@PreUpdate
//	public void preUpdate() {
//		System.out.println("#####\n#####\npreUpdate#####\n#####\n");
//		if (building != null) {
//			setBuilding(null);
//		}
//		if (seats != null) {
//			seats.forEach(e -> e.setFloor(null));
//			this.seats.clear();
//		}
//	}

	public void setSeats(List<Seat> seats) {
		System.out.println("#####\n#####\nFloor.setSeats() called\n #####\n#####\n");
//		if(seats == null) {
//	        this.seats.clear();
//	    }
//		if (this.seats == null) {
//			this.seats = seats;
//		} else {
//			this.seats.retainAll(seats);
//			this.seats.addAll(seats);
//		}

		if (this.seats == null) {
			this.seats = seats;
		} else if (this.seats != seats) {
			this.seats.retainAll(seats);
			if (seats != null) {
				this.seats.addAll(seats);
			}
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
