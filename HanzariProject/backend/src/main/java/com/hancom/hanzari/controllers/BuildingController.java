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

import com.hancom.hanzari.dto.BuildingDto;
import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.service.BuildingService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/buildings")
public class BuildingController {

	@Autowired
	private BuildingService buildingService;

	// 전체 건물 조회
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BuildingDto>> getAllBuildings() {
		List<Building> buildings = buildingService.findAll();
		List<BuildingDto> result = new ArrayList<BuildingDto>();
		buildings.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<BuildingDto>>(result, HttpStatus.OK);
	}

	// 건물 id로 해당 건물 정보 조회
	@GetMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BuildingDto> getBuilding(@PathVariable("building_id") String buildingId) throws Exception {
		return new ResponseEntity<BuildingDto>(buildingService.findById(buildingId).toDto(), HttpStatus.OK);
	}

	@Transactional
	@PostMapping
	public ResponseEntity<Building> save(@RequestBody BuildingDto buildingDto) throws Exception {
		HttpStatus status = null;
		Building building = buildingService.findByIdNullable(buildingDto.getBuilding_id());
		if (building != null) {
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.CREATED;
		}
		building = Building.builder().buildingId(buildingDto.getBuilding_id())
				.buildingName(buildingDto.getBuilding_name()).build(); // Create Or Update

		return new ResponseEntity<Building>(buildingService.save(building), status);
	}

	@DeleteMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteBuilding(@PathVariable("building_id") String buildingId) {
		buildingService.deleteById(buildingId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}