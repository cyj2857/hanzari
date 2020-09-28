//package com.hancom.hanzari.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import service.BuildingService;
//
//public class BuildingController {
//	@Autowired
//	private BuildingService buildingService;
//
//	@GetMapping(value = "/all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity get() {
//		return ResponseEntity.ok(boardService.getBoard());
//	}
//
//	@GetMapping(value = "/{seq}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity get(@PathVariable("seq") Long seq) {
//		return ResponseEntity.ok(boardService.getBoard(seq));
//	}
//
//	@PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity add(@RequestBody @Valid BoardParam param) {
//		boardService.add(param);
//		return ResponseEntity.ok(null);
//	}
//
//	@PutMapping(value = "/{seq}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity edit(@RequestBody @Valid BoardParam param, @PathVariable("seq") Long seq) {
//		param.setSeq(seq);
//		boardService.edit(param);
//		return ResponseEntity.ok(null);
//	}
//
//	@DeleteMapping(value = "/{seq}", consumes = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity delete(@PathVariable("seq") Long seq) {
//		boardService.delete(seq);
//		return ResponseEntity.ok(null);
//	}
//}
