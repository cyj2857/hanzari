package com.hancom.hanzari.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/buildings/{building_id}/floors")
public class FloorController {

	private final FloorService floorService;
	private final BuildingService buildingService;

	// Logger
	private final Logger LOGGER = LoggerFactory.getLogger("EngineLogger");

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<FloorDto>> getAllFloorsInBuilding(@PathVariable("building_id") String buildingId)
			throws Exception {

		LOGGER.info("FloorController.getAllFloorsInBuilding called. (building_id : {})", buildingId);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		List<Floor> floor = floorService.findByBuilding(building);
		List<FloorDto> result = new ArrayList<FloorDto>();
		floor.forEach(e -> result.add(e.toDto()));
		return new ResponseEntity<List<FloorDto>>(result, HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(value = "/{floor_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FloorDto> getFloor(@PathVariable("building_id") String buildingId,
			@PathVariable("floor_id") String floorId) throws Exception {

		LOGGER.info("FloorController.getFloor called. (building_id : {}, floor_id : {})", buildingId, floorId);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}
		return new ResponseEntity<FloorDto>(floorService.findById(floorId).toDto(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@GetMapping(value = "/get-latest-floor", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<FloorDto> getLatestFloor(@PathVariable("building_id") String buildingId) throws Exception {

		LOGGER.info("FloorController.getLatestFloor called. (building_id : {})", buildingId);

		Building building = buildingService.findById(buildingId);
		if (building == null) {
			throw new ResourceNotFoundException("Building", "building_id", buildingId);
		}

		return new ResponseEntity<FloorDto>(floorService.findFirstByOrderByFloorOrderDesc().toDto(), HttpStatus.OK);
	}

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@Transactional
	@PostMapping
	public ResponseEntity<Floor> save(@PathVariable("building_id") String buildingId, @RequestBody FloorDto floorDto)
			throws Exception {

		LOGGER.info("FloorController.save called. (building_id : {}, floor_id : {})", buildingId,
				floorDto.getFloor_id());

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

	@ApiImplicitParams({
			@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header") })
	@DeleteMapping(value = "/{floor_id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> deleteFloorById(@PathVariable("floor_id") String floor_id) {

		LOGGER.info("FloorController.deleteFloorById called. (floor_id : {})", floor_id);

		floorService.deleteById(floor_id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}