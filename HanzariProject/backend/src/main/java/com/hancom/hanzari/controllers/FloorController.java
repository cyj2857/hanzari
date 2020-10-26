package com.hancom.hanzari.controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hancom.hanzari.dto.FloorDto;
import com.hancom.hanzari.exception.ResourceNotFoundException;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Floor;
import com.hancom.hanzari.service.BuildingService;
import com.hancom.hanzari.service.FloorService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/buildings/{building_id}/floors")
public class FloorController {

	@Autowired
	private FloorService floorService;

	@Autowired
	private BuildingService buildingService;

	@Transactional
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<FloorDto>> getAllFloorsInBuilding(@PathVariable("building_id") String buildingId)
			throws Exception {
		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		List<Floor> floor = floorService.findByBuilding(building);
		List<FloorDto> result = new ArrayList<FloorDto>();
		floor.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<FloorDto>>(result, HttpStatus.OK);
	}

	@Transactional
	@GetMapping(value = "/{floor_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FloorDto> getFloorInBuilding(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId) throws Exception {
		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		return new ResponseEntity<FloorDto>(floorService.findById(floorId).toDto(), HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Floor> save(@PathVariable("building_id") String buildingId, @RequestBody FloorDto floorDto)
			throws Exception {
		for (Field field : floorDto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(floorDto);
			System.out.println(field.getName() + " : " + value + " // type : " + value.getClass());
		}

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}

		Floor floor = Floor.builder().floorId(floorDto.getFloor_id()).floorName(floorDto.getFloor_name())
				.building(building).floorOrder(floorDto.getFloor_order()).build();
		// floor.getSeats().clear();
		return new ResponseEntity<Floor>(floorService.save(floor), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{floor_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteFloorById(@PathVariable("floor_id") String floor_id) {
		System.out.println("==================================================================");
		System.out.println("seatId: " + floor_id);
		System.out.println("==================================================================");

		floorService.deleteById(floor_id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	/*
	 * @DeleteMapping(value = "/by-building/{building_id}", produces = {
	 * MediaType.APPLICATION_JSON_VALUE }) public void
	 * deleteByBuildingId(@PathVariable("building_id") String building_id) throws
	 * Exception { Building building = buildingService.findById(building_id); if
	 * (building == null) { throw new ResourceNotFoundException("Building",
	 * "building_id", building_id); } floorService.deleteByBuilding(building); }
	 * 
	 * // 전체삭제
	 * 
	 * @DeleteMapping(value = "/truncate", produces = {
	 * MediaType.APPLICATION_JSON_VALUE }) public ResponseEntity<Void> truncate() {
	 * floorService.truncate(); return new
	 * ResponseEntity<Void>(HttpStatus.NO_CONTENT); }
	 */

}
