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

	@PostMapping
	public ResponseEntity<Building> save(@RequestBody BuildingDto buildingDto) throws Exception {
		for (Field field : buildingDto.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Object value = field.get(buildingDto);
			System.out.println(field.getName() + " : " + value);
		}
		Building newBuilding = Building.builder().buildingId(buildingDto.getBuilding_id())
				.buildingName(buildingDto.getBuilding_name()).build();
		return new ResponseEntity<Building>(buildingService.save(newBuilding), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteBuilding(@PathVariable("building_id") String buildingId) {
		buildingService.deleteById(buildingId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
