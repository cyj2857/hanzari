package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/buildings")
public class BuildingController {

	@Autowired
	private BuildingService buildingService;

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	// 전체 건물 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<BuildingDto>> getAllBuildings() {

		LOGGER.info("BuildingController.getAllBuildings called.");

		List<Building> buildings = buildingService.findAll();
		List<BuildingDto> result = new ArrayList<BuildingDto>();
		buildings.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<BuildingDto>>(result, HttpStatus.OK);
	}

	// 건물 id로 해당 건물 정보 조회
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@GetMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BuildingDto> getBuilding(@PathVariable("building_id") String buildingId) throws Exception {

		LOGGER.info("BuildingController.getBuilding called. (building_id : {})", buildingId);

		return new ResponseEntity<BuildingDto>(buildingService.findById(buildingId).toDto(), HttpStatus.OK);
	}

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@PostMapping
	public ResponseEntity<Building> save(@RequestBody BuildingDto buildingDto) throws Exception {

		LOGGER.info("BuildingController.save called. (building_id : {})", buildingDto.getBuilding_id());
		
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

	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping(value = "/{building_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteBuilding(@PathVariable("building_id") String buildingId) {
		
		LOGGER.info("BuildingController.deleteBuilding called. (building_id : {})", buildingId);
		
		buildingService.deleteById(buildingId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}