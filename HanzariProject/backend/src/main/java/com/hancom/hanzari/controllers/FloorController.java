package com.hancom.hanzari.controllers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("api/floors")
public class FloorController {

	@Autowired
	private FloorService floorService;

	@Autowired
	private BuildingService buildingService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<FloorDto>> getAllFloors() {
		List<Floor> floor = floorService.findAll();
		List<FloorDto> result = new ArrayList<FloorDto>();
		floor.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<FloorDto>>(result, HttpStatus.OK);
	}

	@GetMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<FloorDto>> getAllFloorsInBuilding(@PathVariable("building_id") String building_id)
			throws Exception {
		Building building = buildingService.findById(building_id);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", building_id);
		}
		List<Floor> floor = floorService.findByBuiding(building);
		List<FloorDto> result = new ArrayList<FloorDto>();
		floor.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<FloorDto>>(result, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Floor> save(@RequestBody FloorDto floorDto) throws Exception {
		for (Field field : floorDto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(floorDto);
			System.out.println(field.getName() + " : " + value + " // type : " + value.getClass());
		}

		Building building = buildingService.findById(floorDto.getBuilding_id());
		Floor floor = Floor.builder().floorId(floorDto.getFloor_id()).floorName(floorDto.getFloor_name()).building(building)
				.floorIndex(floorDto.getFloor_index()).build();
		return new ResponseEntity<Floor>(floorService.save(floor), HttpStatus.OK);
	}

	@DeleteMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody FloorDto floorDto) throws Exception {
		for (Field field : floorDto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(floorDto);
			System.out.println(field.getName() + " : " + value);
		}

		Building building = buildingService.findById(floorDto.getBuilding_id());
		floorService.deleteByFloorNameAndBuilding(floorDto.getFloor_name(), building);
	}

	@DeleteMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@PathVariable("building_id") String building_id) throws Exception {
		Building building = buildingService.findById(building_id);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", building_id);
		}
		floorService.deleteByBuilding(building);
	}

	// 전체삭제
	@DeleteMapping(value = "/truncate", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> truncate() {
		floorService.truncate();
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

}
