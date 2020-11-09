package com.hancom.hanzari.controllers;

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
	public ResponseEntity<FloorDto> getFloor(@PathVariable("building_id") String buildingId,
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
		HttpStatus status = null;
		Building building = buildingService.findByIdNullable(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		Floor floor = floorService.findByIdNullable(floorDto.getFloor_id());
		if (floor != null) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.CREATED;
		}
		floor = Floor.builder().floorId(floorDto.getFloor_id()).floorName(floorDto.getFloor_name()).building(building)
				.floorOrder(floorDto.getFloor_order()).build(); // Create Or Update

		return new ResponseEntity<Floor>(floorService.save(floor), status);
	}

	@DeleteMapping(value = "/{floor_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteFloorById(@PathVariable("floor_id") String floor_id) {
		floorService.deleteById(floor_id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}