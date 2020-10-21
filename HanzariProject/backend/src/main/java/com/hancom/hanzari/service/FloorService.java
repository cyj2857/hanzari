package com.hancom.hanzari.service;

import java.util.List;

import com.hancom.hanzari.model.Building;
import com.hancom.hanzari.model.Floor;

public interface FloorService {
	public List<Floor> findAll();

	public Floor findById(String floorId) throws Exception;;

	public Floor findByFloorNameAndBuilding(String floorName, Building building) throws Exception;

	public List<Floor> findByBuiding(Building building);

	public void deleteById(String floorId); // D
	
	public void deleteByFloorNameAndBuilding(String floorName, Building building);

	public void deleteByBuilding(Building building);

	public void truncate();

	public Floor save(Floor floor);
	// 이건 나중에 고려해봐야할듯. 지금은 save()가 알아서 INSERT나 UPDATE로 판단해서 작동되지만 이건 Id로 체크하는게아니고
	// floorName이랑 building정보를 동시에 조합해서 비교해봐야하기때문에 추가적으로 구현해야할 수도 있음.
	// public Floor updateByFloorNameAndBuilding(String floorName, Building
	// building);
}
